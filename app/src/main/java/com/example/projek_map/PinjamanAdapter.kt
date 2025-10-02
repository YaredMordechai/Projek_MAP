package com.example.projek_map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PinjamanAdapter(
    private val items: List<Pinjaman>,
    private val onClick: (Pinjaman) -> Unit
) : RecyclerView.Adapter<PinjamanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJenis: TextView = view.findViewById(R.id.tvJenis)
        val tvJumlah: TextView = view.findViewById(R.id.tvJumlah)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)

        fun bind(pinjaman: Pinjaman) {
            tvJenis.text = pinjaman.jenis
            tvJumlah.text = "Rp ${pinjaman.jumlah}"
            tvStatus.text = pinjaman.status
            tvTanggal.text = pinjaman.tanggal

            itemView.setOnClickListener { onClick(pinjaman) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pinjaman, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
