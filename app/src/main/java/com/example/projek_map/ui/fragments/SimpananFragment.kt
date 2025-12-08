package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.HistoriSimpanan
import com.example.projek_map.data.Simpanan
import com.example.projek_map.data.TransaksiSimpanan
import com.example.projek_map.network.ApiClient
import com.example.projek_map.ui.adapters.HistoriSimpananAdapter
import com.example.projek_map.ui.adapters.TransaksiSimpananAdapter
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

class SimpananFragment : Fragment() {

    private lateinit var tvSaldoPokok: TextView
    private lateinit var tvSaldoWajib: TextView
    private lateinit var tvSaldoSukarela: TextView
    private lateinit var tvTotalSaldo: TextView
    private lateinit var btnSetorPokok: MaterialButton
    private lateinit var btnSetorWajib: MaterialButton
    private lateinit var btnSetorSukarela: MaterialButton
    private lateinit var btnTarikSukarela: MaterialButton
    private lateinit var rvHistori: RecyclerView
    private lateinit var rvTransaksi: RecyclerView
    private lateinit var tvEmptyHistori: TextView
    private lateinit var tvEmptyTransaksi: TextView

    private val dataHistori = mutableListOf<HistoriSimpanan>()
    private val dataTransaksi = mutableListOf<TransaksiSimpanan>()
    private var dataSimpanan: Simpanan? = null

    private lateinit var adapterHistori: HistoriSimpananAdapter
    private lateinit var adapterTransaksi: TransaksiSimpananAdapter

    private val rupiah: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_simpanan, container, false)

        tvSaldoPokok = v.findViewById(R.id.tvSaldoPokok)
        tvSaldoWajib = v.findViewById(R.id.tvSaldoWajib)
        tvSaldoSukarela = v.findViewById(R.id.tvSaldoSukarela)
        tvTotalSaldo = v.findViewById(R.id.tvTotalSaldo)

        btnSetorPokok = v.findViewById(R.id.btnSetorPokok)
        btnSetorWajib = v.findViewById(R.id.btnSetorWajib)
        btnSetorSukarela = v.findViewById(R.id.btnSetorSukarela)
        btnTarikSukarela = v.findViewById(R.id.btnTarikSukarela)

        rvHistori = v.findViewById(R.id.rvHistoriSimpanan)
        rvTransaksi = v.findViewById(R.id.rvTransaksiSimpanan)
        tvEmptyHistori = v.findViewById(R.id.tvEmptyHistori)
        tvEmptyTransaksi = v.findViewById(R.id.tvEmptyTransaksi)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        rvTransaksi.layoutManager = LinearLayoutManager(requireContext())

        adapterHistori = HistoriSimpananAdapter(dataHistori)
        adapterTransaksi = TransaksiSimpananAdapter(
            dataTransaksi,
            onEdit = { _, _ ->
                Toast.makeText(requireContext(), "Edit transaksi belum di-API-kan", Toast.LENGTH_SHORT).show()
            },
            onDelete = { _, _ ->
                Toast.makeText(requireContext(), "Hapus transaksi belum di-API-kan", Toast.LENGTH_SHORT).show()
            }
        )

        rvHistori.adapter = adapterHistori
        rvTransaksi.adapter = adapterTransaksi

        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai()

        if (kodePegawai != null) {
            loadDataSimpanan(kodePegawai)
        } else {
            Toast.makeText(requireContext(), "Kode pegawai tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        btnSetorPokok.setOnClickListener {
            if (kodePegawai != null) showDialogTransaksi(kodePegawai, "pokok")
        }
        btnSetorWajib.setOnClickListener {
            if (kodePegawai != null) showDialogTransaksi(kodePegawai, "wajib")
        }
        btnSetorSukarela.setOnClickListener {
            if (kodePegawai != null) showDialogTransaksi(kodePegawai, "sukarela")
        }
        btnTarikSukarela.setOnClickListener {
            if (kodePegawai != null) showDialogTransaksi(kodePegawai, "penarikan")
        }

        return v
    }

    private fun loadDataSimpanan(kodePegawai: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.getSimpanan(kodePegawai)
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        val body = res.body()!!
                        dataSimpanan = body.simpanan
                        dataHistori.clear()
                        dataHistori.addAll(body.histori ?: emptyList())
                        dataTransaksi.clear()
                        dataTransaksi.addAll(body.transaksi ?: emptyList())

                        adapterHistori.notifyDataSetChanged()
                        adapterTransaksi.notifyDataSetChanged()

                        updateSaldo()
                        updateEmptyState()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal memuat simpanan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error koneksi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateSaldo() {
        val s = dataSimpanan
        val pokok = s?.simpananPokok ?: 0.0
        val wajib = s?.simpananWajib ?: 0.0
        val sukarela = s?.simpananSukarela ?: 0.0
        val total = pokok + wajib + sukarela

        tvSaldoPokok.text = rupiah.format(pokok)
        tvSaldoWajib.text = rupiah.format(wajib)
        tvSaldoSukarela.text = rupiah.format(sukarela)
        tvTotalSaldo.text = rupiah.format(total)
    }

    private fun updateEmptyState() {
        tvEmptyHistori.visibility = if (dataHistori.isEmpty()) View.VISIBLE else View.GONE
        rvHistori.visibility = if (dataHistori.isEmpty()) View.GONE else View.VISIBLE

        tvEmptyTransaksi.visibility = if (dataTransaksi.isEmpty()) View.VISIBLE else View.GONE
        rvTransaksi.visibility = if (dataTransaksi.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showDialogTransaksi(kodePegawai: String, jenis: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_transaksi_simpanan, null)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val etKeterangan = dialogView.findViewById<EditText>(R.id.etKeterangan)
        val tvLabel = dialogView.findViewById<TextView>(R.id.tvLabelJenis)

        tvLabel.text = when (jenis) {
            "pokok" -> "Setoran Simpanan Pokok"
            "wajib" -> "Setoran Simpanan Wajib"
            "sukarela" -> "Setoran Simpanan Sukarela"
            "penarikan" -> "Penarikan Simpanan Sukarela"
            else -> "Transaksi Simpanan"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Transaksi Simpanan")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val jumlah = etJumlah.text.toString().toDoubleOrNull()
                val ket = etKeterangan.text.toString().trim()

                if (jumlah == null || jumlah <= 0.0) {
                    Toast.makeText(requireContext(), "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                tambahTransaksiSimpanan(kodePegawai, jenis, jumlah, ket)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun tambahTransaksiSimpanan(
        kodePegawai: String,
        jenis: String,
        jumlah: Double,
        keterangan: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.tambahSimpanan(
                    kodePegawai = kodePegawai,
                    jenis = jenis,
                    jumlah = jumlah,
                    keterangan = keterangan
                )
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        Toast.makeText(
                            requireContext(),
                            "Transaksi simpanan berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadDataSimpanan(kodePegawai)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal menyimpan transaksi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error koneksi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
