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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.PengumumanAddRequest
import com.example.projek_map.api.PengumumanDeleteRequest
import com.example.projek_map.api.PengumumanUpdateRequest
import com.example.projek_map.api.Pengumuman
import com.example.projek_map.utils.AlarmReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class PengumumanFragment : Fragment() {

    private var isAdmin: Boolean = false
    private var rv: RecyclerView? = null
    private var emptyState: TextView? = null
    private var fab: FloatingActionButton? = null
    private lateinit var adapter: PengumumanAdapter

    private val pengumumanItems = mutableListOf<Pengumuman>()

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
                if (judul.isEmpty() || isi.isEmpty()) {
                    Toast.makeText(konteks, "Judul & isi wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                addPengumumanApi(judul, isi)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun loadPengumumanFromApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getPengumuman()
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    pengumumanItems.clear()
                    pengumumanItems.addAll(body.data ?: emptyList())
                    adapter.notifyDataSetChanged()
                    updateEmptyState()
                } else {
                    Toast.makeText(
                        requireContext(),
                        body?.message ?: "Gagal ambil pengumuman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPengumumanApi(judul: String, isi: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.addPengumuman(PengumumanAddRequest(judul, isi))
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val baru = body.data

                    pengumumanItems.add(0, baru)
                    adapter.notifyItemInserted(0)
                    rv?.scrollToPosition(0)
                    updateEmptyState()

                    // Broadcast notifikasi (seperti semula)
                    try {
                        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
                            putExtra("type", "pengumuman_baru")
                            putExtra("title", "Pengumuman Baru")
                            putExtra("message", "${baru.judul} (${baru.tanggal})")
                        }
                        requireContext().sendBroadcast(intent)
                    } catch (_: Throwable) { /* no-op */ }

                } else {
                    Toast.makeText(
                        requireContext(),
                        body?.message ?: "Gagal tambah pengumuman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDialog(item: Pengumuman) {
        if (!isAdmin) return

        val konteks = requireContext()

        val inputJudul = EditText(konteks).apply {
            setText(item.judul)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
        val inputIsi = EditText(konteks).apply {
            setText(item.isi)
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
            .setTitle("Edit Pengumuman")
            .setView(container)
            .setPositiveButton("Simpan") { _, _ ->
                val judulBaru = inputJudul.text.toString().trim()
                val isiBaru = inputIsi.text.toString().trim()
                if (judulBaru.isEmpty() || isiBaru.isEmpty()) {
                    Toast.makeText(konteks, "Judul & isi wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                updatePengumumanApi(item.id, judulBaru, isiBaru)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun updatePengumumanApi(id: Int, judul: String, isi: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.updatePengumuman(PengumumanUpdateRequest(id, judul, isi))
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    val idx = pengumumanItems.indexOfFirst { it.id == id }
                    if (idx >= 0) {
                        pengumumanItems[idx] = pengumumanItems[idx].copy(judul = judul, isi = isi)
                        adapter.notifyItemChanged(idx)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        body?.message ?: "Gagal update pengumuman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmDelete(item: Pengumuman) {
        if (!isAdmin) return
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Pengumuman")
            .setMessage("Yakin hapus: \"${item.judul}\" ?")
            .setPositiveButton("Hapus") { _, _ -> deletePengumumanApi(item.id) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deletePengumumanApi(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.deletePengumuman(PengumumanDeleteRequest(id))
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    val idx = pengumumanItems.indexOfFirst { it.id == id }
                    if (idx >= 0) {
                        pengumumanItems.removeAt(idx)
                        adapter.notifyItemRemoved(idx)
                        updateEmptyState()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        body?.message ?: "Gagal hapus pengumuman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private class PengumumanAdapter(
        private val items: List<Pengumuman>,
        private val isAdmin: Boolean,
        private val onEdit: (Pengumuman) -> Unit,
        private val onDelete: (Pengumuman) -> Unit
    ) : RecyclerView.Adapter<PengumumanAdapter.VH>() {

        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tJudul: TextView = v.findViewById(R.id.tvJudul)
            val tTanggal: TextView = v.findViewById(R.id.tvTanggal)
            val tIsi: TextView = v.findViewById(R.id.tvIsi)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pengumuman, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(h: VH, pos: Int) {
            val p = items[pos]
            h.tJudul.text = p.judul
            h.tTanggal.text = p.tanggal
            h.tIsi.text = p.isi

            if (isAdmin) {
                h.itemView.setOnClickListener { onEdit(p) }
                h.itemView.setOnLongClickListener { onDelete(p); true }
            } else {
                h.itemView.setOnClickListener(null)
                h.itemView.setOnLongClickListener(null)
            }
        }

        override fun getItemCount(): Int = items.size
    }
}
