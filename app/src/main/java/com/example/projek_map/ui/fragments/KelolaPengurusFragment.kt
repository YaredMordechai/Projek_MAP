package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.google.android.material.button.MaterialButton

class KelolaPengurusFragment : Fragment() {

    private lateinit var inputBunga: EditText
    private lateinit var inputDenda: EditText
    private lateinit var btnSimpanBunga: MaterialButton
    private lateinit var btnCatatPembayaran: MaterialButton
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvPinjamanLunas: RecyclerView

    private lateinit var adapterAktif: PinjamanAdapter
    private lateinit var adapterLunas: PinjamanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_pengurus, container, false)

        inputBunga = view.findViewById(R.id.inputBunga)
        inputDenda = view.findViewById(R.id.inputDenda)
        btnSimpanBunga = view.findViewById(R.id.btnSimpanBunga)
        btnCatatPembayaran = view.findViewById(R.id.btnCatatPembayaran)
        rvPinjamanAktif = view.findViewById(R.id.rvPinjamanAktif)
        rvPinjamanLunas = view.findViewById(R.id.rvPinjamanLunas)

        rvPinjamanAktif.layoutManager = LinearLayoutManager(requireContext())
        rvPinjamanLunas.layoutManager = LinearLayoutManager(requireContext())

        adapterAktif = PinjamanAdapter(DummyUserData.getDaftarPinjamanAktif()) { /* click detail */ }
        adapterLunas = PinjamanAdapter(DummyUserData.getDaftarPinjamanLunas()) { /* click detail */ }

        rvPinjamanAktif.adapter = adapterAktif
        rvPinjamanLunas.adapter = adapterLunas

        btnSimpanBunga.setOnClickListener {
            val bunga = inputBunga.text.toString().toDoubleOrNull()
            val denda = inputDenda.text.toString().toDoubleOrNull()
            if (bunga == null || denda == null) {
                toast("Masukkan angka valid.")
                return@setOnClickListener
            }
            DummyUserData.setSukuBungaBaru(bunga)
            DummyUserData.setDendaKeterlambatanBaru(denda)
            toast("Suku bunga & denda berhasil diperbarui.")
        }

        btnCatatPembayaran.setOnClickListener {
            showInputPembayaranDialog()
        }

        return view
    }

    private fun showInputPembayaranDialog() {
        val inputLayout = layoutInflater.inflate(R.layout.dialog_catat_pembayaran, null)
        val edtKodePegawai = inputLayout.findViewById<EditText>(R.id.edtKodePegawai)
        val edtPinjamanId = inputLayout.findViewById<EditText>(R.id.edtPinjamanId)
        val edtJumlah = inputLayout.findViewById<EditText>(R.id.edtJumlahBayar)

        AlertDialog.Builder(requireContext())
            .setTitle("Catat Pembayaran Angsuran")
            .setView(inputLayout)
            .setPositiveButton("Simpan") { _, _ ->
                val kode = edtKodePegawai.text.toString().trim()
                val id = edtPinjamanId.text.toString().toIntOrNull()
                val jumlah = edtJumlah.text.toString().toIntOrNull()
                if (kode.isEmpty() || id == null || jumlah == null) {
                    toast("Semua data harus diisi.")
                    return@setPositiveButton
                }
                DummyUserData.catatPembayaranAngsuranAdmin(kode, id, jumlah)
                toast("Pembayaran berhasil dicatat.")
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
