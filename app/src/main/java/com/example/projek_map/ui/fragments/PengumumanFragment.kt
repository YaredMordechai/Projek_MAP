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
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.Pengumuman
import com.example.projek_map.utils.AlarmReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PengumumanFragment : Fragment() {

    private var isAdmin: Boolean = false
    private var rv: RecyclerView? = null
    private var emptyState: TextView? = null
    private var fab: FloatingActionButton? = null
    private lateinit var adapter: PengumumanAdapter

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
        adapter = PengumumanAdapter(DummyUserData.pengumumanList)
        rv?.adapter = adapter

        updateEmptyState()

        fab?.visibility = if (isAdmin) View.VISIBLE else View.GONE
        fab?.setOnClickListener { showAddAnnouncementDialog() }

        return v
    }

    private fun updateEmptyState() {
        if (DummyUserData.pengumumanList.isEmpty()) {
            emptyState?.visibility = View.VISIBLE
            rv?.visibility = View.GONE
        } else {
            emptyState?.visibility = View.GONE
            rv?.visibility = View.VISIBLE
        }
    }

    private fun showAddAnnouncementDialog() {
        // Dialog sederhana tanpa layout terpisah (agar minim resiko crash)
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

                val baru = DummyUserData.addPengumuman(judul, isi)
                adapter.notifyItemInserted(0)
                rv?.scrollToPosition(0)
                updateEmptyState()

                // Broadcast notifikasi untuk semua device (mode sederhana)
                try {
                    val intent = Intent(konteks, AlarmReceiver::class.java).apply {
                        putExtra("type", "pengumuman_baru")
                        putExtra("title", "Pengumuman Baru")
                        putExtra("message", "${baru.judul} (${baru.tanggal})")
                    }
                    konteks.sendBroadcast(intent)
                } catch (_: Throwable) { /* no-op */ }
            }
            .setNegativeButton("Batal", null)
            .show()
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
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pengumuman, parent, false)
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