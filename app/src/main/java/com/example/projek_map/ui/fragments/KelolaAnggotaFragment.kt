package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.AddUserRequest
import com.example.projek_map.api.UpdateUserRequest
import com.example.projek_map.api.SetUserStatusRequest
import com.example.projek_map.api.DeleteUserRequest
import com.example.projek_map.api.User
import com.example.projek_map.ui.adapters.AnggotaAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class KelolaAnggotaFragment : Fragment() {

    private lateinit var rvAnggota: RecyclerView
    private lateinit var btnTambahAnggota: MaterialButton
    private lateinit var adapter: AnggotaAdapter

    private val api = ApiClient.apiService
    private val anggotaList = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_anggota, container, false)
        rvAnggota = view.findViewById(R.id.rvAnggota)
        btnTambahAnggota = view.findViewById(R.id.btnTambahAnggota)

        rvAnggota.layoutManager = LinearLayoutManager(requireContext())

        adapter = AnggotaAdapter(
            anggotaList,
            onEdit = { user -> showEditDialog(user) },
            onDeactivate = { user -> setStatusUser(user, "Nonaktif") },
            onDelete = { user, position -> showDeleteOptions(user) }
        )
        rvAnggota.adapter = adapter

        btnTambahAnggota.setOnClickListener { showAddDialog() }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadAnggota()
    }

    private fun loadAnggota() {
        lifecycleScope.launch {
            try {
                val res = api.getAllUsers()
                if (res.isSuccessful && res.body()?.success == true) {
                    val data = res.body()?.data.orEmpty()
                    anggotaList.clear()
                    anggotaList.addAll(data)
                    adapter.refreshData(anggotaList)
                } else {
                    Toast.makeText(requireContext(), res.body()?.message ?: "Gagal memuat anggota", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val etNama = dialogView.findViewById<EditText>(R.id.etNama)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Anggota Baru")
            .setView(dialogView)
            .setPositiveButton("Tambah", null)
            .setNegativeButton("Batal", null)
            .create()
            .also { dialog ->
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val nama = etNama.text.toString().trim()
                        val email = etEmail.text.toString().trim()

                        if (nama.isEmpty() || email.isEmpty()) {
                            Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val nextKode = generateNextKode(anggotaList)
                        lifecycleScope.launch {
                            try {
                                val res = api.addUser(
                                    AddUserRequest(
                                        kodePegawai = nextKode,
                                        nama = nama,
                                        email = email,
                                        password = "1234",
                                        statusKeanggotaan = "Anggota Aktif"
                                    )
                                )
                                if (res.isSuccessful && res.body()?.success == true) {
                                    Toast.makeText(requireContext(), "Anggota ditambahkan", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                    loadAnggota()
                                } else {
                                    Toast.makeText(requireContext(), res.body()?.message ?: "Gagal tambah anggota", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                dialog.show()
            }
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
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()
            .also { dialog ->
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val newNama = etNama.text.toString().trim()
                        val newEmail = etEmail.text.toString().trim()

                        if (newNama.isEmpty() || newEmail.isEmpty()) {
                            Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        lifecycleScope.launch {
                            try {
                                val res = api.updateUser(
                                    UpdateUserRequest(
                                        kodePegawai = user.kodePegawai,
                                        nama = newNama,
                                        email = newEmail
                                    )
                                )
                                if (res.isSuccessful && res.body()?.success == true) {
                                    Toast.makeText(requireContext(), "Data diperbarui", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                    loadAnggota()
                                } else {
                                    Toast.makeText(requireContext(), res.body()?.message ?: "Gagal update", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                dialog.show()
            }
    }

    private fun showDeleteOptions(user: User) {
        val options = arrayOf("Aktifkan", "Nonaktifkan", "Hapus Permanen")

        AlertDialog.Builder(requireContext())
            .setTitle("Kelola Anggota: ${user.nama}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> setStatusUser(user, "Anggota Aktif")
                    1 -> setStatusUser(user, "Nonaktif")
                    2 -> deleteUser(user)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setStatusUser(user: User, status: String) {
        lifecycleScope.launch {
            try {
                val res = api.setUserStatus(
                    SetUserStatusRequest(
                        kodePegawai = user.kodePegawai,
                        statusKeanggotaan = status
                    )
                )
                if (res.isSuccessful && res.body()?.success == true) {
                    Toast.makeText(requireContext(), "Status diubah: $status", Toast.LENGTH_SHORT).show()
                    loadAnggota()
                } else {
                    Toast.makeText(requireContext(), res.body()?.message ?: "Gagal ubah status", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            try {
                val res = api.deleteUser(DeleteUserRequest(kodePegawai = user.kodePegawai))
                if (res.isSuccessful && res.body()?.success == true) {
                    Toast.makeText(requireContext(), "Anggota dihapus", Toast.LENGTH_SHORT).show()
                    loadAnggota()
                } else {
                    Toast.makeText(requireContext(), res.body()?.message ?: "Gagal hapus", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // bikin kode EMPxxx berdasarkan max existing
    private fun generateNextKode(list: List<User>): String {
        var maxNum = 0
        for (u in list) {
            val n = u.kodePegawai.removePrefix("EMP").toIntOrNull() ?: 0
            if (n > maxNum) maxNum = n
        }
        return "EMP" + (maxNum + 1).toString().padStart(3, '0')
    }
}
