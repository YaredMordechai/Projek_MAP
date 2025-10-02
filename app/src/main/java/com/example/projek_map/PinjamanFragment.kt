package com.example.projek_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PinjamanFragment : Fragment() {

    private lateinit var rvPinjaman: RecyclerView
    private lateinit var btnAjukan: Button
    private lateinit var adapter: PinjamanAdapter
    private val data = mutableListOf<Pinjaman>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        // pakai id dari XML yang baru
        rvPinjaman = view.findViewById(R.id.rvRiwayatPinjaman)
        btnAjukan = view.findViewById(R.id.btnAjukanPinjaman)

        // Setup RecyclerView
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())
        adapter = PinjamanAdapter(data) { pinjaman ->
            Toast.makeText(
                requireContext(),
                "${pinjaman.jenis} — Rp ${pinjaman.jumlah}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvPinjaman.adapter = adapter

        loadDummyData()

        btnAjukan.setOnClickListener {
            Toast.makeText(requireContext(), "Ajukan Pinjaman ditekan", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadDummyData() {
        data.clear()
        data.add(Pinjaman(1, "Pinjaman Pendidikan", 2000000L, "Berjalan", "2025-08-01"))
        data.add(Pinjaman(2, "Pinjaman Darurat", 1500000L, "Lunas", "2024-12-10"))
        data.add(Pinjaman(3, "Pinjaman Modal Usaha", 5000000L, "Pengajuan", "2025-09-15"))
        adapter.notifyDataSetChanged()
    }
}
