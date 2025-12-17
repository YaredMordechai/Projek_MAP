package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.User
import com.example.projek_map.ui.adapters.LaporanAdminAdapter
import java.text.NumberFormat
import java.util.Locale

class LaporanAdminFragment : Fragment() {

    private lateinit var tvSaldoKas: TextView
    private lateinit var tvTotalSimpananAll: TextView
    private lateinit var tvTotalPinjamanAll: TextView
    private lateinit var tvJumlahAnggota: TextView

    private lateinit var spStatus: Spinner
    private lateinit var etCari: EditText
    private lateinit var btnFilter: Button
    private lateinit var btnReset: Button

    private lateinit var rvAnggota: RecyclerView
    private lateinit var adapter: LaporanAdminAdapter
    private lateinit var tvEmpty: TextView

    private val rupiah: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    // ViewModel (MVVM)
    private val vm: LaporanAdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_laporan_admin, container, false)

        // Ringkasan
        tvSaldoKas = v.findViewById(R.id.tvSaldoKas)
        tvTotalSimpananAll = v.findViewById(R.id.tvTotalSimpananAll)
        tvTotalPinjamanAll = v.findViewById(R.id.tvTotalPinjamanAll)
        tvJumlahAnggota = v.findViewById(R.id.tvJumlahAnggota)

        // Filter
        spStatus = v.findViewById(R.id.spFilterStatus)
        etCari = v.findViewById(R.id.etFilterCari)
        btnFilter = v.findViewById(R.id.btnFilter)
        btnReset = v.findViewById(R.id.btnReset)

        // List
        rvAnggota = v.findViewById(R.id.rvAnggota)
        tvEmpty = v.findViewById(R.id.tvEmptyAnggota)
        rvAnggota.layoutManager = LinearLayoutManager(requireContext())

        adapter = LaporanAdminAdapter(mutableListOf()) { item ->
            val msg = """
                Kode: ${item.user.kodePegawai}
                Nama: ${item.user.nama}
                Status: ${item.user.statusKeanggotaan}
                
                Total Simpanan: ${rupiah.format(item.totalSimpanan)}
                Total Pinjaman Aktif: ${rupiah.format(item.totalPinjamanAktif)}
            """.trimIndent()

            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Detail Anggota")
                .setMessage(msg)
                .setPositiveButton("Tutup", null)
                .show()
        }
        rvAnggota.adapter = adapter

        // Spinner status
        val statusItems = listOf("Semua", "Aktif", "Nonaktif")
        spStatus.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusItems)

        // Filter / Reset (tetap sama fiturnya, tapi memanggil ViewModel)
        btnFilter.setOnClickListener {
            vm.setFilter(
                query = etCari.text?.toString()?.trim().orElse(""),
                status = spStatus.selectedItem?.toString().orElse("Semua")
            )
        }

        btnReset.setOnClickListener {
            spStatus.setSelection(0)
            etCari.setText("")
            vm.setFilter(query = "", status = "Semua")
        }

        // Observers (render UI dari ViewModel)
        observeViewModel()

        // Load awal (100% API) -> sekarang lewat ViewModel
        vm.loadAll()

        return v
    }

    private fun observeViewModel() {
        vm.summary.observe(viewLifecycleOwner) { s ->
            if (s == null) {
                tvSaldoKas.text = "-"
                tvTotalSimpananAll.text = "-"
                tvTotalPinjamanAll.text = "-"
                tvJumlahAnggota.text = "-"
            } else {
                tvSaldoKas.text = rupiah.format(s.saldoKas)
                tvTotalSimpananAll.text = rupiah.format(s.totalSimpananAll)
                tvTotalPinjamanAll.text = rupiah.format(s.totalPinjamanAll)
                tvJumlahAnggota.text = s.jumlahAnggota.toString()
            }
        }

        vm.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading == true) {
                showLoadingState()
            }
        }

        vm.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                showErrorState(msg)
            }
        }

        vm.filteredList.observe(viewLifecycleOwner) { list ->
            val safe = list ?: emptyList()
            adapter.replaceAll(safe)

            if (safe.isEmpty()) {
                tvEmpty.text = "Tidak ada anggota sesuai filter."
                tvEmpty.visibility = View.VISIBLE
                rvAnggota.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                rvAnggota.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoadingState() {
        // Ringkasan placeholder
        tvSaldoKas.text = "-"
        tvTotalSimpananAll.text = "-"
        tvTotalPinjamanAll.text = "-"
        tvJumlahAnggota.text = "-"

        tvEmpty.text = "Memuat data."
        tvEmpty.visibility = View.VISIBLE
        rvAnggota.visibility = View.GONE
        adapter.replaceAll(emptyList())
    }

    private fun showErrorState(msg: String) {
        // Ringkasan placeholder
        tvSaldoKas.text = "-"
        tvTotalSimpananAll.text = "-"
        tvTotalPinjamanAll.text = "-"
        tvJumlahAnggota.text = "-"

        tvEmpty.text = msg
        tvEmpty.visibility = View.VISIBLE
        rvAnggota.visibility = View.GONE
        adapter.replaceAll(emptyList())
    }

    private fun String?.orElse(def: String) = this ?: def

    // Tetap dipakai (di ViewModel juga ada normalisasi), aman kalau suatu saat perlu di Fragment
    private fun normalizeStatus(raw: String?): String {
        val s = raw?.trim().orEmpty().lowercase()
        return when {
            s.contains("nonaktif") -> "Nonaktif"
            s.contains("non-aktif") -> "Nonaktif"
            s.contains("tidak aktif") -> "Nonaktif"
            s.contains("inactive") -> "Nonaktif"
            s == "0" -> "Nonaktif"
            s.contains("aktif") -> "Aktif"
            s.contains("active") -> "Aktif"
            s == "1" -> "Aktif"
            else -> if (s.isBlank()) "Nonaktif" else raw!!.trim()
        }
    }

    data class UserSummary(
        val user: User,
        val totalSimpanan: Double,
        val totalPinjamanAktif: Double
    )
}
