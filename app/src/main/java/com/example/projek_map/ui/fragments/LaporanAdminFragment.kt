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
import com.example.projek_map.data.DummyUserData
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

    // cache data dari DB (supaya filter tidak hit API terus)
    private val anggotaDbCache = mutableListOf<UserSummary>()

    // cache response admin (biar loadSummary + loadAnggota bisa share)
    private var adminLoaded = false
    private var loadingAdmin = false

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
        val statusItems = listOf("Semua", "Anggota Aktif", "Anggota Tidak Aktif")
        spStatus.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusItems)

        btnFilter.setOnClickListener { applyFilter() }
        btnReset.setOnClickListener {
            spStatus.setSelection(0)
            etCari.setText("")
            applyFilter()
        }

        // ✅ LOAD DARI DATABASE
        loadSummaryFromApi()
        loadAnggotaFromApi()

        return v
    }

    // =======================
    // RINGKASAN (DB → fallback dummy)
    // =======================
    private fun loadSummaryFromApi() {
        ensureAdminLoaded(
            onSuccess = {
                // sudah di-set di ensureAdminLoaded
            },
            onFail = {
                renderSummaryDummy()
            }
        )
    }

    // =======================
    // LIST ANGGOTA (DB → fallback dummy)
    // =======================
    private fun loadAnggotaFromApi() {
        ensureAdminLoaded(
            onSuccess = {
                applyFilterDb()
            },
            onFail = {
                applyFilterDummy()
            }
        )
    }

    // ✅ load sekali dari endpoint laporan_admin.php
    private fun ensureAdminLoaded(onSuccess: () -> Unit, onFail: () -> Unit) {
        if (adminLoaded && anggotaDbCache.isNotEmpty()) {
            onSuccess()
            return
        }
        if (loadingAdmin) {
            // kalau sedang loading, kita biarkan user bisa klik filter → pakai dummy dulu
            // (agar nggak “ngerusak” UI)
            onFail()
            return
        }

        loadingAdmin = true
        lifecycleScope.launch {
            try {
                // ⚠️ Pastikan KoperasiApiService punya getLaporanAdmin()
                val resp = ApiClient.apiService.getLaporanAdmin()
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data

                    // set summary
                    tvSaldoKas.text = rupiah.format(d.saldoKas)
                    tvTotalSimpananAll.text = rupiah.format(d.totalSimpananAll)
                    tvTotalPinjamanAll.text = rupiah.format(d.totalPinjamanAll)
                    tvJumlahAnggota.text = d.jumlahAnggota.toString()

                    // map list user rows -> UserSummary
                    anggotaDbCache.clear()
                    anggotaDbCache.addAll(
                        d.users.map { r ->
                            UserSummary(
                                user = User(
                                    kodePegawai = r.kodePegawai,
                                    email = r.email,
                                    password = r.password,
                                    nama = r.nama,
                                    statusKeanggotaan = r.statusKeanggotaan
                                ),
                                totalSimpanan = r.totalSimpanan,
                                totalPinjamanAktif = r.totalPinjamanAktif
                            )
                        }
                    )

                    adminLoaded = true
                    loadingAdmin = false
                    onSuccess()
                } else {
                    loadingAdmin = false
                    onFail()
                }
            } catch (_: Exception) {
                loadingAdmin = false
                onFail()
            }
        }
    }

    // 🔁 fallback lama (tidak dihapus)
    private fun renderSummaryDummy() {
        val saldoKas = DummyUserData.getSaldoKas()
        val totalSimpananAll = DummyUserData.simpananList.sumOf {
            it.simpananPokok + it.simpananWajib + it.simpananSukarela
        }
        val totalPinjamanAll = DummyUserData.pinjamanList
            .filter { !it.status.equals("Lunas", true) }
            .sumOf { it.jumlah.toDouble() }

        val jumlahAnggota = DummyUserData.users.size

        tvSaldoKas.text = rupiah.format(saldoKas)
        tvTotalSimpananAll.text = rupiah.format(totalSimpananAll)
        tvTotalPinjamanAll.text = rupiah.format(totalPinjamanAll)
        tvJumlahAnggota.text = jumlahAnggota.toString()
    }

    // =======================
    // FILTER DARI DB
    // =======================
    private fun applyFilterDb() {
        val q = etCari.text?.toString()?.trim()?.lowercase().orElse("")
        val status = spStatus.selectedItem?.toString().orElse("Semua")

        val list = anggotaDbCache
            .filter { row ->
                val user = row.user
                val okStatus = when (status) {
                    "Anggota Aktif" -> user.statusKeanggotaan.equals("Anggota Aktif", true)
                    "Anggota Tidak Aktif" -> user.statusKeanggotaan.equals("Anggota Tidak Aktif", true)
                    else -> true
                }
                if (!okStatus) return@filter false
                if (q.isBlank()) return@filter true
                "${user.nama} ${user.kodePegawai} ${user.email}".lowercase().contains(q)
            }
            .sortedBy { it.user.nama.lowercase() }

        adapter.replaceAll(list)
        tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        rvAnggota.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
    }

    // =======================
    // FILTER DUMMY (fallback lama)
    // =======================
    private fun applyFilterDummy() {
        val q = etCari.text?.toString()?.trim()?.lowercase().orElse("")
        val status = spStatus.selectedItem?.toString().orElse("Semua")

        val list = DummyUserData.users
            .asSequence()
            .filter { user ->
                val okStatus = when (status) {
                    "Anggota Aktif" -> user.statusKeanggotaan.equals("Anggota Aktif", true)
                    "Anggota Tidak Aktif" -> user.statusKeanggotaan.equals("Anggota Tidak Aktif", true)
                    else -> true
                }
                if (!okStatus) return@filter false
                if (q.isBlank()) return@filter true
                "${user.nama} ${user.kodePegawai} ${user.email}".lowercase().contains(q)
            }
            .map { user ->
                UserSummary(
                    user = user,
                    totalSimpanan = DummyUserData.getTotalSimpanan(user.kodePegawai),
                    totalPinjamanAktif = DummyUserData.getTotalPinjamanAktif(user.kodePegawai)
                )
            }
            .sortedBy { it.user.nama.lowercase() }
            .toList()

        adapter.replaceAll(list)
        tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        rvAnggota.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun applyFilter() {
        if (anggotaDbCache.isNotEmpty()) applyFilterDb() else applyFilterDummy()
    }

    private fun String?.orElse(def: String) = this ?: def

    // ===== MODEL (TIDAK DIUBAH) =====
    data class UserSummary(
        val user: User,
        val totalSimpanan: Double,
        val totalPinjamanAktif: Double
    )
}
