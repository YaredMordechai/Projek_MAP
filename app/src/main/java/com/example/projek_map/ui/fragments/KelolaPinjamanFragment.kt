package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.ui.adapters.KelolaPinjamanAdapter

class KelolaPinjamanFragment : Fragment() {

    private lateinit var rvPinjaman: RecyclerView
    private lateinit var adapter: KelolaPinjamanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_pinjaman, container, false)

        rvPinjaman = view.findViewById(R.id.rvKelolaPinjaman)
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())

        adapter = KelolaPinjamanAdapter(
            DummyUserData.pinjamanList.toMutableList(),
            onApprove = { pinjaman -> showApprovalDialog(pinjaman, true) },
            onReject = { pinjaman -> showApprovalDialog(pinjaman, false) }
        )

        rvPinjaman.adapter = adapter
        return view
    }

    private fun showApprovalDialog(pinjaman: Pinjaman, approve: Boolean) {
        val actionText = if (approve) "Setujui" else "Tolak"

        AlertDialog.Builder(requireContext())
            .setTitle("$actionText Pinjaman")
            .setMessage("Apakah kamu yakin ingin $actionText pinjaman dari ${pinjaman.kodePegawai}?")
            .setPositiveButton(actionText) { _, _ ->
                if (approve) DummyUserData.approvePinjaman(pinjaman.id)
                else DummyUserData.rejectPinjaman(pinjaman.id)

                adapter.notifyDataSetChanged()
                Toast.makeText(
                    requireContext(),
                    "Pinjaman ${pinjaman.kodePegawai} telah ${if (approve) "disetujui" else "ditolak"}.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}