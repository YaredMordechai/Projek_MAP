package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton

class SimpananFragment : Fragment() {

    private lateinit var tvPokok: TextView
    private lateinit var tvWajib: TextView
    private lateinit var tvSukarela: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnBack: MaterialButton
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simpanan, container, false)

        prefManager = PrefManager(requireContext())

        tvPokok = view.findViewById(R.id.tvSimpananPokok)
        tvWajib = view.findViewById(R.id.tvSimpananWajib)
        tvSukarela = view.findViewById(R.id.tvSimpananSukarela)
        tvTotal = view.findViewById(R.id.tvTotalSimpanan)
        btnBack = view.findViewById(R.id.btnBackDashboard)

        loadDataSimpanan()

        btnBack.setOnClickListener {
            Toast.makeText(requireContext(), "Kembali ke Dashboard", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    private fun loadDataSimpanan() {
        val kodePegawai = prefManager.getKodePegawai() ?: ""
        val simpanan = DummyUserData.simpananList.firstOrNull { it.kodePegawai == kodePegawai }

        if (simpanan != null) {
            val total = simpanan.simpananPokok + simpanan.simpananWajib + simpanan.simpananSukarela
            tvPokok.text = formatRupiah(simpanan.simpananPokok)
            tvWajib.text = formatRupiah(simpanan.simpananWajib)
            tvSukarela.text = formatRupiah(simpanan.simpananSukarela)
            tvTotal.text = formatRupiah(total)
        } else {
            tvPokok.text = "-"
            tvWajib.text = "-"
            tvSukarela.text = "-"
            tvTotal.text = "-"
        }
    }

    private fun formatRupiah(value: Double): String {
        return "Rp " + String.format("%,.0f", value).replace(',', '.')
    }
}
