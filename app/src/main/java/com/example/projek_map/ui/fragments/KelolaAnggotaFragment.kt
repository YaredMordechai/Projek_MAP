package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.User
import com.example.projek_map.ui.adapters.AnggotaAdapter
import com.example.projek_map.viewmodel.KoperasiViewModel
import com.google.android.material.button.MaterialButton

class KelolaAnggotaFragment : Fragment() {

    private lateinit var rvAnggota: RecyclerView
    private lateinit var btnTambahAnggota: MaterialButton
    private lateinit var adapter: AnggotaAdapter

    // shared ViewModel (dipakai oleh activity yang sama)
    private val viewModel: KoperasiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_anggota, container, false)
        rvAnggota = view.findViewById(R.id.rvAnggota)
        btnTambahAnggota = view.findViewById(R.id.btnTambahAnggota)

        rvAnggota.layoutManager = LinearLayoutManager(requireContext())

        // Awalnya list kosong, nanti diisi dari LiveData ViewModel
        adapter = AnggotaAdapter(
            mutableListOf(),
            onEdit = { user -> showEditDialog(user) },
            onDeactivate = { user -> deactivateUser(user) },
            onDelete = { user, _ -> showDeleteOptions(user) }
        )
        rvAnggota.adapter = adapter

        // Observasi data user dari ViewModel
        viewModel.users.observe(viewLifecycleOwner) { list ->
            adapter.refreshData(list)
        }

        btnTambahAnggota.setOnClickListener { showAddDialog() }

        return view
    }

    override fun onResume() {
        super.onResume()
        // reload dari DB (jaga-jaga kalau ada perubahan dari layar lain)
        viewModel.loadUsers()
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val etNama = dialogView.findViewById<EditText>(R.id.etNama)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Anggota Baru")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ ->
                val nama = etNama.text.toString().trim()
                val email = etEmail.text.toString().trim()

                if (nama.isEmpty() || email.isEmpty()) {
                    Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Simpan lewat ViewModel -> Repository -> Room
                viewModel.tambahAnggota(nama, email)
                Toast.makeText(requireContext(), "Anggota baru ditambahkan!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(user: User) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val etNama = dialogView.findViewById<EditText>(R.id.etNama)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        etNama.setText(user.nama)
        etEmail.setText(user.email)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Data Anggota")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val namaBaru = etNama.text.toString().trim()
                val emailBaru = etEmail.text.toString().trim()

                if (namaBaru.isEmpty() || emailBaru.isEmpty()) {
                    Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Update lewat ViewModel, bukan edit objek di memori saja
                viewModel.updateAnggota(user.kodePegawai, namaBaru, emailBaru)
                Toast.makeText(requireContext(), "Data anggota diperbarui!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteOptions(user: User) {
        val options = arrayOf("Nonaktifkan", "Hapus Permanen")

        AlertDialog.Builder(requireContext())
            .setTitle("Kelola Anggota: ${user.nama}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // Nonaktifkan
                        viewModel.nonaktifkanAnggota(user)
                        Toast.makeText(requireContext(), "${user.nama} dinonaktifkan", Toast.LENGTH_SHORT).show()
                    }
                    1 -> { // Hapus Permanen
                        viewModel.hapusAnggota(user)
                        Toast.makeText(requireContext(), "${user.nama} dihapus permanen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deactivateUser(user: User) {
        viewModel.nonaktifkanAnggota(user)
        Toast.makeText(requireContext(), "${user.nama} dinonaktifkan", Toast.LENGTH_SHORT).show()
    }
}