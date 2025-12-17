package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projek_map.R
import com.example.projek_map.ui.viewmodels.LabaRugiViewModel
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class LabaRugiFragment : Fragment() {

    private lateinit var tvPendapatan: TextView
    private lateinit var tvBeban: TextView
    private lateinit var tvHasil: TextView

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    private val viewModel: LabaRugiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_laba_rugi, container, false)

        tvPendapatan = view.findViewById(R.id.tvTotalPendapatan)
        tvBeban = view.findViewById(R.id.tvTotalBeban)
        tvHasil = view.findViewById(R.id.tvLabaRugiBersih)

        setupObservers()

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val bulan = cal.get(Calendar.MONTH) + 1

        // tetap 100% DB via API (struktur kamu)
        viewModel.loadFromDb(year, bulan)

        return view
    }

    private fun setupObservers() {
        viewModel.labaRugiUi.observe(viewLifecycleOwner) { d ->
            if (d == null) return@observe

            tvPendapatan.text = "Total Pendapatan: ${rupiah.format(d.totalPendapatan)}"
            tvBeban.text = "Total Beban: ${rupiah.format(d.totalBeban)}"

            tvHasil.text = if (d.isLaba)
                "Laba Bersih: ${rupiah.format(d.labaRugi)}"
            else
                "Rugi Bersih: ${rupiah.format(d.labaRugi)}"

            tvHasil.setTextColor(
                requireContext().getColor(
                    if (d.isLaba) R.color.green_700 else R.color.red_600
                )
            )
        }

        viewModel.toast.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }
    }
}
