package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.ui.adapters.PengumumanAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PengumumanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_pengumuman, container, false)
        val rv = v.findViewById<RecyclerView>(R.id.rvPengumuman)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = PengumumanAdapter(DummyUserData.pengumumanList) { item ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(item.judul)
                .setMessage("${item.isi}\n\nTanggal: ${item.tanggal}")
                .setPositiveButton("Tutup", null)
                .show()
        }
        return v
    }
}
