package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pengumuman

class PengumumanAdapter(
    private val items: List<Pengumuman>,
    private val onClick: ((Pengumuman) -> Unit)? = null
) : RecyclerView.Adapter<PengumumanAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvJudul: TextView = v.findViewById(R.id.tvJudul)
        val tvTanggal: TextView = v.findViewById(R.id.tvTanggal)
        val tvIsi: TextView = v.findViewById(R.id.tvIsi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengumuman, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.tvJudul.text = it.judul
        holder.tvTanggal.text = it.tanggal
        holder.tvIsi.text = it.isi
        holder.itemView.setOnClickListener { _ -> onClick?.invoke(it) }
    }

    override fun getItemCount(): Int = items.size
}
