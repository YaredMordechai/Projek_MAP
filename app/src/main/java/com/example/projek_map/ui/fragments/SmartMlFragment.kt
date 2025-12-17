package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.ml.SklearnTfidfLRClassifier

class SmartMlFragment : Fragment() {

    private lateinit var clf: SklearnTfidfLRClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clf = SklearnTfidfLRClassifier(requireContext()) // load assets/ml/*
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_smart_ml, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val et = view.findViewById<EditText>(R.id.etInput)
        val btn = view.findViewById<Button>(R.id.btnPredict)
        val tv = view.findViewById<TextView>(R.id.tvOutput)

        btn.setOnClickListener {
            val text = et.text.toString().trim()
            if (text.isEmpty()) {
                tv.text = "Output: teks masih kosong."
                return@setOnClickListener
            }

            val pred = clf.predict(text)
            tv.text = "Label: ${pred.label}\nConfidence: ${(pred.confidence * 100).toInt()}%"
        }
    }
}
