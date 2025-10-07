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
import com.example.projek_map.PinjamanAdapter
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class PinjamanFragment : Fragment() {

    private lateinit var txtPinjamanAktif: TextView
    private lateinit var rvPendingPinjaman: RecyclerView
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvRiwayatSelesai: RecyclerView
    private lateinit var inputNominal: TextInputEditText
    private lateinit var inputTenor: TextInputEditText
    private lateinit var btnAjukan: MaterialButton

    private lateinit var pendingAdapter: PinjamanAdapter
    private lateinit var aktifAdapter: PinjamanAdapter
    private lateinit var selesaiAdapter: PinjamanAdapter

    private val pendingList = mutableListOf<Pinjaman>()
    private val aktifList = mutableListOf<Pinjaman>()
    private val selesaiList = mutableListOf<Pinjaman>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        // 🔹 Bind views
        txtPinjamanAktif = view.findViewById(R.id.txtPinjamanAktif)
        rvPendingPinjaman = view.findViewById(R.id.rvPendingPinjaman)
        rvPinjamanAktif = view.findViewById(R.id.rvRiwayatPinjaman)
        rvRiwayatSelesai = view.findViewById(R.id.rvRiwayatSelesai)
        inputNominal = view.findViewById(R.id.inputNominalPinjaman)
        inputTenor = view.findViewById(R.id.inputTenor)
        btnAjukan = view.findViewById(R.id.btnAjukanPinjaman)

        // 🔹 Setup RecyclerViews
        rvPendingPinjaman.layoutManager = LinearLayoutManager(requireContext())
        rvPinjamanAktif.layoutManager = LinearLayoutManager(requireContext())
        rvRiwayatSelesai.layoutManager = LinearLayoutManager(requireContext())

        pendingAdapter = PinjamanAdapter(pendingList) {
            Toast.makeText(requireContext(), "Menunggu: Rp ${it.jumlah}", Toast.LENGTH_SHORT).show()
        }

        aktifAdapter = PinjamanAdapter(aktifList) {
            Toast.makeText(requireContext(), "Aktif: Rp ${it.jumlah}", Toast.LENGTH_SHORT).show()
        }

        selesaiAdapter = PinjamanAdapter(selesaiList) {
            Toast.makeText(requireContext(), "Selesai: Rp ${it.jumlah}", Toast.LENGTH_SHORT).show()
        }

        rvPendingPinjaman.adapter = pendingAdapter
        rvPinjamanAktif.adapter = aktifAdapter
        rvRiwayatSelesai.adapter = selesaiAdapter

        // 🔹 Load data awal
        loadDummyData()

        // 🔹 Tombol ajukan pinjaman
        btnAjukan.setOnClickListener { ajukanPinjamanBaru() }

        return view
    }

    private fun loadDummyData() {
        pendingList.clear()
        aktifList.clear()
        selesaiList.clear()

        val allPinjaman = DummyUserData.pinjamanList

        pendingList.addAll(allPinjaman.filter { it.status.equals("Proses", true) })
        aktifList.addAll(allPinjaman.filter { it.status.equals("Disetujui", true) })
        selesaiList.addAll(allPinjaman.filter { it.status.equals("Lunas", true) || it.status.equals("Ditolak", true) })

        pendingAdapter.notifyDataSetChanged()
        aktifAdapter.notifyDataSetChanged()
        selesaiAdapter.notifyDataSetChanged()

        updatePinjamanAktif()
    }

    private fun ajukanPinjamanBaru() {
        val nominal = inputNominal.text?.toString()?.trim()?.toIntOrNull()
        val tenor = inputTenor.text?.toString()?.trim()?.toIntOrNull()

        if (nominal == null || tenor == null) {
            Toast.makeText(requireContext(), "Isi nominal dan tenor dengan benar", Toast.LENGTH_SHORT).show()
            return
        }

        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai() ?: "UNKNOWN"

        val newPinjaman = Pinjaman(
            id = DummyUserData.pinjamanList.size + 1,
            kodePegawai = kodePegawai,
            jumlah = nominal,
            tenor = tenor,
            status = "Proses"
        )

        DummyUserData.pinjamanList.add(0, newPinjaman)
        pendingList.add(0, newPinjaman)
        pendingAdapter.notifyItemInserted(0)
        rvPendingPinjaman.scrollToPosition(0)

        inputNominal.setText("")
        inputTenor.setText("")

        updatePinjamanAktif()
        Toast.makeText(requireContext(), "Pinjaman diajukan!", Toast.LENGTH_SHORT).show()
    }

    private fun updatePinjamanAktif() {
        val aktifLoans = aktifList.filter { it.status.equals("Disetujui", true) }

        if (aktifLoans.isNotEmpty()) {
            val total = aktifLoans.sumOf { it.jumlah }
            val longestTenor = aktifLoans.maxOfOrNull { it.tenor } ?: 0
            txtPinjamanAktif.text = "Pinjaman aktif: Rp $total (${longestTenor} bulan)"
        } else {
            txtPinjamanAktif.text = "Belum ada pinjaman aktif"
        }
    }
}