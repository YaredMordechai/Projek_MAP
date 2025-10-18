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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.HistoriPembayaran
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
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
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

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
        // Adapter milikmu menerima List<HistoriPembayaran> → kita kirim display (MutableList) sebagai List
        adapter = HistoriPembayaranAdapter(display)
        rvHistori.adapter = adapter

        btnCatat.setOnClickListener { showCatatDialog() }

        refresh()
        return v
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        // urutkan terbaru dulu (opsional)
        val list = DummyUserData.historiPembayaranList.sortedByDescending { it.tanggal }
        display.clear()
        display.addAll(list)
        adapter.notifyDataSetChanged()

        tvEmpty.visibility = if (display.isEmpty()) View.VISIBLE else View.GONE
        rvHistori.visibility = if (display.isEmpty()) View.GONE else View.VISIBLE
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

                // Parsing jumlah yang tahan titik/koma & desimal
                val jumlahRaw = edtJumlah.text.toString().trim()
                val jumlah = jumlahRaw
                    .replace(".", "")        // 200.000 -> 200000
                    .replace(",", "")        // 200,000.50 -> 20000050 (nanti dibagi 100 kalau perlu)
                    .toLongOrNull()
                    ?.let { it.toInt() }     // kita simpan sebagai Int sesuai DummyUserData

                if (kode.isEmpty() || pinjamanId == null || jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Isi data dengan benar.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Pastikan pinjaman ada & milik Kode Pegawai tsb
                val pinj = DummyUserData.pinjamanList.find { it.id == pinjamanId }
                if (pinj == null) {
                    Toast.makeText(requireContext(), "Pinjaman #$pinjamanId tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (!pinj.kodePegawai.equals(kode, true)) {
                    Toast.makeText(requireContext(), "Pinjaman #$pinjamanId bukan milik $kode.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Validasi sisa angsuran
                val sisa = DummyUserData.getSisaAngsuran(pinjamanId)
                if (sisa <= 0) {
                    Toast.makeText(requireContext(), "Pinjaman sudah lunas.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (jumlah > sisa) {
                    Toast.makeText(requireContext(), "Jumlah melebihi sisa (Rp ${rupiah.format(sisa)}).", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Catat pembayaran → masuk ke historiPembayaranList
                DummyUserData.catatPembayaranAngsuranAdmin(
                    kodePegawai = kode,
                    pinjamanId = pinjamanId,
                    jumlahBayar = jumlah
                )

                // Auto-lunas jika sisa habis
                val sisaBaru = DummyUserData.getSisaAngsuran(pinjamanId)
                if (sisaBaru == 0) DummyUserData.setStatusPinjaman(pinjamanId, "Lunas")

                // Refresh list agar histori langsung terlihat
                refresh()
                Toast.makeText(requireContext(), "Pembayaran dicatat.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

}
