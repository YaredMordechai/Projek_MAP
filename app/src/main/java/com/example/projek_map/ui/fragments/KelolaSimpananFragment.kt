package com.example.projek_map.ui.fragments

import android.app.AlertDialog
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
import com.example.projek_map.data.TransaksiSimpanan
import com.example.projek_map.ui.adapters.TransaksiSimpananAdapter
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
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

    // 🔹 List tampilan (hasil filter) — tetap dipakai sesuai keinginanmu
    private val displayList = mutableListOf<TransaksiSimpanan>()

    private val rupiah: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_simpanan, container, false)

        // View binding
        rvTransaksi = view.findViewById(R.id.rvTransaksiSimpanan)
        btnTambah = view.findViewById(R.id.btnTambahSimpanan)

        spinnerFilterJenis = view.findViewById(R.id.spinnerFilterJenis)
        tvTotalPokok = view.findViewById(R.id.tvTotalPokok)
        tvTotalWajib = view.findViewById(R.id.tvTotalWajib)
        tvTotalSukarela = view.findViewById(R.id.tvTotalSukarela)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        // RecyclerView
        rvTransaksi.layoutManager = LinearLayoutManager(requireContext())

        // ⬇️ Penting: pakai displayList langsung (bukan toMutableList)
        adapter = TransaksiSimpananAdapter(
            displayList,
            onEdit = { item, pos ->
                val dialogView = layoutInflater.inflate(R.layout.dialog_add_simpanan, null)
                val etKode = dialogView.findViewById<EditText>(R.id.etKodePegawai)
                val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
                val spinnerJenis = dialogView.findViewById<Spinner>(R.id.spinnerJenis)

                etKode.setText(item.kodePegawai)
                etKode.isEnabled = false // fixed
                etJumlah.setText(item.jumlah.toString())

                val jenisList = listOf("Pokok", "Wajib", "Sukarela")
                spinnerJenis.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, jenisList)
                spinnerJenis.setSelection(jenisList.indexOfFirst { it.equals(item.jenis, true) })

                AlertDialog.Builder(requireContext())
                    .setTitle("Edit Transaksi Simpanan")
                    .setView(dialogView)
                    .setPositiveButton("Simpan") { _, _ ->
                        val newJumlah = etJumlah.text.toString().toDoubleOrNull()
                        val newJenis = spinnerJenis.selectedItem.toString()

                        if (newJumlah == null || newJumlah <= 0) {
                            Toast.makeText(requireContext(), "Jumlah tidak valid!", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        // Update sumber data global
                        val index = DummyUserData.transaksiSimpananList.indexOfFirst { it.id == item.id }
                        if (index >= 0) {
                            DummyUserData.transaksiSimpananList[index] =
                                item.copy(jenis = newJenis, jumlah = newJumlah)
                        }

                        // Update tampilan & ringkasan
                        adapter.updateAt(pos, item.copy(jenis = newJenis, jumlah = newJumlah))
                        applyFilterAndRefresh()
                        Toast.makeText(requireContext(), "Transaksi diperbarui!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            },
            onDelete = { item, pos ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Transaksi")
                    .setMessage("Hapus transaksi ${item.jenis} milik ${item.kodePegawai}?")
                    .setPositiveButton("Hapus") { _, _ ->
                        DummyUserData.transaksiSimpananList.removeAll { it.id == item.id }
                        adapter.removeAt(pos)
                        applyFilterAndRefresh()
                        Toast.makeText(requireContext(), "Transaksi dihapus!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )
        rvTransaksi.adapter = adapter

        // Spinner filter — isi dari kode
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

        // Tombol tambah
        btnTambah.setOnClickListener { showAddDialog() }

        // Tampilkan awal
        applyFilterAndRefresh()

        return view
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_simpanan, null)
        val etKode = dialogView.findViewById<EditText>(R.id.etKodePegawai)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val spinnerJenis = dialogView.findViewById<Spinner>(R.id.spinnerJenis)

        val jenisList = listOf("Pokok", "Wajib", "Sukarela")
        spinnerJenis.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, jenisList)

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
                    Toast.makeText(requireContext(), "Jumlah tidak valid!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val user = DummyUserData.users.find { it.kodePegawai == kode }
                if (user == null) {
                    Toast.makeText(requireContext(), "Kode pegawai tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                DummyUserData.tambahTransaksiSimpanan(kode, jenis, jumlah)
                applyFilterAndRefresh()
                Toast.makeText(requireContext(), "Transaksi simpanan berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        applyFilterAndRefresh()
    }

    // ==============================
    // 🔧 Helper: Filter + Ringkasan
    // ==============================

    private fun applyFilterAndRefresh() {
        val selected = spinnerFilterJenis.selectedItem?.toString() ?: "Semua"

        val source = DummyUserData.transaksiSimpananList
        val filtered = if (selected == "Semua") {
            source
        } else {
            source.filter { it.jenis.equals(selected, ignoreCase = true) }
        }

        // ⬇️ Kamu minta baris ini tetap → aman, adapter memegang list yang sama (displayList)
        displayList.clear()
        displayList.addAll(filtered)
        adapter.notifyDataSetChanged()

        updateEmptyState(displayList.isEmpty())
        updateSummary(filtered)
    }

    private fun updateSummary(list: List<TransaksiSimpanan>) {
        val totalPokok = list.filter { it.jenis.equals("Pokok", true) }.sumOf { it.jumlah }
        val totalWajib = list.filter { it.jenis.equals("Wajib", true) }.sumOf { it.jumlah }
        val totalSukarela = list.filter { it.jenis.equals("Sukarela", true) }.sumOf { it.jumlah }

        tvTotalPokok.text = rupiah.format(totalPokok)
        tvTotalWajib.text = rupiah.format(totalWajib)
        tvTotalSukarela.text = rupiah.format(totalSukarela)
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        rvTransaksi.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}
