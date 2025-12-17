package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
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
import com.example.projek_map.api.SimpananPending
import com.example.projek_map.api.SimpananTransaksiRequest
import com.example.projek_map.data.SimpananRepository
import com.example.projek_map.api.TransaksiSimpanan
import com.example.projek_map.ui.adapters.TransaksiSimpananAdapter
import com.example.projek_map.ui.viewmodels.KelolaSimpananViewModel
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale
import androidx.lifecycle.lifecycleScope


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
    private val serverSourceList = mutableListOf<TransaksiSimpanan>()

    private val kodePegawaiOptions = mutableListOf<String>()

    private val rupiah by lazy {
        // ✅ fix deprecated Locale("in","ID")
        NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    }

    // ✅ simpan mapping pendingId -> data pending (untuk approve/reject)
    private val pendingMap = mutableMapOf<Int, SimpananPending>()

    // ===== MVVM =====
    private val viewModel: KelolaSimpananViewModel by viewModels {
        KelolaSimpananViewModel.Factory(SimpananRepository())
    }

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
            onEdit = { item, _ ->
                // ✅ onEdit dipakai untuk APPROVE kalau item pending
                if (item.id < 0) {
                    val pendingId = -item.id
                    val p = pendingMap[pendingId]
                    if (p != null) showPendingDecideDialog(pendingId, p)
                    else Toast.makeText(requireContext(), "Data pending tidak ditemukan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Edit belum tersedia", Toast.LENGTH_SHORT).show()
                }
            },
            onDelete = { item, _ ->
                // ✅ onDelete dipakai untuk REJECT kalau item pending
                if (item.id < 0) {
                    val pendingId = -item.id
                    confirmRejectPending(pendingId)
                } else {
                    Toast.makeText(requireContext(), "Hapus belum tersedia", Toast.LENGTH_SHORT).show()
                }
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

        observeVm()

        // load awal
        viewModel.loadAll()

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAll()
    }

    private fun observeVm() {
        viewModel.state.observe(viewLifecycleOwner) { st ->
            st.message?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }

            // header
            tvTotalPokok.text = rupiah.format(st.header.totalPokok)
            tvTotalWajib.text = rupiah.format(st.header.totalWajib)
            tvTotalSukarela.text = rupiah.format(st.header.totalSukarela)

            kodePegawaiOptions.clear()
            kodePegawaiOptions.addAll(st.header.kodePegawaiOptions)

            // pending map
            pendingMap.clear()
            pendingMap.putAll(st.pendingMap)

            // list
            serverSourceList.clear()
            serverSourceList.addAll(st.list)

            applyFilterAndRefresh()
        }
    }

    private fun showPendingDecideDialog(pendingId: Int, p: SimpananPending) {
        val msg = buildString {
            append("Kode: ${p.kodePegawai}\n")
            append("Jenis: ${p.jenisInput}\n")
            append("Jumlah: ${rupiah.format(p.jumlah)}\n")
            append("Tanggal: ${p.tanggal ?: "-"}\n")
            append("Status: ${p.statusVerifikasi ?: "-"}\n\n")
            append("Bukti: ${p.buktiUrl ?: "-"}\n")
            append("\nKlik OK untuk buka bukti, lalu pilih Verifikasi.")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Verifikasi Simpanan Pending #$pendingId")
            .setMessage(msg)
            .setPositiveButton("OK (Buka Bukti)") { _, _ ->
                val url = p.buktiUrl
                if (!url.isNullOrBlank()) {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    } catch (_: Exception) { }
                }
                confirmApprovePending(pendingId)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun confirmApprovePending(pendingId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Approve?")
            .setMessage("Setujui transaksi pending #$pendingId?")
            .setPositiveButton("Approve") { _, _ -> viewModel.decidePending(pendingId, "approve") }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun confirmRejectPending(pendingId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Reject?")
            .setMessage("Tolak transaksi pending #$pendingId?")
            .setPositiveButton("Reject") { _, _ -> viewModel.decidePending(pendingId, "reject") }
            .setNegativeButton("Batal", null)
            .show()
    }

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

                // NOTE: transaksi admin tetap pakai endpoint transaksi existing (fitur tidak berubah)
                val repo = SimpananRepository()
                val req = SimpananTransaksiRequest(
                    kodePegawai = kode,
                    jenisInput = "Simpanan $jenis",
                    jumlah = jumlah,
                    keterangan = "-"
                )

                // tetap jalan seperti sebelumnya, lalu reload via VM
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    try {
                        val resp = repo.transaksi(req)
                        val body = resp.body()
                        if (resp.isSuccessful && body?.success == true) {
                            Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()
                            viewModel.loadAll()
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

    private fun applyFilterAndRefresh() {
        val selected = spinnerFilterJenis.selectedItem?.toString() ?: "Semua"

        val filtered = if (selected == "Semua") {
            serverSourceList
        } else {
            serverSourceList.filter {
                // pending punya "Pokok (Pending)" dll, jadi pakai contains supaya filter tetap jalan
                it.jenis.contains(selected, ignoreCase = true)
            }
        }

        displayList.clear()
        displayList.addAll(filtered)
        adapter.notifyDataSetChanged()

        tvEmptyState.visibility = if (displayList.isEmpty()) View.VISIBLE else View.GONE
        rvTransaksi.visibility = if (displayList.isEmpty()) View.GONE else View.VISIBLE
    }
}
