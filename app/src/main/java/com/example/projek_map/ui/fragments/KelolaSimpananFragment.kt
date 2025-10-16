package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.TransaksiSimpanan
import com.example.projek_map.ui.adapters.TransaksiSimpananAdapter
import com.google.android.material.button.MaterialButton

class KelolaSimpananFragment : Fragment() {

    private lateinit var rvTransaksi: RecyclerView
    private lateinit var btnTambah: MaterialButton
    private lateinit var adapter: TransaksiSimpananAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_simpanan, container, false)
        rvTransaksi = view.findViewById(R.id.rvTransaksiSimpanan)
        btnTambah = view.findViewById(R.id.btnTambahSimpanan)

        rvTransaksi.layoutManager = LinearLayoutManager(requireContext())
        adapter = TransaksiSimpananAdapter(DummyUserData.transaksiSimpananList)
        rvTransaksi.adapter = adapter

        btnTambah.setOnClickListener { showAddDialog() }

        return view
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_simpanan, null)
        val etKode = dialogView.findViewById<EditText>(R.id.etKodePegawai)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val spinnerJenis = dialogView.findViewById<Spinner>(R.id.spinnerJenis)

        val jenisList = listOf("Pokok", "Wajib", "Sukarela")
        spinnerJenis.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, jenisList)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi Simpanan")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val kode = etKode.text.toString().trim()
                val jumlahText = etJumlah.text.toString().trim()
                val jenis = spinnerJenis.selectedItem.toString()

                if (kode.isEmpty() || jumlahText.isEmpty()) {
                    Toast.makeText(requireContext(), "Isi semua kolom!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val jumlah = jumlahText.toDoubleOrNull()
                if (jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Jumlah tidak valid!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val user = DummyUserData.users.find { it.kodePegawai == kode }
                if (user == null) {
                    Toast.makeText(requireContext(), "Kode pegawai tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                DummyUserData.tambahTransaksiSimpanan(kode, jenis, jumlah)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Transaksi simpanan berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}