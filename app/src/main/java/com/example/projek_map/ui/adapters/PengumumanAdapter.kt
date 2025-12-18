package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.Pengumuman

class PengumumanAdapter(
    private val items: List<Pengumuman>,
    private val isAdmin: Boolean,
    private val onEdit: (Pengumuman) -> Unit,
    private val onDelete: (Pengumuman) -> Unit
) : RecyclerView.Adapter<PengumumanAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tJudul: TextView = v.findViewById(R.id.tvJudul)
        val tTanggal: TextView = v.findViewById(R.id.tvTanggal)
        val tIsi: TextView = v.findViewById(R.id.tvIsi)

        val adminActions: LinearLayout = v.findViewById(R.id.layoutAdminActions)
        val btnEdit: Button = v.findViewById(R.id.btnEdit)
        val btnDelete: Button = v.findViewById(R.id.btnDelete)
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
            h.adminActions.visibility = View.VISIBLE

            h.btnEdit.setOnClickListener { onEdit(p) }
            h.btnDelete.setOnClickListener { onDelete(p) }

            // optional: kalau kamu mau card click juga bisa edit
            h.itemView.setOnClickListener(null)
            h.itemView.setOnLongClickListener(null)
        } else {
            h.adminActions.visibility = View.GONE

            // bersihin listener biar aman (recycle view)
            h.btnEdit.setOnClickListener(null)
            h.btnDelete.setOnClickListener(null)
            h.itemView.setOnClickListener(null)
            h.itemView.setOnLongClickListener(null)
        }
    }

    override fun getItemCount(): Int = items.size
}
