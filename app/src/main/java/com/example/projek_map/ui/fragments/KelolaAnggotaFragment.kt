package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.User
import com.example.projek_map.ui.adapters.AnggotaAdapter
import com.google.android.material.button.MaterialButton

class KelolaAnggotaFragment : Fragment() {

    private lateinit var rvAnggota: RecyclerView
    private lateinit var btnTambahAnggota: MaterialButton
    private lateinit var adapter: AnggotaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_anggota, container, false)
        rvAnggota = view.findViewById(R.id.rvAnggota)
        btnTambahAnggota = view.findViewById(R.id.btnTambahAnggota)

        rvAnggota.layoutManager = LinearLayoutManager(requireContext())

        adapter = AnggotaAdapter(
            DummyUserData.users.toMutableList(),
            onEdit = { user -> showEditDialog(user) },
            onDeactivate = { user -> deactivateUser(user) },
            onDelete = { user, position -> showDeleteOptions(user, position) }
        )

        rvAnggota.adapter = adapter

        btnTambahAnggota.setOnClickListener { showAddDialog() }

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.refreshData(DummyUserData.users)
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

                val newUser = User(
                    kodePegawai = "EMP" + (DummyUserData.users.size + 1).toString().padStart(3, '0'),
                    email = email,
                    password = "1234",
                    nama = nama,
                    statusKeanggotaan = "Anggota Aktif"
                )
                DummyUserData.users.add(newUser)
                adapter.notifyItemInserted(DummyUserData.users.size - 1)
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
                user.nama = etNama.text.toString()
                user.email = etEmail.text.toString()
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Data anggota diperbarui!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteOptions(user: User, position: Int) {
        val options = arrayOf("Nonaktifkan", "Hapus Permanen")

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Kelola Anggota: ${user.nama}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // Nonaktifkan
                        user.statusKeanggotaan = "Nonaktif"
                        adapter.refreshData(DummyUserData.users)
                        Toast.makeText(requireContext(), "${user.nama} dinonaktifkan", Toast.LENGTH_SHORT).show()
                    }
                    1 -> { // Hapus Permanen
                        DummyUserData.users.removeAll { it.kodePegawai == user.kodePegawai }
                        adapter.refreshData(DummyUserData.users)
                        Toast.makeText(requireContext(), "${user.nama} dihapus permanen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deactivateUser(user: User) {
        user.statusKeanggotaan = "Nonaktif"
        adapter.notifyDataSetChanged()
        Toast.makeText(requireContext(), "${user.nama} dinonaktifkan", Toast.LENGTH_SHORT).show()
    }
}