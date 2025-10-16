package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.TransaksiSimpanan

class TransaksiSimpananAdapter(private val transaksiList: List<TransaksiSimpanan>) :
    RecyclerView.Adapter<TransaksiSimpananAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKode: TextView = itemView.findViewById(R.id.tvKodePegawai)
        val tvJenis: TextView = itemView.findViewById(R.id.tvJenisSimpanan)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlahSimpanan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi_simpanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transaksiList[position]
        holder.tvKode.text = item.kodePegawai
        holder.tvJenis.text = item.jenis
        holder.tvJumlah.text = "Rp %, .0f".format(item.jumlah)
        holder.tvTanggal.text = item.tanggal
    }

    override fun getItemCount() = transaksiList.size
}