package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import java.text.NumberFormat
import java.util.Locale

class LabaBersihFragment : Fragment() {

    private lateinit var tvTotalMasuk: TextView
    private lateinit var tvTotalKeluar: TextView
    private lateinit var tvLabaBersih: TextView

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_laba_bersih, container, false)

        tvTotalMasuk = v.findViewById(R.id.tvTotalMasuk)
        tvTotalKeluar = v.findViewById(R.id.tvTotalKeluar)
        tvLabaBersih = v.findViewById(R.id.tvLabaBersih)

        tampilkanData()
        return v
    }

    private fun tampilkanData() {
        val semuaTransaksi = DummyUserData.kasTransaksiList

        val totalMasuk = semuaTransaksi.filter { it.jenis.equals("Masuk", true) }
            .sumOf { it.jumlah }

        val totalKeluar = semuaTransaksi.filter { it.jenis.equals("Keluar", true) }
            .sumOf { it.jumlah }

        val labaBersih = totalMasuk - totalKeluar

        tvTotalMasuk.text = "Total Pemasukan: ${rupiah.format(totalMasuk)}"
        tvTotalKeluar.text = "Total Pengeluaran: ${rupiah.format(totalKeluar)}"
        tvLabaBersih.text = "Laba Bersih: ${rupiah.format(labaBersih)}"

        tvLabaBersih.setTextColor(
            requireContext().getColor(if (labaBersih >= 0) R.color.green_700 else R.color.red_600)
        )
    }
}