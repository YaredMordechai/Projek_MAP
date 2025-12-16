package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

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

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val bulan = cal.get(Calendar.MONTH) + 1

        // ✅ 100% DB
        loadFromDb(year, bulan)

        return view
    }

    private fun loadFromDb(year: Int, bulan: Int) {
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getLabaRugi(year = year, bulan = bulan)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data

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
                } else {
                    toast("Gagal load laba rugi dari server")
                }
            } catch (e: Exception) {
                toast("Error: ${e.message}")
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
