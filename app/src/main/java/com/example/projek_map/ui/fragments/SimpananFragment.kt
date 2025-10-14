package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.ui.adapters.HistoriSimpananAdapter
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SimpananFragment : Fragment() {

    private lateinit var tvTotalSaldo: TextView
    private lateinit var rvHistori: RecyclerView
    private lateinit var btnSetor: MaterialButton
    private lateinit var btnTarik: MaterialButton
    private lateinit var inputJumlah: TextInputEditText
    private lateinit var inputKeterangan: TextInputEditText
    private lateinit var prefManager: PrefManager
    private lateinit var adapterHistori: HistoriSimpananAdapter
    private val dataHistori = mutableListOf<com.example.projek_map.data.HistoriSimpanan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_simpanan, container, false)

        prefManager = PrefManager(requireContext())

        tvTotalSaldo = view.findViewById(R.id.tvTotalSaldoSimpanan)
        rvHistori = view.findViewById(R.id.rvHistoriSimpanan)
        btnSetor = view.findViewById(R.id.btnSetor)
        btnTarik = view.findViewById(R.id.btnTarik)
        inputJumlah = view.findViewById(R.id.inputJumlahSimpanan)
        inputKeterangan = view.findViewById(R.id.inputKeteranganSimpanan)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        adapterHistori = HistoriSimpananAdapter(dataHistori)
        rvHistori.adapter = adapterHistori

        loadHistoriSimpanan()

        btnSetor.setOnClickListener {
            val jumlah = inputJumlah.text?.toString()?.toDoubleOrNull()
            val ket = inputKeterangan.text?.toString()?.trim().orEmpty()
            val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"

            if (jumlah == null || jumlah <= 0) {
                Toast.makeText(requireContext(), "Masukkan jumlah valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DummyUserData.tambahSimpanan(kodePegawai, "Setoran Sukarela", jumlah, ket)
            loadHistoriSimpanan()
            Toast.makeText(requireContext(), "Setoran berhasil!", Toast.LENGTH_SHORT).show()
        }

        btnTarik.setOnClickListener {
            val jumlah = inputJumlah.text?.toString()?.toDoubleOrNull()
            val ket = inputKeterangan.text?.toString()?.trim().orEmpty()
            val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"

            if (jumlah == null || jumlah <= 0) {
                Toast.makeText(requireContext(), "Masukkan jumlah valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DummyUserData.tambahSimpanan(kodePegawai, "Penarikan Sukarela", -jumlah, ket)
            loadHistoriSimpanan()
            Toast.makeText(requireContext(), "Penarikan berhasil!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadHistoriSimpanan() {
        val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"
        dataHistori.clear()
        dataHistori.addAll(DummyUserData.getHistoriSimpanan(kodePegawai))
        adapterHistori.notifyDataSetChanged()

        val total = DummyUserData.getTotalSimpanan(kodePegawai)
        tvTotalSaldo.text = "Total Saldo Simpanan: Rp ${String.format("%,.0f", total).replace(',', '.')}"
    }
}
