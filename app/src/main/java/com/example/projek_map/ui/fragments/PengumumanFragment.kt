package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.PengumumanAddRequest
import com.example.projek_map.api.PengumumanDeleteRequest
import com.example.projek_map.api.PengumumanUpdateRequest
import com.example.projek_map.api.Pengumuman
import com.example.projek_map.data.PengumumanRepository
import com.example.projek_map.ui.adapters.PengumumanAdapter
import com.example.projek_map.ui.viewmodels.PengumumanViewModel
import com.example.projek_map.utils.AlarmReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PengumumanFragment : Fragment() {

    private var isAdmin: Boolean = false
    private var rv: RecyclerView? = null
    private var emptyState: TextView? = null
    private var fab: FloatingActionButton? = null
    private lateinit var adapter: PengumumanAdapter

    private val pengumumanItems = mutableListOf<Pengumuman>()

    private lateinit var viewModel: PengumumanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_pengumuman, container, false)

        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        rv = v.findViewById(R.id.rvPengumuman)
        emptyState = v.findViewById(R.id.tvEmptyState)
        fab = v.findViewById(R.id.fabAddAnnouncement)

        rv?.layoutManager = LinearLayoutManager(requireContext())
        adapter = PengumumanAdapter(
            pengumumanItems,
            isAdmin,
            onEdit = { item -> showEditDialog(item) },
            onDelete = { item -> confirmDelete(item) }
        )
        rv?.adapter = adapter

        fab?.visibility = if (isAdmin) View.VISIBLE else View.GONE
        fab?.setOnClickListener { showAddAnnouncementDialog() }

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PengumumanViewModel(PengumumanRepository()) as T
            }
        })[PengumumanViewModel::class.java]

        viewModel.items.observe(viewLifecycleOwner) { list ->
            pengumumanItems.clear()
            pengumumanItems.addAll(list)
            adapter.notifyDataSetChanged()
            updateEmptyState()
        }

        viewModel.toast.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }

        updateEmptyState()
        loadPengumumanFromApi()

        return v
    }

    private fun updateEmptyState() {
        if (pengumumanItems.isEmpty()) {
            emptyState?.visibility = View.VISIBLE
            rv?.visibility = View.GONE
        } else {
            emptyState?.visibility = View.GONE
            rv?.visibility = View.VISIBLE
        }
    }

    private fun showAddAnnouncementDialog() {
        val konteks = requireContext()

        val inputJudul = EditText(konteks).apply {
            hint = "Judul pengumuman"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
        val inputIsi = EditText(konteks).apply {
            hint = "Isi pengumuman"
            minLines = 3
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }

        val container = LinearLayoutCompat(konteks).apply {
            orientation = LinearLayoutCompat.VERTICAL
            setPadding(48, 24, 48, 0)
            addView(inputJudul)
            addView(inputIsi)
        }

        AlertDialog.Builder(konteks)
            .setTitle("Tambah Pengumuman")
            .setView(container)
            .setPositiveButton("Simpan") { _, _ ->
                val judul = inputJudul.text.toString().trim()
                val isi = inputIsi.text.toString().trim()

                if (judul.isBlank() || isi.isBlank()) {
                    Toast.makeText(konteks, "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // ✅ FIX DI SINI
                val req = PengumumanAddRequest(judul = judul, isi = isi)
                viewModel.add(req)

                try {
                    val intent = Intent(konteks, AlarmReceiver::class.java)
                    konteks.sendBroadcast(intent)
                } catch (_: Exception) { }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(item: Pengumuman) {
        val konteks = requireContext()

        val inputJudul = EditText(konteks).apply {
            hint = "Judul pengumuman"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            setText(item.judul ?: "")
        }
        val inputIsi = EditText(konteks).apply {
            hint = "Isi pengumuman"
            minLines = 3
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            setText(item.isi ?: "")
        }

        val container = LinearLayoutCompat(konteks).apply {
            orientation = LinearLayoutCompat.VERTICAL
            setPadding(48, 24, 48, 0)
            addView(inputJudul)
            addView(inputIsi)
        }

        AlertDialog.Builder(konteks)
            .setTitle("Edit Pengumuman")
            .setView(container)
            .setPositiveButton("Update") { _, _ ->
                val judul = inputJudul.text.toString().trim()
                val isi = inputIsi.text.toString().trim()

                if (judul.isBlank() || isi.isBlank()) {
                    Toast.makeText(konteks, "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val req = PengumumanUpdateRequest(
                    id = item.id,
                    judul = judul,
                    isi = isi
                )
                viewModel.update(req)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun confirmDelete(item: Pengumuman) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Pengumuman")
            .setMessage("Yakin ingin menghapus pengumuman ini?")
            .setPositiveButton("Hapus") { _, _ ->
                val req = PengumumanDeleteRequest(id = item.id)
                viewModel.delete(req)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun loadPengumumanFromApi() {
        viewModel.load()
    }
}
