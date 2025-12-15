package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.SettingsUpdateRequest
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class   KelolaPengurusFragment : Fragment() {

    private lateinit var inputBunga: EditText
    private lateinit var inputDenda: EditText
    private lateinit var btnSimpanBunga: MaterialButton
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvPinjamanLunas: RecyclerView

    private lateinit var adapterAktif: PinjamanAdapter
    private lateinit var adapterLunas: PinjamanAdapter

    private val listAktif = mutableListOf<Pinjaman>()
    private val listLunas = mutableListOf<Pinjaman>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_pengurus, container, false)

        inputBunga = view.findViewById(R.id.inputBunga)
        inputDenda = view.findViewById(R.id.inputDenda)
        btnSimpanBunga = view.findViewById(R.id.btnSimpanBunga)
        rvPinjamanAktif = view.findViewById(R.id.rvPinjamanAktif)
        rvPinjamanLunas = view.findViewById(R.id.rvPinjamanLunas)

        rvPinjamanAktif.layoutManager = LinearLayoutManager(requireContext())
        rvPinjamanLunas.layoutManager = LinearLayoutManager(requireContext())

        adapterAktif = PinjamanAdapter(listAktif) { /* click detail kalau mau */ }
        adapterLunas = PinjamanAdapter(listLunas) { /* click detail kalau mau */ }

        rvPinjamanAktif.adapter = adapterAktif
        rvPinjamanLunas.adapter = adapterLunas

        btnSimpanBunga.setOnClickListener { simpanPengaturan() }

        // load awal
        refreshSettings()
        refreshPinjaman()

        return view
    }

    override fun onResume() {
        super.onResume()
        refreshPinjaman()
        refreshSettings()
    }

    private fun refreshPinjaman() {
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getAllPinjaman()
                val body = resp.body()

                if (!resp.isSuccessful || body?.success != true) {
                    toast(body?.message ?: "Gagal memuat pinjaman")
                    return@launch
                }

                val all = body.data ?: emptyList()

                listAktif.clear()
                listLunas.clear()

                all.forEach { p ->
                    when (p.status.lowercase()) {
                        "disetujui", "aktif", "proses", "menunggu" -> listAktif.add(p)
                        "lunas", "selesai" -> listLunas.add(p)
                        else -> listAktif.add(p)
                    }
                }

                adapterAktif.notifyDataSetChanged()
                adapterLunas.notifyDataSetChanged()

            } catch (e: Exception) {
                toast("Error: ${e.message}")
            }
        }
    }

    private fun refreshSettings() {
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getSettings()
                val body = resp.body()

                if (!resp.isSuccessful || body?.success != true) {
                    // gak usah toast terus2an kalau endpoint belum ada
                    return@launch
                }

                val s = body.data
                if (s != null) {
                    inputBunga.setText(s.bungaPersen.toString())
                    inputDenda.setText(s.dendaPersenPerHari.toString())
                }

            } catch (_: Exception) {
                // silent biar gak spam
            }
        }
    }

    private fun simpanPengaturan() {
        val bunga = inputBunga.text.toString().toDoubleOrNull()
        val denda = inputDenda.text.toString().toDoubleOrNull()

        if (bunga == null || denda == null) {
            toast("Masukkan angka valid.")
            return
        }

        lifecycleScope.launch {
            try {
                btnSimpanBunga.isEnabled = false

                val resp = ApiClient.apiService.updateSettings(
                    SettingsUpdateRequest(
                        bungaPersen = bunga,
                        dendaPersenPerHari = denda
                    )
                )
                val body = resp.body()

                btnSimpanBunga.isEnabled = true

                if (!resp.isSuccessful || body?.success != true) {
                    toast(body?.message ?: "Gagal simpan pengaturan")
                    return@launch
                }

                toast("Suku bunga & denda berhasil diperbarui.")

            } catch (e: Exception) {
                btnSimpanBunga.isEnabled = true
                toast("Error: ${e.message}")
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
