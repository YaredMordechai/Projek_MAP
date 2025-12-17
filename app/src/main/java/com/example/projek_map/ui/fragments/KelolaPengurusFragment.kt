package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.Pinjaman
import com.example.projek_map.ui.adapters.PinjamanAdapter
import com.example.projek_map.ui.viewmodels.KelolaPengurusViewModel
import com.google.android.material.button.MaterialButton

class KelolaPengurusFragment : Fragment() {

    private lateinit var inputBunga: EditText
    private lateinit var inputDenda: EditText
    private lateinit var btnSimpanBunga: MaterialButton
    private lateinit var rvPinjamanAktif: RecyclerView
    private lateinit var rvPinjamanLunas: RecyclerView

    private lateinit var adapterAktif: PinjamanAdapter
    private lateinit var adapterLunas: PinjamanAdapter

    private val listAktif = mutableListOf<Pinjaman>()
    private val listLunas = mutableListOf<Pinjaman>()

    private val viewModel: KelolaPengurusViewModel by viewModels()

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

        setupObservers()

        // load awal (struktur tetap)
        refreshSettings()
        refreshPinjaman()

        return view
    }

    override fun onResume() {
        super.onResume()
        refreshPinjaman()
        refreshSettings()
    }

    private fun setupObservers() {
        viewModel.pinjamanAktif.observe(viewLifecycleOwner) { data ->
            listAktif.clear()
            listAktif.addAll(data ?: emptyList())
            adapterAktif.notifyDataSetChanged()
        }

        viewModel.pinjamanLunas.observe(viewLifecycleOwner) { data ->
            listLunas.clear()
            listLunas.addAll(data ?: emptyList())
            adapterLunas.notifyDataSetChanged()
        }

        viewModel.settingsPair.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                inputBunga.setText(pair.first.toString())
                inputDenda.setText(pair.second.toString())
            }
        }

        viewModel.isSaving.observe(viewLifecycleOwner) { saving ->
            btnSimpanBunga.isEnabled = !saving
        }

        viewModel.toast.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) toast(msg)
        }
    }

    private fun refreshPinjaman() {
        viewModel.refreshPinjaman()
    }

    private fun refreshSettings() {
        viewModel.refreshSettings()
    }

    private fun simpanPengaturan() {
        val bunga = inputBunga.text.toString().toDoubleOrNull()
        val denda = inputDenda.text.toString().toDoubleOrNull()

        if (bunga == null || denda == null) {
            toast("Masukkan angka valid.")
            return
        }

        viewModel.simpanPengaturan(bunga = bunga, denda = denda)
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
