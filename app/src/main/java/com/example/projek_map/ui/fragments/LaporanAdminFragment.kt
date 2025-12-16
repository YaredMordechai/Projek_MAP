package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.data.User
import com.example.projek_map.ui.adapters.LaporanAdminAdapter
import kotlinx.coroutines.launch
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

    // cache data dari API (supaya filter tidak hit API terus)
    private val anggotaCache = mutableListOf<UserSummary>()

    private var isLoading = false

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

        btnFilter.setOnClickListener { applyFilterFromCache() }
        btnReset.setOnClickListener {
            spStatus.setSelection(0)
            etCari.setText("")
            applyFilterFromCache()
        }

        // Load awal (100% API)
        loadAllFromApi()

        return v
    }

    private fun loadAllFromApi() {
        if (isLoading) return
        isLoading = true

        showLoadingState()

        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getLaporanAdmin()
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data

                    // render summary
                    tvSaldoKas.text = rupiah.format(d.saldoKas)
                    tvTotalSimpananAll.text = rupiah.format(d.totalSimpananAll)
                    tvTotalPinjamanAll.text = rupiah.format(d.totalPinjamanAll)
                    tvJumlahAnggota.text = d.jumlahAnggota.toString()

                    // cache anggota
                    anggotaCache.clear()
                    anggotaCache.addAll(
                        d.users.map { r ->
                            UserSummary(
                                user = User(
                                    kodePegawai = r.kodePegawai,
                                    email = r.email,
                                    password = "", // server sengaja kosong
                                    nama = r.nama,
                                    statusKeanggotaan = normalizeStatus(r.statusKeanggotaan)
                                ),
                                totalSimpanan = r.totalSimpanan,
                                totalPinjamanAktif = r.totalPinjamanAktif
                            )
                        }
                    )

                    // apply filter dari cache
                    applyFilterFromCache()

                    isLoading = false
                } else {
                    isLoading = false
                    showErrorState(
                        msg = body?.message?.takeIf { it.isNotBlank() }
                            ?: "Gagal memuat data laporan admin (response tidak valid)."
                    )
                }
            } catch (e: Exception) {
                isLoading = false
                showErrorState(msg = "Gagal terhubung ke server: ${e.message ?: "Unknown error"}")
            }
        }
    }

    private fun applyFilterFromCache() {
        val q = etCari.text?.toString()?.trim()?.lowercase().orElse("")
        val status = spStatus.selectedItem?.toString().orElse("Semua")

        val list = anggotaCache
            .filter { row ->
                val user = row.user
                val okStatus = when (status) {
                    "Aktif" -> user.statusKeanggotaan.equals("Aktif", true)
                    "Nonaktif" -> user.statusKeanggotaan.equals("Nonaktif", true)
                    else -> true
                }
                if (!okStatus) return@filter false
                if (q.isBlank()) return@filter true
                "${user.nama} ${user.kodePegawai} ${user.email}".lowercase().contains(q)
            }
            .sortedBy { it.user.nama.lowercase() }

        adapter.replaceAll(list)

        if (list.isEmpty()) {
            tvEmpty.text = "Tidak ada anggota sesuai filter."
            tvEmpty.visibility = View.VISIBLE
            rvAnggota.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvAnggota.visibility = View.VISIBLE
        }
    }

    private fun showLoadingState() {
        // Ringkasan bisa dikosongin / placeholder
        tvSaldoKas.text = "-"
        tvTotalSimpananAll.text = "-"
        tvTotalPinjamanAll.text = "-"
        tvJumlahAnggota.text = "-"

        tvEmpty.text = "Memuat data..."
        tvEmpty.visibility = View.VISIBLE
        rvAnggota.visibility = View.GONE
        adapter.replaceAll(emptyList())
    }

    private fun showErrorState(msg: String) {
        // Ringkasan tetap placeholder
        tvSaldoKas.text = "-"
        tvTotalSimpananAll.text = "-"
        tvTotalPinjamanAll.text = "-"
        tvJumlahAnggota.text = "-"

        tvEmpty.text = msg
        tvEmpty.visibility = View.VISIBLE
        rvAnggota.visibility = View.GONE
        adapter.replaceAll(emptyList())
        anggotaCache.clear()
    }

    private fun String?.orElse(def: String) = this ?: def

    // Normalisasi semua variasi jadi cuma: "Aktif" / "Nonaktif"
    private fun normalizeStatus(raw: String?): String {
        val s = raw?.trim().orEmpty().lowercase()

        return when {
            // nonaktif variants
            s.contains("nonaktif") -> "Nonaktif"
            s.contains("non-aktif") -> "Nonaktif"
            s.contains("tidak aktif") -> "Nonaktif"
            s.contains("inactive") -> "Nonaktif"
            s == "0" -> "Nonaktif"

            // aktif variants (pastikan tidak ketabrak "tidak aktif")
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
