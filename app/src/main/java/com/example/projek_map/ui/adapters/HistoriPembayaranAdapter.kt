package com.example.projek_map.ui.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val imgBukti: ImageView = view.findViewById(R.id.imgBuktiPembayaran)
        val imgPaymentIcon: ImageView = view.findViewById(R.id.imgPaymentIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_histori_pembayaran, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pembayaran = data[position]
        holder.tvTanggal.text = pembayaran.tanggal
        holder.tvJumlah.text = "Rp ${pembayaran.jumlah}"
        holder.txtStatus.text = pembayaran.status

        // Tampilkan bukti pembayaran jika ada URI-nya
        if (!pembayaran.buktiPembayaranUri.isNullOrEmpty()) {
            holder.imgBukti.visibility = View.VISIBLE
            try {
                holder.imgBukti.setImageURI(Uri.parse(pembayaran.buktiPembayaranUri))
            } catch (_: Exception) {
                holder.imgBukti.setImageResource(R.drawable.ic_bukti)
            }
        } else {
            holder.imgBukti.visibility = View.GONE
        }

        // Tentukan warna status
        when {
            pembayaran.status.contains("Denda", true) -> {
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.red_600))
                holder.imgPaymentIcon.setColorFilter(holder.itemView.context.getColor(R.color.red_400))
            }
            pembayaran.status.contains("Lunas", true) -> {
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.green_700))
                holder.imgPaymentIcon.setColorFilter(holder.itemView.context.getColor(R.color.green_700))
            }
            else -> {
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.green_900))
                holder.imgPaymentIcon.setColorFilter(holder.itemView.context.getColor(R.color.green_400))
            }
        }

        val statusText = if (!pembayaran.buktiPembayaranUri.isNullOrEmpty()) {
            "${pembayaran.status}  •  Bukti: ✓"
        } else pembayaran.status

        holder.tvStatus.text = statusText
    }

    override fun getItemCount() = data.size
}
