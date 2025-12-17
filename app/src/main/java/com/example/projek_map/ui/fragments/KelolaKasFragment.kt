package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.KasDeleteRequest
import com.example.projek_map.api.KasTransaksiAddRequest
import com.example.projek_map.api.KasTransaksiUpdateRequest
import com.example.projek_map.api.KasTransaksi
import com.example.projek_map.ui.adapters.KasAdapter
import com.example.projek_map.ui.viewmodels.KelolaKasViewModel
import com.example.projek_map.utils.Event
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class KelolaKasFragment : Fragment() {

    // List & Adapter
    private lateinit var rvKas: RecyclerView
    private lateinit var adapter: KasAdapter

    // Header & empty state
    private lateinit var tvSaldoKas: TextView
    private lateinit var tvEmptyKas: TextView

    // Buttons
    private lateinit var btnTambahKas: Button
    private lateinit var btnFilterTerapkan: Button
    private lateinit var btnFilterReset: Button

    private lateinit var btnLabaBersih: Button
    private lateinit var btnLabaRugi: Button

    // Filter views
    private lateinit var spFilterJenis: Spinner
    private lateinit var spFilterKategori: Spinner
    private lateinit var etFilterDari: EditText
    private lateinit var etFilterSampai: EditText
    private lateinit var etFilterCari: EditText

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))
    private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale("id","ID"))

    private val kasAll = mutableListOf<KasTransaksi>()

    private val viewModel: KelolaKasViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.kelola_kas_fragment, container, false)

        // Bind views
        tvSaldoKas = v.findViewById(R.id.tvSaldoKas)
        tvEmptyKas = v.findViewById(R.id.tvEmptyKas)
        btnTambahKas = v.findViewById(R.id.btnTambahKas)
        rvKas = v.findViewById(R.id.rvKas)

        spFilterJenis     = v.findViewById(R.id.spFilterJenis)
        spFilterKategori  = v.findViewById(R.id.spFilterKategori)
        etFilterDari      = v.findViewById(R.id.etFilterDari)
        etFilterSampai    = v.findViewById(R.id.etFilterSampai)
        etFilterCari      = v.findViewById(R.id.etFilterCari)
        btnFilterTerapkan = v.findViewById(R.id.btnFilterTerapkan)
        btnFilterReset    = v.findViewById(R.id.btnFilterReset)

        btnLabaBersih = v.findViewById(R.id.btnLabaBersih)
        btnLabaBersih.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LabaBersihFragment())
                .addToBackStack(null)
                .commit()
        }

        btnLabaRugi = v.findViewById(R.id.btnLabaRugi)
        btnLabaRugi.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LabaRugiFragment())
                .addToBackStack(null)
                .commit()
        }

        // Recycler
        rvKas.layoutManager = LinearLayoutManager(requireContext())
        adapter = KasAdapter(
            mutableListOf(), // tampilannya akan diisi dari applyFilter()
            onEdit = { item, _ -> showEditDialog(item) },
            onDelete = { item, pos -> confirmDelete(item, pos) }
        )
        rvKas.adapter = adapter

        // Filter: Jenis
        val jenisItems = listOf("Semua", "Masuk", "Keluar")
        spFilterJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            jenisItems
        )

        // Filter: Kategori (awal kosong dulu, nanti diupdate setelah loadKas)
        spFilterKategori.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Semua")
        )

        // default date range awal bulan - hari ini bila kosong
        if (etFilterDari.text.isNullOrBlank() || etFilterSampai.text.isNullOrBlank()) {
            val calNow = java.util.Calendar.getInstance()
            val calStart = (calNow.clone() as java.util.Calendar).apply {
                set(java.util.Calendar.DAY_OF_MONTH, 1)
            }
            if (etFilterDari.text.isNullOrBlank()) etFilterDari.setText(dateFmt.format(calStart.time))
            if (etFilterSampai.text.isNullOrBlank()) etFilterSampai.setText(dateFmt.format(calNow.time))
        }

        // Tombol filter
        btnFilterTerapkan.setOnClickListener { applyFilter() }
        btnFilterReset.setOnClickListener {
            spFilterJenis.setSelection(0)
            spFilterKategori.setSelection(0)
            etFilterDari.setText("")
            etFilterSampai.setText("")
            etFilterCari.setText("")
            applyFilter()
        }

        // Tambah
        btnTambahKas.setOnClickListener { showAddDialog() }

        // Tampilkan awal (akan diisi setelah load)
        applyFilter()
        updateSaldoAndEmpty()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observers
        viewModel.kasList.observe(viewLifecycleOwner) { data ->
            kasAll.clear()
            kasAll.addAll(data ?: emptyList())

            // update kategori spinner dari data terbaru
            updateKategoriSpinner()

            // pakai filter aktif
            applyFilter()
        }

        viewModel.toastEvent.observe(viewLifecycleOwner) { ev: Event<String> ->
            ev.getContentIfNotHandled()?.let { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        // Data bisa berubah — ambil dari MySQL dulu, lalu filter aktif
        loadKas()
    }

    private fun loadKas() {
        viewModel.loadKas()
    }

    private fun updateKategoriSpinner() {
        val distinctKategori = kasAll.map { it.kategori }.distinct().sorted()
        val items = mutableListOf("Semua").apply { addAll(distinctKategori) }

        val current = spFilterKategori.selectedItem?.toString() ?: "Semua"
        val adapterSpinner = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        spFilterKategori.adapter = adapterSpinner

        val idx = items.indexOfFirst { it.equals(current, true) }.let { if (it >= 0) it else 0 }
        spFilterKategori.setSelection(idx)
    }

    private fun updateSaldoAndEmpty() {
        // Header saldo menampilkan total global (tidak terfilter)
        val saldo = kasAll.sumOf { t ->
            if (t.jenis.equals("Masuk", true)) t.jumlah else -t.jumlah
        }
        tvSaldoKas.text = "Saldo Kas: ${rupiah.format(saldo)}"

        val isEmpty = adapter.itemCount == 0
        tvEmptyKas.visibility = if (isEmpty) View.VISIBLE else View.GONE
        rvKas.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun parseDateMs(s: String): Long? = try {
        if (s.isBlank()) null else dateFmt.parse(s)?.time
    } catch (_: Exception) { null }

    private fun applyFilter() {
        val all = kasAll

        val jenisSelected = spFilterJenis.selectedItem?.toString()?.lowercase() ?: "semua"
        val kategoriSelected = spFilterKategori.selectedItem?.toString() ?: "Semua"
        val q = etFilterCari.text?.toString()?.trim()?.lowercase().orEmpty()

        val dariStr = etFilterDari.text?.toString()?.trim().orEmpty()
        val sampaiStr = etFilterSampai.text?.toString()?.trim().orEmpty()
        val dari = parseDateMs(dariStr)
        val sampai = parseDateMs(sampaiStr)

        val filtered = all.filter { t ->
            // Jenis
            val okJenis = when (jenisSelected) {
                "masuk"  -> t.jenis.equals("Masuk", true)
                "keluar" -> t.jenis.equals("Keluar", true)
                else     -> true
            }
            if (!okJenis) return@filter false

            // Kategori
            val okKategori = if (kategoriSelected == "Semua") true
            else t.kategori.equals(kategoriSelected, true)
            if (!okKategori) return@filter false

            // Rentang tanggal
            val tMs = parseDateMs(t.tanggal)
            val okDate = when {
                (dari == null && sampai == null) -> true
                (dari != null && sampai == null) -> (tMs ?: Long.MIN_VALUE) >= dari
                (dari == null && sampai != null) -> (tMs ?: Long.MAX_VALUE) <= sampai
                else -> (tMs ?: 0L) in dari!!..sampai!!
            }
            if (!okDate) return@filter false

            // Pencarian teks
            if (q.isNotBlank()) {
                val join = "${t.kategori} ${t.deskripsi}".lowercase()
                join.contains(q)
            } else true
        }

        adapter.replaceAll(filtered)
        updateSaldoAndEmpty()
    }

    // === Konfirmasi Hapus ===
    private fun confirmDelete(item: KasTransaksi, pos: Int) {
        val msg = buildString {
            append("Yakin mau hapus transaksi ini?\n\n")
            append("Tanggal : ${item.tanggal}\n")
            append("Jenis   : ${item.jenis}\n")
            append("Kategori: ${item.kategori}\n")
            if (item.deskripsi.isNotBlank()) append("Deskripsi: ${item.deskripsi}\n")
            append("Jumlah  : ${rupiah.format(item.jumlah)}")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Hapus")
            .setMessage(msg)
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteKas(KasDeleteRequest(item.id))
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAddDialog() {
        val dv = layoutInflater.inflate(R.layout.dialog_add_kas, null)
        val etTanggal = dv.findViewById<EditText>(R.id.etTanggalKas)
        val spJenis = dv.findViewById<Spinner>(R.id.spinnerJenisKas)
        val spKategori = dv.findViewById<Spinner>(R.id.spinnerKategoriKas)
        val etKategoriCustom = dv.findViewById<EditText>(R.id.etKategoriKasCustom)
        val etDeskripsi = dv.findViewById<EditText>(R.id.etDeskripsiKas)
        val etJumlah = dv.findViewById<EditText>(R.id.etJumlahKas)

        spJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Masuk","Keluar")
        )

        // Daftar kategori distinct dari kasAll + "Tambah kategori..."
        val baseKategori = kasAll.map { it.kategori }.distinct().sorted()
        val kategoriItems = baseKategori.toMutableList().apply { add("Tambah kategori...") }
        spKategori.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            kategoriItems
        )

        spKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val isTambah = kategoriItems.getOrNull(position) == "Tambah kategori..."
                etKategoriCustom.visibility = if (isTambah) View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // default tanggal hari ini
        etTanggal.setText(java.time.LocalDate.now().toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi Kas")
            .setView(dv)
            .setPositiveButton("Simpan") { _, _ ->
                val tanggal = etTanggal.text.toString().trim()
                val jenis = spJenis.selectedItem.toString()
                val kategoriSelected = spKategori.selectedItem.toString()
                val kategori = if (kategoriSelected == "Tambah kategori...") {
                    etKategoriCustom.text.toString().trim()
                } else kategoriSelected
                val deskripsi = etDeskripsi.text.toString().trim()
                val jumlah = etJumlah.text.toString().trim()
                    .replace(".","").replace(",",".").toDoubleOrNull()

                if (tanggal.isEmpty() || kategori.isEmpty() || jumlah == null || jumlah <= 0.0) {
                    Toast.makeText(requireContext(), "Isi data dengan benar.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.addKas(
                    KasTransaksiAddRequest(
                        tanggal = tanggal,
                        jenis = jenis,
                        kategori = kategori,
                        deskripsi = deskripsi,
                        jumlah = jumlah
                    )
                )
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(item: KasTransaksi) {
        val dv = layoutInflater.inflate(R.layout.dialog_add_kas, null)
        val etTanggal = dv.findViewById<EditText>(R.id.etTanggalKas)
        val spJenis = dv.findViewById<Spinner>(R.id.spinnerJenisKas)
        val spKategori = dv.findViewById<Spinner>(R.id.spinnerKategoriKas)
        val etKategoriCustom = dv.findViewById<EditText>(R.id.etKategoriKasCustom)
        val etDeskripsi = dv.findViewById<EditText>(R.id.etDeskripsiKas)
        val etJumlah = dv.findViewById<EditText>(R.id.etJumlahKas)

        spJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Masuk","Keluar")
        )

        val baseKategori = kasAll.map { it.kategori }.distinct().sorted()
        val kategoriItems = baseKategori.toMutableList().apply { add("Tambah kategori...") }
        spKategori.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            kategoriItems
        )

        // Prefill data
        etTanggal.setText(item.tanggal)
        spJenis.setSelection(if (item.jenis.equals("Masuk", true)) 0 else 1)

        val idxKategori = kategoriItems.indexOfFirst { it.equals(item.kategori, true) }
            .let { if (it >= 0) it else kategoriItems.lastIndex }
        spKategori.setSelection(idxKategori)

        if (idxKategori == kategoriItems.lastIndex) {
            etKategoriCustom.visibility = View.VISIBLE
            etKategoriCustom.setText(item.kategori)
        } else {
            etKategoriCustom.visibility = View.GONE
        }

        etDeskripsi.setText(item.deskripsi)
        etJumlah.setText(item.jumlah.toString())

        spKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val isTambah = kategoriItems.getOrNull(position) == "Tambah kategori..."
                etKategoriCustom.visibility = if (isTambah) View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Transaksi Kas")
            .setView(dv)
            .setPositiveButton("Simpan") { _, _ ->
                val tanggal = etTanggal.text.toString().trim()
                val jenis = spJenis.selectedItem.toString()
                val kategoriSelected = spKategori.selectedItem.toString()
                val kategori = if (kategoriSelected == "Tambah kategori...") {
                    etKategoriCustom.text.toString().trim()
                } else kategoriSelected
                val deskripsi = etDeskripsi.text.toString().trim()
                val jumlah = etJumlah.text.toString().trim()
                    .replace(".","").replace(",",".").toDoubleOrNull()

                if (tanggal.isEmpty() || kategori.isEmpty() || jumlah == null || jumlah <= 0.0) {
                    Toast.makeText(requireContext(), "Isi data dengan benar.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.updateKas(
                    KasTransaksiUpdateRequest(
                        id = item.id,
                        tanggal = tanggal,
                        jenis = jenis,
                        kategori = kategori,
                        deskripsi = deskripsi,
                        jumlah = jumlah
                    )
                )
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
