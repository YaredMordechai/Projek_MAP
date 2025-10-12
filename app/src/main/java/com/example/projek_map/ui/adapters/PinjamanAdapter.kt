package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pinjaman

class PinjamanAdapter(
    private val pinjamanList: List<Pinjaman>,
    private val onItemClick: (Pinjaman) -> Unit
) : RecyclerView.Adapter<PinjamanAdapter.PinjamanViewHolder>() {

    inner class PinjamanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlahPinjaman)
        val tvTenor: TextView = itemView.findViewById(R.id.tvTenorPinjaman)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusPinjaman)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pinjaman, parent, false)
        return PinjamanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PinjamanViewHolder, position: Int) {
        val pinjaman = pinjamanList[position]
        holder.tvJumlah.text = "Rp ${pinjaman.jumlah}"
        holder.tvTenor.text = "${pinjaman.tenor} bulan"
        holder.tvStatus.text = pinjaman.status

        // 🔹 Warna status dinamis
        val context = holder.itemView.context
        val statusColor = when (pinjaman.status.lowercase()) {
            "disetujui" -> R.color.green_600
            "proses" -> R.color.orange_600
            "ditolak" -> R.color.red_600
            else -> android.R.color.darker_gray
        }
        holder.tvStatus.setTextColor(ContextCompat.getColor(context, statusColor))

        holder.itemView.setOnClickListener {
            onItemClick(pinjaman)
        }
    }

    override fun getItemCount(): Int = pinjamanList.size
}