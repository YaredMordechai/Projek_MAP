package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import java.text.NumberFormat
import java.util.*

class LaporanFragment : Fragment() {

    private lateinit var txtTotalSimpanan: TextView
    private lateinit var txtTotalPinjaman: TextView
    private lateinit var txtTotalAngsuran: TextView
    private lateinit var spinnerBulan: Spinner

    private val kodePegawaiAktif = "EMP001" // nanti bisa disesuaikan dari login aktif

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        txtTotalSimpanan = view.findViewById(R.id.txtTotalSimpanan)
        txtTotalPinjaman = view.findViewById(R.id.txtTotalPinjaman)
        txtTotalAngsuran = view.findViewById(R.id.txtTotalAngsuran)
        spinnerBulan = view.findViewById(R.id.spinnerBulan)

        setupSpinner()
        updateLaporan(Calendar.getInstance().get(Calendar.MONTH) + 1) // bulan saat ini

        return view
    }

    private fun setupSpinner() {
        val bulanList = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bulanList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBulan.adapter = adapter

        // set ke bulan sekarang
        spinnerBulan.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                updateLaporan(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateLaporan(bulan: Int) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val totalSimpanan = DummyUserData.getTotalSimpanan(kodePegawaiAktif)
        val totalPinjaman = DummyUserData.getTotalPinjamanAktif(kodePegawaiAktif)
        val totalAngsuran = DummyUserData.getTotalAngsuranBulanan(kodePegawaiAktif, bulan)

        txtTotalSimpanan.text = formatter.format(totalSimpanan)
        txtTotalPinjaman.text = formatter.format(totalPinjaman)
        txtTotalAngsuran.text = formatter.format(totalAngsuran)
    }
}
