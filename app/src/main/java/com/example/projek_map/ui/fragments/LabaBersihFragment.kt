package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projek_map.R
import com.example.projek_map.ui.viewmodels.LabaBersihViewModel
import java.text.NumberFormat
import java.util.Locale

class LabaBersihFragment : Fragment() {

    private lateinit var tvTotalMasuk: TextView
    private lateinit var tvTotalKeluar: TextView
    private lateinit var tvLabaBersih: TextView

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    private val viewModel: LabaBersihViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_laba_bersih, container, false)

        tvTotalMasuk = v.findViewById(R.id.tvTotalMasuk)
        tvTotalKeluar = v.findViewById(R.id.tvTotalKeluar)
        tvLabaBersih = v.findViewById(R.id.tvLabaBersih)

        setupObservers()

        // tetap load di onCreateView (struktur kamu)
        viewModel.loadLabaBersih()

        return v
    }

    private fun setupObservers() {
        viewModel.labaBersihUi.observe(viewLifecycleOwner) { d ->
            if (d == null) {
                renderZero()
                return@observe
            }

            tvTotalMasuk.text = "Total Pemasukan: ${rupiah.format(d.totalMasuk)}"
            tvTotalKeluar.text = "Total Pengeluaran: ${rupiah.format(d.totalKeluar)}"
            tvLabaBersih.text = "Laba Bersih: ${rupiah.format(d.labaBersih)}"

            tvLabaBersih.setTextColor(
                requireContext().getColor(
                    if (d.labaBersih >= 0) R.color.green_700 else R.color.red_600
                )
            )
        }
    }

    private fun renderZero() {
        tvTotalMasuk.text = "Total Pemasukan: ${rupiah.format(0)}"
        tvTotalKeluar.text = "Total Pengeluaran: ${rupiah.format(0)}"
        tvLabaBersih.text = "Laba Bersih: ${rupiah.format(0)}"
        tvLabaBersih.setTextColor(requireContext().getColor(R.color.green_700))
    }
}
