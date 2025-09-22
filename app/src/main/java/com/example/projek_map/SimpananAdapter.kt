package com.example.projek_map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.databinding.ItemSimpananBinding

class SimpananAdapter(private val listSimpanan: List<Simpanan>) :
    RecyclerView.Adapter<SimpananAdapter.SimpananViewHolder>() {

    inner class SimpananViewHolder(val binding: ItemSimpananBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpananViewHolder {
        val binding = ItemSimpananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimpananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimpananViewHolder, position: Int) {
        val simpanan = listSimpanan[position]
        holder.binding.tvJenisSimpanan.text = simpanan.jenis
        holder.binding.tvJumlahSimpanan.text = simpanan.jumlah
        holder.binding.tvTanggalSimpanan.text = simpanan.tanggal
    }

    override fun getItemCount(): Int = listSimpanan.size
}
