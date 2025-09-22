package com.example.projek_map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PinjamanAdapter(
    private val items: List<Pinjaman>,
    private val onClick: ((Pinjaman) -> Unit)? = null
) : RecyclerView.Adapter<PinjamanAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJenis: TextView = itemView.findViewById(R.id.tvJenisPinjaman)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlahPinjaman)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusPinjaman)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pinjaman, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.tvJenis.text = p.jenis
        holder.tvJumlah.text = "Jumlah: Rp ${formatRupiah(p.jumlah)}"
        holder.tvStatus.text = "Status: ${p.status}"

        holder.itemView.setOnClickListener { onClick?.invoke(p) }
    }

    override fun getItemCount(): Int = items.size

    private fun formatRupiah(value: Long): String {
        // Format sederhana: ribuan menggunakan titik (1.234.567)
        return String.format("%,d", value).replace(',', '.')
    }
}
