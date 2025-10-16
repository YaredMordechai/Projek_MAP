package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.ui.adapters.AngsuranAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.Locale

class PinjamanDetailFragment : Fragment() {

    private lateinit var tvCicilan: TextView
    private lateinit var tvTotalPokok: TextView
    private lateinit var tvTotalBunga: TextView
    private lateinit var tvTotalBayar: TextView
    private lateinit var tvTerbayar: TextView
    private lateinit var tvSisaBayar: TextView
    private lateinit var tvSisaPokok: TextView
    private lateinit var toggleMetode: MaterialButtonToggleGroup
    private lateinit var rvJadwal: RecyclerView
    private lateinit var adapter: AngsuranAdapter

    // untuk demo: ambil dari argument atau default 1
    private val pinjamanId: Int by lazy {
        arguments?.getInt(ARG_PINJAMAN_ID) ?: 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_pinjaman_detail, container, false)

        tvCicilan = v.findViewById(R.id.tvCicilan)
        tvTotalPokok = v.findViewById(R.id.tvTotalPokok)
        tvTotalBunga = v.findViewById(R.id.tvTotalBunga)
        tvTotalBayar = v.findViewById(R.id.tvTotalBayar)
        tvTerbayar = v.findViewById(R.id.tvTerbayar)
        tvSisaBayar = v.findViewById(R.id.tvSisaBayar)
        tvSisaPokok = v.findViewById(R.id.tvSisaPokok)
        toggleMetode = v.findViewById(R.id.toggleMetode)
        rvJadwal = v.findViewById(R.id.rvJadwal)

        adapter = AngsuranAdapter()
        rvJadwal.layoutManager = LinearLayoutManager(requireContext())
        rvJadwal.adapter = adapter
        rvJadwal.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // default: ANUITAS
        v.findViewById<View>(R.id.btnAnuitas)?.let { toggleMetode.check(it.id) }
        renderAnuitas()

        toggleMetode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            when (checkedId) {
                R.id.btnAnuitas -> renderAnuitas()
                R.id.btnFlat -> renderFlat()
            }
        }

        return v
    }

    private fun renderAnuitas() {
        val rincian = DummyUserData.getRincianPinjamanAnuitas(pinjamanId)
        tvCicilan.text = rupiah(rincian.cicilanPerBulan)
        tvTotalPokok.text = rupiah(rincian.totalPokok)
        tvTotalBunga.text = rupiah(rincian.totalBunga)
        tvTotalBayar.text = rupiah(rincian.totalBayar)
        tvTerbayar.text = rupiah(rincian.terbayar)
        tvSisaBayar.text = rupiah(rincian.sisaBayar)
        tvSisaPokok.text = rupiah(rincian.sisaPokok)

        val jadwal = DummyUserData.generateJadwalAnuitas(pinjamanId)
        adapter.setData(jadwal)
    }

    private fun renderFlat() {
        val rincian = DummyUserData.getRincianPinjamanFlat(pinjamanId)
        tvCicilan.text = rupiah(rincian.cicilanPerBulan)
        tvTotalPokok.text = rupiah(rincian.totalPokok)
        tvTotalBunga.text = rupiah(rincian.totalBunga)
        tvTotalBayar.text = rupiah(rincian.totalBayar)
        tvTerbayar.text = rupiah(rincian.terbayar)
        tvSisaBayar.text = rupiah(rincian.sisaBayar)
        tvSisaPokok.text = rupiah(rincian.sisaPokok)

        val jadwal = DummyUserData.generateJadwalFlat(pinjamanId)
        adapter.setData(jadwal)
    }

    private fun rupiah(n: Int): String {
        return String.format(Locale("id","ID"), "Rp %,d", n).replace(',', '.')
    }

    companion object {
        private const val ARG_PINJAMAN_ID = "pinjaman_id"
        fun newInstance(pinjamanId: Int) = PinjamanDetailFragment().apply {
            arguments = Bundle().apply { putInt(ARG_PINJAMAN_ID, pinjamanId) }
        }
    }
}
