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
import com.example.projek_map.data.KoperasiDatabase
import com.example.projek_map.data.Simpanan
import com.example.projek_map.data.TransaksiSimpanan
import com.example.projek_map.ui.adapters.TransaksiSimpananAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KelolaSimpananFragment : Fragment() {

    private lateinit var rvTransaksi: RecyclerView
    private lateinit var btnTambah: MaterialButton
    private lateinit var adapter: TransaksiSimpananAdapter

    // 🔹 Filter & ringkasan
    private lateinit var spinnerFilterJenis: Spinner
    private lateinit var tvTotalPokok: TextView
    private lateinit var tvTotalWajib: TextView
    private lateinit var tvTotalSukarela: TextView
    private lateinit var tvEmptyState: TextView

    // 🔹 List tampilan (hasil filter)
    private val displayList = mutableListOf<TransaksiSimpanan>()

    private val rupiah: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("in", "ID"))
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
            onEdit = { item, pos -> showEditDialog(item, pos) },
            onDelete = { item, pos -> showDeleteDialog(item, pos) }
        )
        rvTransaksi.adapter = adapter

        // Spinner filter
        val jenisFilter = listOf("Semua", "Pokok", "Wajib", "Sukarela")
        spinnerFilterJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            jenisFilter
        )
        spinnerFilterJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                applyFilterAndRefresh()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnTambah.setOnClickListener { showAddDialog() }

        applyFilterAndRefresh()

        return view
    }

    override fun onResume() {
        super.onResume()
        applyFilterAndRefresh()
    }

    // ==============================
    // Tambah / Edit / Hapus
    // ==============================

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_simpanan, null)
        val etKode = dialogView.findViewById<EditText>(R.id.etKodePegawai)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val spinnerJenis = dialogView.findViewById<Spinner>(R.id.spinnerJenis)

        val jenisList = listOf("Pokok", "Wajib", "Sukarela")
        spinnerJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            jenisList
        )

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Transaksi Simpanan")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val kode = etKode.text.toString().trim()
                val jumlahText = etJumlah.text.toString().trim()
                val jenis = spinnerJenis.selectedItem.toString()

                if (kode.isEmpty() || jumlahText.isEmpty()) {
                    Toast.makeText(requireContext(), "Isi semua kolom!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val jumlah = jumlahText.toDoubleOrNull()
                if (jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Jumlah tidak valid!", Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                // Proses di background (cek user, update simpanan & transaksi)
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val appContext = requireContext().applicationContext
                    val db = KoperasiDatabase.getInstance(appContext)
                    val userDao = db.userDao()

                    val user = userDao.getUserByKode(kode)
                    if (user == null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Kode pegawai tidak ditemukan!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@launch
                    }

                    tambahTransaksiSimpananDiDb(db, kode, jenis, jumlah)

                    withContext(Dispatchers.Main) {
                        applyFilterAndRefresh()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi simpanan berhasil ditambahkan!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(item: TransaksiSimpanan, pos: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_simpanan, null)
        val etKode = dialogView.findViewById<EditText>(R.id.etKodePegawai)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val spinnerJenis = dialogView.findViewById<Spinner>(R.id.spinnerJenis)

        etKode.setText(item.kodePegawai)
        etKode.isEnabled = false
        etJumlah.setText(item.jumlah.toString())

        val jenisList = listOf("Pokok", "Wajib", "Sukarela")
        spinnerJenis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            jenisList
        )
        spinnerJenis.setSelection(jenisList.indexOfFirst { it.equals(item.jenis, true) })

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Transaksi Simpanan")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newJumlah = etJumlah.text.toString().toDoubleOrNull()
                val newJenis = spinnerJenis.selectedItem.toString()

                if (newJumlah == null || newJumlah <= 0) {
                    Toast.makeText(requireContext(), "Jumlah tidak valid!", Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val appContext = requireContext().applicationContext
                    val db = KoperasiDatabase.getInstance(appContext)
                    val transaksiDao = db.transaksiSimpananDao()

                    // Update hanya pada tabel transaksi (perilaku sama seperti implementasi lama)
                    transaksiDao.updateTransaksi(
                        item.copy(jenis = newJenis, jumlah = newJumlah)
                    )

                    withContext(Dispatchers.Main) {
                        adapter.updateAt(pos, item.copy(jenis = newJenis, jumlah = newJumlah))
                        applyFilterAndRefresh()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi diperbarui!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteDialog(item: TransaksiSimpanan, pos: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Transaksi")
            .setMessage("Hapus transaksi ${item.jenis} milik ${item.kodePegawai}?")
            .setPositiveButton("Hapus") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val appContext = requireContext().applicationContext
                    val db = KoperasiDatabase.getInstance(appContext)
                    val transaksiDao = db.transaksiSimpananDao()

                    transaksiDao.deleteTransaksi(item)

                    withContext(Dispatchers.Main) {
                        adapter.removeAt(pos)
                        applyFilterAndRefresh()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi dihapus!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // ==============================
    // 🔧 Helper: DB + Filter + Ringkasan
    // ==============================

    private suspend fun tambahTransaksiSimpananDiDb(
        db: KoperasiDatabase,
        kodePegawai: String,
        jenis: String,
        jumlah: Double
    ) {
        val simpananDao = db.simpananDao()
        val transaksiDao = db.transaksiSimpananDao()

        // Update saldo simpanan anggota
        val s = simpananDao.getSimpananByKode(kodePegawai)
        if (s == null) {
            val (pokok, wajib, sukarela) = when (jenis.lowercase()) {
                "pokok" -> Triple(jumlah, 0.0, 0.0)
                "wajib" -> Triple(0.0, jumlah, 0.0)
                else -> Triple(0.0, 0.0, jumlah)
            }
            simpananDao.insertAllSimpanan(listOf(Simpanan(kodePegawai, pokok, wajib, sukarela)))
        } else {
            val newPokok =
                if (jenis.equals("Pokok", true)) s.simpananPokok + jumlah else s.simpananPokok
            val newWajib =
                if (jenis.equals("Wajib", true)) s.simpananWajib + jumlah else s.simpananWajib
            val newSukarela =
                if (jenis.equals("Sukarela", true)) s.simpananSukarela + jumlah else s.simpananSukarela

            val updated = s.copy(
                simpananPokok = newPokok,
                simpananWajib = newWajib,
                simpananSukarela = newSukarela
            )
            simpananDao.insertAllSimpanan(listOf(updated))
        }

        // Insert ke tabel transaksi_simpanan
        val maxId = transaksiDao.getMaxId() ?: 0
        val idBaru = maxId + 1
        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).format(Date())

        val transaksi = TransaksiSimpanan(
            id = idBaru,
            kodePegawai = kodePegawai,
            jenis = jenis,
            jumlah = jumlah,
            tanggal = tanggal
        )
        transaksiDao.insertAllTransaksi(listOf(transaksi))
    }

    private fun applyFilterAndRefresh() {
        val selected = spinnerFilterJenis.selectedItem?.toString() ?: "Semua"
        val appContext = requireContext().applicationContext

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val db = KoperasiDatabase.getInstance(appContext)
            val transaksiDao = db.transaksiSimpananDao()

            val source = transaksiDao.getAllTransaksi()
            val filtered = if (selected == "Semua") {
                source
            } else {
                source.filter { it.jenis.equals(selected, ignoreCase = true) }
            }

            val totalPokok =
                filtered.filter { it.jenis.equals("Pokok", true) }.sumOf { it.jumlah }
            val totalWajib =
                filtered.filter { it.jenis.equals("Wajib", true) }.sumOf { it.jumlah }
            val totalSukarela =
                filtered.filter { it.jenis.equals("Sukarela", true) }.sumOf { it.jumlah }

            withContext(Dispatchers.Main) {
                displayList.clear()
                displayList.addAll(filtered)
                adapter.notifyDataSetChanged()

                updateEmptyState(displayList.isEmpty())
                updateSummary(totalPokok, totalWajib, totalSukarela)
            }
        }
    }

    private fun updateSummary(totalPokok: Double, totalWajib: Double, totalSukarela: Double) {
        tvTotalPokok.text = rupiah.format(totalPokok)
        tvTotalWajib.text = rupiah.format(totalWajib)
        tvTotalSukarela.text = rupiah.format(totalSukarela)
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        rvTransaksi.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}
