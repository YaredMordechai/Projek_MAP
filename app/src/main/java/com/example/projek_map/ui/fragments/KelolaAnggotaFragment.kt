package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.AddUserRequest
import com.example.projek_map.api.DeleteUserRequest
import com.example.projek_map.api.SetUserStatusRequest
import com.example.projek_map.api.UpdateUserRequest
import com.example.projek_map.api.User
import com.example.projek_map.data.AnggotaRepository
import com.example.projek_map.ui.adapters.AnggotaAdapter
import com.example.projek_map.ui.viewmodels.AnggotaAction
import com.example.projek_map.ui.viewmodels.KelolaAnggotaViewModel
import com.google.android.material.button.MaterialButton

class KelolaAnggotaFragment : Fragment() {

    private lateinit var rvAnggota: RecyclerView
    private lateinit var btnTambahAnggota: MaterialButton
    private lateinit var adapter: AnggotaAdapter

    // MVVM: data list tetap ada di Fragment biar struktur kamu sama,
    // tapi sumber kebenarannya dari ViewModel (observer).
    private val anggotaList = mutableListOf<User>()

    private lateinit var viewModel: KelolaAnggotaViewModel

    // buat dismiss dialog hanya saat sukses (sesuai perilaku kamu sebelumnya)
    private var pendingDialog: AlertDialog? = null
    private var pendingDialogAction: AnggotaAction? = null

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

        // init ViewModel (tanpa “framework injection”, biar simpel dan nggak drama)
        val api = ApiClient.apiService
        val repo = AnggotaRepository(api)
        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(KelolaAnggotaViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return KelolaAnggotaViewModel(repo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        )[KelolaAnggotaViewModel::class.java]

        setupObservers()

        btnTambahAnggota.setOnClickListener { showAddDialog() }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadAnggota()
    }

    private fun setupObservers() {
        viewModel.anggotaList.observe(viewLifecycleOwner) { data ->
            anggotaList.clear()
            anggotaList.addAll(data.orEmpty())
            adapter.refreshData(anggotaList)
        }

        viewModel.actionResult.observe(viewLifecycleOwner) { event ->
            val result = event.getContentIfNotHandled() ?: return@observe
            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()

            // kalau sedang ada dialog add/edit, dismiss hanya kalau sukses dan aksinya match
            if (result.success && pendingDialog != null && pendingDialogAction == result.action) {
                pendingDialog?.dismiss()
                pendingDialog = null
                pendingDialogAction = null
            }
        }
    }

    private fun loadAnggota() {
        viewModel.loadAnggota()
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

                        // tandai dialog ini untuk di-dismiss kalau sukses ADD
                        pendingDialog = dialog
                        pendingDialogAction = AnggotaAction.ADD

                        viewModel.addUser(
                            AddUserRequest(
                                kodePegawai = nextKode,
                                nama = nama,
                                email = email,
                                password = "1234",
                                statusKeanggotaan = "Anggota Aktif"
                            )
                        )
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

                        pendingDialog = dialog
                        pendingDialogAction = AnggotaAction.UPDATE

                        viewModel.updateUser(
                            UpdateUserRequest(
                                kodePegawai = user.kodePegawai,
                                nama = newNama,
                                email = newEmail
                            )
                        )
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
        viewModel.setStatusUser(
            SetUserStatusRequest(
                kodePegawai = user.kodePegawai,
                statusKeanggotaan = status
            )
        )
    }

    private fun deleteUser(user: User) {
        viewModel.deleteUser(DeleteUserRequest(kodePegawai = user.kodePegawai))
    }

    // bikin kode EMPxxx berdasarkan max existing (tetap kamu punya di Fragment)
    private fun generateNextKode(list: List<User>): String {
        var maxNum = 0
        for (u in list) {
            val n = u.kodePegawai.removePrefix("EMP").toIntOrNull() ?: 0
            if (n > maxNum) maxNum = n
        }
        return "EMP" + (maxNum + 1).toString().padStart(3, '0')
    }
}
