package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.HistoriPembayaran
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
import com.example.projek_map.ui.viewmodels.KelolaPembayaranAngsuranViewModel
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class KelolaPembayaranAngsuranFragment : Fragment() {

    private lateinit var rvHistori: RecyclerView
    private lateinit var btnCatat: MaterialButton
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: HistoriPembayaranAdapter

    // List tampilan (bisa dimodifikasi lalu notifyDataSetChanged)
    private val display = mutableListOf<HistoriPembayaran>()
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    private val viewModel: KelolaPembayaranAngsuranViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 🔹 PAKAI layout fragment_kelola_pinjaman (yang sudah disisipi blok pembayaran)
        val v = inflater.inflate(R.layout.fragment_kelola_pinjaman, container, false)

        rvHistori = v.findViewById(R.id.rvHistoriPembayaran)
        btnCatat = v.findViewById(R.id.btnCatatPembayaran)
        tvEmpty = v.findViewById(R.id.tvEmptyPembayaran)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoriPembayaranAdapter(display)
        rvHistori.adapter = adapter

        btnCatat.setOnClickListener { showCatatDialog() }

        setupObservers()
        refresh()

        return v
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun setupObservers() {
        viewModel.historiList.observe(viewLifecycleOwner) { list ->
            val sorted = (list ?: emptyList()).sortedByDescending { it.tanggal }

            display.clear()
            display.addAll(sorted)
            adapter.notifyDataSetChanged()

            tvEmpty.visibility = if (display.isEmpty()) View.VISIBLE else View.GONE
            rvHistori.visibility = if (display.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.toast.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }
    }

    private fun refresh() {
        viewModel.refresh()
    }

    private fun showCatatDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_catat_pembayaran, null)
        val edtKode = dialogView.findViewById<EditText>(R.id.edtKodePegawai)
        val edtPinjamanId = dialogView.findViewById<EditText>(R.id.edtPinjamanId)
        val edtJumlah = dialogView.findViewById<EditText>(R.id.edtJumlahBayar)

        AlertDialog.Builder(requireContext())
            .setTitle("Catat Pembayaran Angsuran")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val kode = edtKode.text.toString().trim()
                val pinjamanId = edtPinjamanId.text.toString().trim().toIntOrNull()

                val jumlahRaw = edtJumlah.text.toString().trim()
                val jumlah = jumlahRaw.replace(".", "").replace(",", "").toIntOrNull()

                if (kode.isEmpty() || pinjamanId == null || jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Isi data dengan benar.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.catatPembayaranAdmin(
                    kodePegawai = kode,
                    pinjamanId = pinjamanId,
                    jumlah = jumlah
                )
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
