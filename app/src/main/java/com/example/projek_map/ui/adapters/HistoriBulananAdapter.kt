package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.HistoriPembayaran

class HistoriBulananAdapter(
    private val historiList: List<HistoriPembayaran>
) : RecyclerView.Adapter<HistoriBulananAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTanggal: TextView = view.findViewById(R.id.txtTanggal)
        val txtJumlah: TextView = view.findViewById(R.id.txtJumlah)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_histori_bulanan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = historiList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historiList[position]
        holder.txtTanggal.text = item.tanggal
        holder.txtJumlah.text = "Rp${item.jumlah}"
        holder.txtStatus.text = item.status
    }
}
