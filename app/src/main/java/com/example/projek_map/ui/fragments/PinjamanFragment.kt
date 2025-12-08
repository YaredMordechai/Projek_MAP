package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.network.ApiClient
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.example.projek_map.ui.dialogs.HistoriPembayaranDialog
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

class PinjamanFragment : Fragment() {

    private lateinit var tvTotalPinjamanAktif: TextView
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvRiwayatPinjaman: RecyclerView
    private lateinit var tvEmptyAktif: TextView
    private lateinit var tvEmptyRiwayat: TextView
    private lateinit var btnAjukanPinjaman: MaterialButton

    private val listAktif = mutableListOf<Pinjaman>()
    private val listRiwayat = mutableListOf<Pinjaman>()

    private lateinit var adapterAktif: PinjamanAdapter
    private lateinit var adapterRiwayat: PinjamanAdapter

    private val rupiah: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    private var kodePegawai: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        tvTotalPinjamanAktif = v.findViewById(R.id.tvTotalPinjamanAktif)
        rvPinjamanAktif = v.findViewById(R.id.rvPinjamanAktif)
        rvRiwayatPinjaman = v.findViewById(R.id.rvRiwayatPinjaman)
        tvEmptyAktif = v.findViewById(R.id.tvEmptyPinjamanAktif)
        tvEmptyRiwayat = v.findViewById(R.id.tvEmptyRiwayatPinjaman)
        btnAjukanPinjaman = v.findViewById(R.id.btnAjukanPinjaman)

        rvPinjamanAktif.layoutManager = LinearLayoutManager(requireContext())
        rvRiwayatPinjaman.layoutManager = LinearLayoutManager(requireContext())

        adapterAktif = PinjamanAdapter(listAktif) { pinjaman ->
            openDetailPinjaman(pinjaman)
        }
        adapterRiwayat = PinjamanAdapter(listRiwayat) { pinjaman ->
            openDetailPinjaman(pinjaman)
        }

        rvPinjamanAktif.adapter = adapterAktif
        rvRiwayatPinjaman.adapter = adapterRiwayat

        val pref = PrefManager(requireContext())
        kodePegawai = pref.getKodePegawai()

        if (kodePegawai != null) {
            loadPinjaman(kodePegawai!!)
        } else {
            Toast.makeText(requireContext(), "Kode pegawai tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        btnAjukanPinjaman.setOnClickListener {
            if (kodePegawai != null) showAjukanPinjamanDialog(kodePegawai!!)
        }

        return v
    }

    private fun loadPinjaman(kode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.getPinjamanUser(kode)
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        val list = res.body()?.pinjaman ?: emptyList()
                        listAktif.clear()
                        listRiwayat.clear()

                        list.forEach { p ->
                            when (p.status.lowercase()) {
                                "disetujui", "aktif", "proses" -> listAktif.add(p)
                                else -> listRiwayat.add(p)
                            }
                        }

                        adapterAktif.notifyDataSetChanged()
                        adapterRiwayat.notifyDataSetChanged()

                        updateTotalAktif()
                        updateEmptyState()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal memuat pinjaman",
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

    private fun updateTotalAktif() {
        val total = listAktif.sumOf { it.jumlah }
        tvTotalPinjamanAktif.text = rupiah.format(total)
    }

    private fun updateEmptyState() {
        tvEmptyAktif.visibility = if (listAktif.isEmpty()) View.VISIBLE else View.GONE
        rvPinjamanAktif.visibility = if (listAktif.isEmpty()) View.GONE else View.VISIBLE

        tvEmptyRiwayat.visibility = if (listRiwayat.isEmpty()) View.VISIBLE else View.GONE
        rvRiwayatPinjaman.visibility = if (listRiwayat.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showAjukanPinjamanDialog(kodePegawai: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_ajukan_pinjaman, null)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlahPinjaman)
        val etTenor = dialogView.findViewById<EditText>(R.id.etTenorPinjaman)

        AlertDialog.Builder(requireContext())
            .setTitle("Ajukan Pinjaman")
            .setView(dialogView)
            .setPositiveButton("Ajukan") { _, _ ->
                val jumlah = etJumlah.text.toString().toIntOrNull()
                val tenor = etTenor.text.toString().toIntOrNull()

                if (jumlah == null || jumlah <= 0 || tenor == null || tenor <= 0) {
                    Toast.makeText(requireContext(), "Data pinjaman tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                ajukanPinjaman(kodePegawai, jumlah, tenor)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun ajukanPinjaman(kodePegawai: String, jumlah: Int, tenor: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.ajukanPinjaman(kodePegawai, jumlah, tenor)
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        Toast.makeText(
                            requireContext(),
                            "Pengajuan pinjaman berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadPinjaman(kodePegawai)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal mengajukan pinjaman",
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

    private fun openDetailPinjaman(pinjaman: Pinjaman) {
        // Tetap pakai PinjamanDetailFragment dan HistoriPembayaranDialog seperti sebelumnya
        val dialog = HistoriPembayaranDialog.newInstance(
            pinjamanId = pinjaman.id,
            jumlahAngsuran = pinjaman.jumlah / pinjaman.tenor
        )
        dialog.show(parentFragmentManager, "HistoriPembayaranDialog")
    }
}
