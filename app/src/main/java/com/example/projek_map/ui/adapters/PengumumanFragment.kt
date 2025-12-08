package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pengumuman
import com.example.projek_map.network.ApiClient
import com.example.projek_map.utils.AlarmReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PengumumanFragment : Fragment() {

    private var isAdmin: Boolean = false
    private var rv: RecyclerView? = null
    private var emptyState: TextView? = null
    private var fab: FloatingActionButton? = null
    private lateinit var adapter: PengumumanAdapter
    private val items = mutableListOf<Pengumuman>()

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
        adapter = PengumumanAdapter(items)
        rv?.adapter = adapter

        fab?.visibility = if (isAdmin) VISIBLE else GONE
        fab?.setOnClickListener { showAddAnnouncementDialog() }

        loadPengumuman()
        return v
    }

    private fun loadPengumuman() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.getPengumuman()
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        items.clear()
                        items.addAll(res.body()?.pengumuman ?: emptyList())
                        adapter.notifyDataSetChanged()
                        updateEmptyState()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal memuat pengumuman",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error koneksi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateEmptyState() {
        if (items.isEmpty()) {
            emptyState?.visibility = VISIBLE
            rv?.visibility = GONE
        } else {
            emptyState?.visibility = GONE
            rv?.visibility = VISIBLE
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
                if (judul.isEmpty() || isi.isEmpty()) return@setPositiveButton

                tambahPengumuman(judul, isi)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun tambahPengumuman(judul: String, isi: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = ApiClient.instance.addPengumuman(judul, isi)
                withContext(Dispatchers.Main) {
                    if (res.isSuccessful && res.body()?.success == true) {
                        val baru = res.body()?.pengumuman
                        if (baru != null) {
                            items.add(0, baru)
                            adapter.notifyItemInserted(0)
                            rv?.scrollToPosition(0)
                            updateEmptyState()

                            // Broadcast notifikasi (pengumuman baru)
                            try {
                                val intent = Intent(
                                    requireContext(),
                                    AlarmReceiver::class.java
                                ).apply {
                                    putExtra("type", "pengumuman_baru")
                                    putExtra("title", "Pengumuman Baru")
                                    putExtra("message", "${baru.judul} (${baru.tanggal})")
                                }
                                requireContext().sendBroadcast(intent)
                            } catch (_: Throwable) { }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            res.body()?.message ?: "Gagal menambah pengumuman",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error koneksi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private class PengumumanAdapter(
        private val items: List<Pengumuman>
    ) : RecyclerView.Adapter<PengumumanAdapter.VH>() {

        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tJudul: TextView = v.findViewById(R.id.tvJudul)
            val tTanggal: TextView = v.findViewById(R.id.tvTanggal)
            val tIsi: TextView = v.findViewById(R.id.tvIsi)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pengumuman, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(h: VH, pos: Int) {
            val p = items[pos]
            h.tJudul.text = p.judul
            h.tTanggal.text = p.tanggal
            h.tIsi.text = p.isi
        }

        override fun getItemCount(): Int = items.size
    }
}
