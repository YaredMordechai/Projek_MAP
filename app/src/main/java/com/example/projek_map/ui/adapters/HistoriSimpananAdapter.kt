package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.HistoriSimpanan

class HistoriSimpananAdapter(
    private val list: List<HistoriSimpanan>
) : RecyclerView.Adapter<HistoriSimpananAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvJenis: TextView = itemView.findViewById(R.id.tvJenis)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_histori_simpanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.tvTanggal.text = data.tanggal
        holder.tvJenis.text = data.jenis
        holder.tvJumlah.text = "Rp ${String.format("%,.0f", data.jumlah).replace(',', '.')}"
        holder.tvKeterangan.text = data.keterangan

        val color = if (data.jumlah >= 0)
            R.color.green_600 else R.color.red_600
        holder.tvJumlah.setTextColor(ContextCompat.getColor(holder.itemView.context, color))
    }

    override fun getItemCount(): Int = list.size
}
