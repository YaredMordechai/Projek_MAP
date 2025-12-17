package com.example.projek_map.ml

import android.content.Context
import com.google.gson.Gson
import java.io.BufferedInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.ln
import kotlin.math.sqrt

data class Meta(
    val word_ngram_range: List<Int>,
    val char_ngram_range: List<Int>,
    val word_norm: String?,
    val char_norm: String?,
    val sublinear_tf_word: Boolean,
    val sublinear_tf_char: Boolean,
    val lowercase: Boolean,
    val n_features_word: Int,
    val n_features_char: Int,
    val n_features_total: Int,
    val n_classes: Int
)

data class Prediction(
    val label: String,
    val confidence: Float,
    val probs: FloatArray
)

class SklearnTfidfLRClassifier(
    private val context: Context,
    private val assetDir: String = "ml"
) {
    private val gson = Gson()

    private val meta: Meta
    private val labels: List<String>
    private val wordVocab: Map<String, Int>
    private val charVocab: Map<String, Int>

    private val wordIdf: FloatArray
    private val charIdf: FloatArray

    private val W: Array<FloatArray>
    private val b: FloatArray

    init {
        meta = readJson("$assetDir/meta.json", Meta::class.java)

        labels = readJson("$assetDir/labels.json", Array<String>::class.java).toList()

        // Gson parse Map<String, Double> -> convert to Int
        wordVocab = readJsonRawMap("$assetDir/word_vocab.json")
        charVocab = readJsonRawMap("$assetDir/char_vocab.json")

        wordIdf = readFloatBin("$assetDir/word_idf.bin", meta.n_features_word)
        charIdf = readFloatBin("$assetDir/char_idf.bin", meta.n_features_char)

        val wFlat = readFloatBin("$assetDir/W.bin", meta.n_classes * meta.n_features_total)
        W = Array(meta.n_classes) { c ->
            FloatArray(meta.n_features_total) { j ->
                wFlat[c * meta.n_features_total + j]
            }
        }

        b = readFloatBin("$assetDir/b.bin", meta.n_classes)
    }

    fun predict(rawText: String): Prediction {
        val text = preprocess(rawText)

        val feats = HashMap<Int, Float>(1024)

        addWordTfidf(text, feats)

        val offsetChar = meta.n_features_word
        addCharTfidf(text, feats, offsetChar)

        l2Normalize(feats)

        val logits = FloatArray(meta.n_classes) { c ->
            var s = b[c]
            for ((idx, value) in feats) s += W[c][idx] * value
            s
        }

        val probs = softmax(logits)
        var best = 0
        for (i in 1 until probs.size) if (probs[i] > probs[best]) best = i

        return Prediction(
            label = labels[best],
            confidence = probs[best],
            probs = probs
        )
    }

    // =========================
    // PREPROCESS (WAJIB disamakan dengan app.ipynb kamu kalau mau 1:1)
    // Saat ini: minimal supaya jalan.
    // =========================
    private fun preprocess(s: String): String {
        var t = s
        if (meta.lowercase) t = t.lowercase()
        t = t.trim()
        // TODO: port preprocessing dari app.ipynb:
        // - normalize_leetspeak_safe
        // - remove_noise
        // - stopwords
        // - typo fix (KBBI)
        return t
    }

    private fun addWordTfidf(text: String, feats: MutableMap<Int, Float>) {
        val nMin = meta.word_ngram_range[0]
        val nMax = meta.word_ngram_range[1]

        val tokens = text.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (tokens.isEmpty()) return

        val tf = HashMap<Int, Int>(512)

        for (n in nMin..nMax) {
            if (tokens.size < n) continue
            for (i in 0..(tokens.size - n)) {
                val gram = tokens.subList(i, i + n).joinToString(" ")
                val idx = wordVocab[gram] ?: continue
                tf[idx] = (tf[idx] ?: 0) + 1
            }
        }

        for ((idx, count) in tf) {
            val tfValue = if (meta.sublinear_tf_word) (1f + ln(count.toFloat())) else count.toFloat()
            val value = tfValue * wordIdf[idx]
            feats[idx] = (feats[idx] ?: 0f) + value
        }
    }

    private fun addCharTfidf(text: String, feats: MutableMap<Int, Float>, offset: Int) {
        val nMin = meta.char_ngram_range[0]
        val nMax = meta.char_ngram_range[1]

        val s = text
        if (s.isEmpty()) return

        val tf = HashMap<Int, Int>(2048)

        for (n in nMin..nMax) {
            if (s.length < n) continue
            for (i in 0..(s.length - n)) {
                val gram = s.substring(i, i + n)
                val idxLocal = charVocab[gram] ?: continue
                tf[idxLocal] = (tf[idxLocal] ?: 0) + 1
            }
        }

        for ((idxLocal, count) in tf) {
            val tfValue = if (meta.sublinear_tf_char) (1f + ln(count.toFloat())) else count.toFloat()
            val value = tfValue * charIdf[idxLocal]
            val idx = offset + idxLocal
            feats[idx] = (feats[idx] ?: 0f) + value
        }
    }

    private fun l2Normalize(feats: MutableMap<Int, Float>) {
        var sumSq = 0.0
        for (v in feats.values) sumSq += (v * v).toDouble()
        val norm = sqrt(sumSq).toFloat()
        if (norm <= 0f) return
        for ((k, v) in feats.toMap()) feats[k] = v / norm
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val max = logits.maxOrNull() ?: 0f
        val exps = FloatArray(logits.size)
        var sum = 0.0
        for (i in logits.indices) {
            val e = kotlin.math.exp((logits[i] - max).toDouble()).toFloat()
            exps[i] = e
            sum += e
        }
        val sumF = sum.toFloat()
        for (i in exps.indices) exps[i] = exps[i] / sumF
        return exps
    }

    private fun <T> readJson(path: String, clazz: Class<T>): T {
        val json = context.assets.open(path).bufferedReader().use { it.readText() }
        return gson.fromJson(json, clazz)
    }

    private fun readJsonRawMap(path: String): Map<String, Int> {
        val json = context.assets.open(path).bufferedReader().use { it.readText() }
        val raw = gson.fromJson(json, Map::class.java) as Map<*, *>
        val out = HashMap<String, Int>(raw.size)
        for ((k, v) in raw) {
            out[k as String] = (v as Double).toInt()
        }
        return out
    }

    private fun readFloatBin(path: String, n: Int): FloatArray {
        context.assets.open(path).use { input ->
            val buf = ByteArray(n * 4)
            val read = BufferedInputStream(input).read(buf)
            if (read != buf.size) {
                throw IllegalStateException("File $path size tidak sesuai. Expected=${buf.size}, got=$read")
            }
            val bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN)
            val arr = FloatArray(n)
            for (i in 0 until n) arr[i] = bb.float
            return arr
        }
    }
}
