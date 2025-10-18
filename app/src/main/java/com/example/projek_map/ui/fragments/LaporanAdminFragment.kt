package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.User
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
            // Klik item → tampilkan detail sederhana (dummy) dalam dialog
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
        spStatus.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusItems)

        // Tombol filter
        btnFilter.setOnClickListener { applyFilter() }
        btnReset.setOnClickListener {
            spStatus.setSelection(0)
            etCari.setText("")
            applyFilter()
        }

        // Muat awal
        renderSummary()
        applyFilter()

        return v
    }

    private fun renderSummary() {
        // Saldo kas dari DummyUserData
        val saldoKas = DummyUserData.getSaldoKas()

        // Total simpanan seluruh anggota
        // (pakai simpananList yang sudah menyimpan saldo per anggota)
        val totalSimpananAll = DummyUserData.simpananList.sumOf {
            it.simpananPokok + it.simpananWajib + it.simpananSukarela
        }

        // Total pinjaman aktif seluruh anggota (status != "Lunas")
        val totalPinjamanAll = DummyUserData.pinjamanList
            .filter { !it.status.equals("Lunas", true) }
            .sumOf { it.jumlah.toDouble() }

        val jumlahAnggota = DummyUserData.users.size

        tvSaldoKas.text = rupiah.format(saldoKas)
        tvTotalSimpananAll.text = rupiah.format(totalSimpananAll)
        tvTotalPinjamanAll.text = rupiah.format(totalPinjamanAll)
        tvJumlahAnggota.text = jumlahAnggota.toString()
    }

    private fun applyFilter() {
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
                val join = "${user.nama} ${user.kodePegawai} ${user.email}".lowercase()
                join.contains(q)
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

    private fun String?.orElse(def: String) = this ?: def

    data class UserSummary(
        val user: User,
        val totalSimpanan: Double,
        val totalPinjamanAktif: Double
    )
}
