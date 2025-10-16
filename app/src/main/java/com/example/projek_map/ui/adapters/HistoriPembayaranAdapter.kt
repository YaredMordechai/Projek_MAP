package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.HistoriPembayaran

class HistoriPembayaranAdapter(
    private val data: List<HistoriPembayaran>
) : RecyclerView.Adapter<HistoriPembayaranAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvJumlah: TextView = view.findViewById(R.id.tvJumlah)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_histori_pembayaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pembayaran = data[position]
        holder.tvTanggal.text = pembayaran.tanggal
        holder.tvJumlah.text = "Rp ${pembayaran.jumlah}"
        // 📷 Jika ada bukti pembayarannya, tambahkan tanda pada status
        val statusText = if (!pembayaran.buktiPembayaranUri.isNullOrEmpty()) {
            "${pembayaran.status}  •  Bukti: ✓"
        } else {
            pembayaran.status
        }
        holder.tvStatus.text = statusText
    }

    override fun getItemCount() = data.size
}
