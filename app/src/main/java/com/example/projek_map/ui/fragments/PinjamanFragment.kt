package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class PinjamanFragment : Fragment() {

    private lateinit var rvPending: RecyclerView
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvSelesai: RecyclerView
    private lateinit var btnAjukan: MaterialButton
    private lateinit var inputNominal: TextInputEditText
    private lateinit var inputTenor: TextInputEditText
    private lateinit var txtPinjamanAktif: TextView

    private lateinit var adapterPending: PinjamanAdapter
    private lateinit var adapterAktif: PinjamanAdapter
    private lateinit var adapterSelesai: PinjamanAdapter

    private val dataPending = mutableListOf<Pinjaman>()
    private val dataAktif = mutableListOf<Pinjaman>()
    private val dataSelesai = mutableListOf<Pinjaman>()

    private var isAdmin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        // ✅ Ambil flag admin dari arguments
        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        // ✅ Inisialisasi semua view
        txtPinjamanAktif = view.findViewById(R.id.txtPinjamanAktif)
        inputNominal = view.findViewById(R.id.inputNominalPinjaman)
        inputTenor = view.findViewById(R.id.inputTenor)
        btnAjukan = view.findViewById(R.id.btnAjukanPinjaman)
        rvPending = view.findViewById(R.id.rvPendingPinjaman)
        rvPinjamanAktif = view.findViewById(R.id.rvRiwayatPinjaman)
        rvSelesai = view.findViewById(R.id.rvRiwayatSelesai)

        // ✅ Jika admin → tampilkan pesan kosong dan sembunyikan semua elemen
        if (isAdmin) {
            txtPinjamanAktif.text = "Admin tidak memiliki data pinjaman"
            inputNominal.visibility = View.GONE
            inputTenor.visibility = View.GONE
            btnAjukan.visibility = View.GONE
            rvPending.visibility = View.GONE
            rvPinjamanAktif.visibility = View.GONE
            rvSelesai.visibility = View.GONE
            return view
        }

        // 🔹 Layout Manager
        rvPending.layoutManager = LinearLayoutManager(requireContext())
        rvPinjamanAktif.layoutManager = LinearLayoutManager(requireContext())
        rvSelesai.layoutManager = LinearLayoutManager(requireContext())

        // 🔹 Setup adapter
        adapterPending = PinjamanAdapter(dataPending) { showPinjamanDetailDialog(it) }
        adapterAktif = PinjamanAdapter(dataAktif) { showPinjamanDetailDialog(it) }
        adapterSelesai = PinjamanAdapter(dataSelesai) { showPinjamanDetailDialog(it) }

        rvPending.adapter = adapterPending
        rvPinjamanAktif.adapter = adapterAktif
        rvSelesai.adapter = adapterSelesai

        // 🔹 Load data awal
        loadDummyPinjaman()
        updateStatusCard()

        // 🔹 Tombol Ajukan Pinjaman
        btnAjukan.setOnClickListener {
            val nominalText = inputNominal.text?.toString()?.trim()
            val tenorText = inputTenor.text?.toString()?.trim()

            if (nominalText.isNullOrEmpty() || tenorText.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Isi semua kolom dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nominal = nominalText.toIntOrNull()
            val tenor = tenorText.toIntOrNull()

            if (nominal == null || tenor == null) {
                Toast.makeText(requireContext(), "Nominal dan Tenor harus berupa angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPinjaman = Pinjaman(
                id = DummyUserData.pinjamanList.size + 1,
                kodePegawai = "EMP001",
                jumlah = nominal,
                tenor = tenor,
                bunga = 10.0,
                angsuranTerbayar = 0,
                status = "Proses"
            )

            DummyUserData.pinjamanList.add(0, newPinjaman)
            dataPending.add(0, newPinjaman)
            adapterPending.notifyItemInserted(0)
            rvPending.scrollToPosition(0)

            inputNominal.setText("")
            inputTenor.setText("")

            Toast.makeText(requireContext(), "Pinjaman diajukan!", Toast.LENGTH_SHORT).show()
            updateStatusCard()
        }

        return view
    }

    // ✅ Jangan refresh kalau admin
    override fun onResume() {
        super.onResume()
        if (!isAdmin) {
            refreshAllLists()
        }
    }

    private fun refreshAllLists() {
        loadDummyPinjaman()
        adapterPending.notifyDataSetChanged()
        adapterAktif.notifyDataSetChanged()
        adapterSelesai.notifyDataSetChanged()
        updateStatusCard()
    }

    private fun loadDummyPinjaman() {
        dataPending.clear()
        dataAktif.clear()
        dataSelesai.clear()

        DummyUserData.pinjamanList.forEach { pinjaman ->
            when (pinjaman.status.lowercase()) {
                "proses", "menunggu" -> dataPending.add(pinjaman)
                "disetujui", "aktif" -> dataAktif.add(pinjaman)
                "selesai", "lunas" -> dataSelesai.add(pinjaman)
            }
        }
    }

    private fun updateStatusCard() {
        val aktif = dataAktif.firstOrNull()
        txtPinjamanAktif.text = if (aktif != null) {
            "Pinjaman Aktif: Rp ${formatRupiah(aktif.jumlah.toDouble())} (${aktif.tenor} bulan)"
        } else {
            "Belum ada pinjaman aktif"
        }
    }

    private fun showPinjamanDetailDialog(pinjaman: Pinjaman) {
        val bungaPersen = 0.1
        val bunga = pinjaman.jumlah * bungaPersen
        val totalCicilan = pinjaman.jumlah + bunga
        val angsuranPerBulan = totalCicilan / pinjaman.tenor

        val historiPembayaran = DummyUserData.getHistoriPembayaran(pinjaman.id)
        val sudahDibayar = historiPembayaran.sumOf { it.jumlah }
        val sisaCicilan = totalCicilan - sudahDibayar

        val message = """
        Pokok Pinjaman  : Rp ${formatRupiah(pinjaman.jumlah.toDouble())}
        Bunga (10%)     : Rp ${formatRupiah(bunga)}
        Total Cicilan   : Rp ${formatRupiah(totalCicilan)}
        Tenor           : ${pinjaman.tenor} bulan
        Angsuran/Bulan  : Rp ${formatRupiah(angsuranPerBulan)}
        Sisa Cicilan    : Rp ${formatRupiah(sisaCicilan)}
        Status          : ${pinjaman.status}
        """.trimIndent()

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Rincian Pinjaman #${pinjaman.id}")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .setNeutralButton("Lihat Riwayat Angsuran") { _, _ ->
                showHistoriPembayaran(pinjaman.id)
            }
            .show()
    }

    private fun showHistoriPembayaran(pinjamanId: Int) {
        val histori = DummyUserData.getHistoriPembayaran(pinjamanId)
        val view = layoutInflater.inflate(R.layout.dialog_histori_pembayaran, null)
        val rvHistori = view.findViewById<RecyclerView>(R.id.rvHistoriPembayaran)
        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        rvHistori.adapter = HistoriPembayaranAdapter(histori)

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Histori Pembayaran")
            .setView(view)
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun formatRupiah(value: Double): String {
        return String.format("%,.0f", value).replace(',', '.')
    }
}