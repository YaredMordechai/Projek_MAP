package com.example.projek_map.ui.fragments

import android.app.AlertDialog
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
import com.example.projek_map.api.SimpananTransaksiRequest
import com.example.projek_map.data.HistoriSimpanan
import com.example.projek_map.data.Simpanan
import com.example.projek_map.data.TransaksiSimpanan
import com.example.projek_map.ui.adapters.TransaksiSimpananAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class KelolaSimpananFragment : Fragment() {

    private lateinit var rvTransaksi: RecyclerView
    private lateinit var btnTambah: MaterialButton
    private lateinit var adapter: TransaksiSimpananAdapter

    private lateinit var spinnerFilterJenis: Spinner
    private lateinit var tvTotalPokok: TextView
    private lateinit var tvTotalWajib: TextView
    private lateinit var tvTotalSukarela: TextView
    private lateinit var tvEmptyState: TextView

    private val displayList = mutableListOf<TransaksiSimpanan>()
    private val serverSourceList = mutableListOf<TransaksiSimpanan>() // ✅ LIST BAWAH: dari histori_simpanan

    // ✅ untuk isi dropdown pegawai di dialog admin (diambil dari tabel simpanan)
    private val kodePegawaiOptions = mutableListOf<String>()

    private val rupiah by lazy {
        NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    }

    private val api by lazy { ApiClient.apiService }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_simpanan, container, false)

        rvTransaksi = view.findViewById(R.id.rvTransaksiSimpanan)
        btnTambah = view.findViewById(R.id.btnTambahSimpanan)

        spinnerFilterJenis = view.findViewById(R.id.spinnerFilterJenis)
        tvTotalPokok = view.findViewById(R.id.tvTotalPokok)
        tvTotalWajib = view.findViewById(R.id.tvTotalWajib)
        tvTotalSukarela = view.findViewById(R.id.tvTotalSukarela)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        rvTransaksi.layoutManager = LinearLayoutManager(requireContext())

        adapter = TransaksiSimpananAdapter(
            displayList,
            onEdit = { _, _ ->
                Toast.makeText(requireContext(), "Edit belum tersedia", Toast.LENGTH_SHORT).show()
            },
            onDelete = { _, _ ->
                Toast.makeText(requireContext(), "Hapus belum tersedia", Toast.LENGTH_SHORT).show()
            }
        )
        rvTransaksi.adapter = adapter

        val jenisFilter = listOf("Semua", "Pokok", "Wajib", "Sukarela")
        spinnerFilterJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            jenisFilter
        )
        spinnerFilterJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                applyFilterAndRefresh()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnTambah.setOnClickListener { showAddDialogAdmin() }

        // ✅ ADMIN:
        // 1) Header total + dropdown pegawai dari tabel simpanan (ALL)
        loadAllSimpananFromServer()

        // 2) List bawah dari histori_simpanan (ALL)
        loadAllHistoriSimpananForList()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadAllSimpananFromServer()
        loadAllHistoriSimpananForList()
    }

    // =========================
    // ✅ ADMIN: LOAD ALL SIMPANAN (TOTAL HEADER + DROPDOWN PEGAWAI)
    // =========================
    private fun loadAllSimpananFromServer() {
        lifecycleScope.launch {
            try {
                val resp = api.getAllSimpanan()
                if (!resp.isSuccessful) {
                    Toast.makeText(requireContext(), "HTTP ${resp.code()}", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val body = resp.body()
                if (body?.success != true) {
                    Toast.makeText(requireContext(), body?.message ?: "Gagal load simpanan", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val list: List<Simpanan> = body.data.orEmpty()

                // ✅ isi dropdown pegawai dari tabel simpanan
                kodePegawaiOptions.clear()
                kodePegawaiOptions.addAll(list.map { it.kodePegawai }.distinct().sorted())

                // ✅ HEADER TOTAL = SUM tabel simpanan (bukan histori)
                val totalPokokAll = list.sumOf { it.simpananPokok }
                val totalWajibAll = list.sumOf { it.simpananWajib }
                val totalSukarelaAll = list.sumOf { it.simpananSukarela }

                tvTotalPokok.text = rupiah.format(totalPokokAll)
                tvTotalWajib.text = rupiah.format(totalWajibAll)
                tvTotalSukarela.text = rupiah.format(totalSukarelaAll)

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // =========================
    // ✅ ADMIN: LIST BAWAH DARI histori_simpanan (ALL)
    // =========================
    private fun loadAllHistoriSimpananForList() {
        lifecycleScope.launch {
            try {
                val resp = api.getAllHistoriSimpanan()
                if (!resp.isSuccessful) {
                    Toast.makeText(requireContext(), "HTTP ${resp.code()}", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val body = resp.body()
                if (body?.success != true) {
                    Toast.makeText(requireContext(), body?.message ?: "Gagal load histori", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val list: List<HistoriSimpanan> = body.data.orEmpty()

                val mapped = list.map { h ->
                    TransaksiSimpanan(
                        id = h.id,
                        kodePegawai = h.kodePegawai,
                        jenis = extractJenis(h.jenis),
                        jumlah = abs(h.jumlah),
                        tanggal = h.tanggal ?: ""
                    )
                }

                serverSourceList.clear()
                serverSourceList.addAll(mapped)

                applyFilterAndRefresh()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // =========================
    // ✅ ADMIN: TAMBAH TRANSAKSI UNTUK EMPLOYEE PILIHAN
    // =========================
    private fun showAddDialogAdmin() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_simpanan, null)

        val spinnerPegawai = dialogView.findViewById<Spinner>(R.id.spinnerPegawai)
        val etKode = dialogView.findViewById<EditText>(R.id.etKodePegawai)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val spinnerJenis = dialogView.findViewById<Spinner>(R.id.spinnerJenis)

        val jenisList = listOf("Pokok", "Wajib", "Sukarela")
        spinnerJenis.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, jenisList)

        val pegawaiAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            if (kodePegawaiOptions.isNotEmpty()) kodePegawaiOptions else listOf("Tidak ada data")
        )
        spinnerPegawai.adapter = pegawaiAdapter

        if (kodePegawaiOptions.isNotEmpty()) etKode.setText(kodePegawaiOptions[0]) else etKode.setText("")
        etKode.isEnabled = false

        spinnerPegawai.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (kodePegawaiOptions.isNotEmpty() && pos in kodePegawaiOptions.indices) {
                    etKode.setText(kodePegawaiOptions[pos])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi Simpanan (Admin)")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val kode = etKode.text.toString().trim()
                val jumlah = etJumlah.text.toString().toDoubleOrNull()
                val jenis = spinnerJenis.selectedItem.toString()

                if (kode.isBlank()) {
                    Toast.makeText(requireContext(), "Kode pegawai wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val req = SimpananTransaksiRequest(
                    kodePegawai = kode,
                    jenisInput = "Simpanan $jenis",
                    jumlah = jumlah,
                    keterangan = "-"
                )

                lifecycleScope.launch {
                    try {
                        val resp = api.simpananTransaksi(req)
                        val body = resp.body()
                        if (resp.isSuccessful && body?.success == true) {
                            Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()

                            // refresh total (simpanan) + list bawah (histori)
                            loadAllSimpananFromServer()
                            loadAllHistoriSimpananForList()
                        } else {
                            Toast.makeText(requireContext(), body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // =========================
    // FILTER & UI
    // =========================
    private fun applyFilterAndRefresh() {
        val selected = spinnerFilterJenis.selectedItem?.toString() ?: "Semua"

        val filtered = if (selected == "Semua") {
            serverSourceList
        } else {
            serverSourceList.filter { it.jenis.equals(selected, true) }
        }

        displayList.clear()
        displayList.addAll(filtered)
        adapter.notifyDataSetChanged()

        tvEmptyState.visibility = if (displayList.isEmpty()) View.VISIBLE else View.GONE
        rvTransaksi.visibility = if (displayList.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun extractJenis(jenisHistori: String): String {
        val s = jenisHistori.lowercase()
        return when {
            "pokok" in s -> "Pokok"
            "wajib" in s -> "Wajib"
            else -> "Sukarela"
        }
    }
}
