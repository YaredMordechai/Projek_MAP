package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.PinjamanAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.example.projek_map.utils.PrefManager

class PinjamanFragment : Fragment() {

    private lateinit var rvPinjaman: RecyclerView
    private lateinit var btnAjukan: MaterialButton
    private lateinit var inputNominal: TextInputEditText
    private lateinit var inputTenor: TextInputEditText

    private lateinit var adapter: PinjamanAdapter
    private val dataPinjaman = mutableListOf<Pinjaman>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        // 🔹 Inisialisasi view
        rvPinjaman = view.findViewById(R.id.rvRiwayatPinjaman)
        btnAjukan = view.findViewById(R.id.btnAjukanPinjaman)
        inputNominal = view.findViewById(R.id.inputNominalPinjaman)
        inputTenor = view.findViewById(R.id.inputTenor)

        // 🔹 Setup RecyclerView
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())

        // 🔹 Load data awal
        loadDummyPinjaman()

        // 🔹 Setup Adapter
        adapter = PinjamanAdapter(dataPinjaman) { pinjaman ->
            Toast.makeText(
                requireContext(),
                "Pinjaman Rp ${pinjaman.jumlah} (${pinjaman.tenor} bulan) - ${pinjaman.status}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvPinjaman.adapter = adapter

        // 🔹 Tombol Ajukan Pinjaman Baru
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

            // 🔹 Ambil kode pegawai dari PrefManager (user yang login)
            val pref = PrefManager(requireContext())
            val kodePegawai = pref.getKodePegawai() ?: "UNKNOWN"

            // 🔹 Buat data pinjaman baru
            val newPinjaman = Pinjaman(
                id = dataPinjaman.size + 1,
                kodePegawai = kodePegawai,
                jumlah = nominal,
                tenor = tenor,
                status = "Proses"
            )

            // 🔹 Simpan ke dummy global (pastikan list di DummyUserData pakai mutableListOf)
            DummyUserData.pinjamanList.add(0, newPinjaman)

            // 🔹 Tambahkan ke list lokal dan update adapter
            dataPinjaman.add(0, newPinjaman)
            adapter.notifyItemInserted(0)
            rvPinjaman.scrollToPosition(0)

            // 🔹 Reset input
            inputNominal.setText("")
            inputTenor.setText("")

            Toast.makeText(requireContext(), "Pinjaman diajukan!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadDummyPinjaman() {
        dataPinjaman.clear()
        dataPinjaman.addAll(DummyUserData.pinjamanList)
    }
}
