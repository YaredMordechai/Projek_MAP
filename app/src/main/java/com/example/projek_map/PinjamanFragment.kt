package com.example.projek_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PinjamanFragment : Fragment() {

    private lateinit var rvPinjaman: RecyclerView
    private lateinit var fabAjukan: FloatingActionButton
    private lateinit var adapter: PinjamanAdapter
    private val data = mutableListOf<Pinjaman>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        rvPinjaman = view.findViewById(R.id.rvPinjaman)
        fabAjukan = view.findViewById(R.id.fabAjukanPinjaman)

        // Setup RecyclerView
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())
        adapter = PinjamanAdapter(data) { pinjaman ->
            // contoh click: tampilkan toast
            Toast.makeText(
                requireContext(),
                "${pinjaman.jenis} — Rp ${pinjaman.jumlah}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvPinjaman.adapter = adapter

        loadDummyData()

        fabAjukan.setOnClickListener {
            // TODO: ganti dengan aksi nyata (Activity / Dialog pengajuan)
            Toast.makeText(requireContext(), "Fungsionalitas ajukan pinjaman belum diimplementasikan", Toast.LENGTH_SHORT).show()
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
