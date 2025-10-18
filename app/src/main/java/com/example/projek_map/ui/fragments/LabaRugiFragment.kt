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
import java.util.*

class LabaRugiFragment : Fragment() {

    private lateinit var tvPendapatan: TextView
    private lateinit var tvBeban: TextView
    private lateinit var tvHasil: TextView

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_laba_rugi, container, false)

        tvPendapatan = view.findViewById(R.id.tvTotalPendapatan)
        tvBeban = view.findViewById(R.id.tvTotalBeban)
        tvHasil = view.findViewById(R.id.tvLabaRugiBersih)

        tampilkanData()
        return view
    }

    private fun tampilkanData() {
        val transaksi = DummyUserData.kasTransaksiList

        // 🔹 Pendapatan = semua kas masuk
        val totalPendapatan = transaksi.filter { it.jenis.equals("Masuk", true) }.sumOf { it.jumlah }

        // 🔹 Beban = semua kas keluar
        val totalBeban = transaksi.filter { it.jenis.equals("Keluar", true) }.sumOf { it.jumlah }

        // 🔹 Hasil akhir
        val labaRugi = totalPendapatan - totalBeban

        tvPendapatan.text = "Total Pendapatan: ${rupiah.format(totalPendapatan)}"
        tvBeban.text = "Total Beban: ${rupiah.format(totalBeban)}"
        tvHasil.text = if (labaRugi >= 0)
            "Laba Bersih: ${rupiah.format(labaRugi)}"
        else
            "Rugi Bersih: ${rupiah.format(labaRugi)}"

        tvHasil.setTextColor(
            requireContext().getColor(if (labaRugi >= 0) R.color.green_700 else R.color.red_600)
        )
    }
}