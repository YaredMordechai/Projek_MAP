package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.api.Simpanan
import com.example.projek_map.databinding.ItemSimpananBinding

class SimpananAdapter(private val listSimpanan: List<Simpanan>) :
    RecyclerView.Adapter<SimpananAdapter.SimpananViewHolder>() {

    inner class SimpananViewHolder(val binding: ItemSimpananBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpananViewHolder {
        val binding = ItemSimpananBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SimpananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimpananViewHolder, position: Int) {
        val simpanan = listSimpanan[position]

        // Gabungkan semua jenis simpanan dalam satu card
        val simpananDetail = """
            Simpanan Pokok   : Rp ${simpanan.simpananPokok}
            Simpanan Wajib   : Rp ${simpanan.simpananWajib}
            Simpanan Sukarela: Rp ${simpanan.simpananSukarela}
        """.trimIndent()

        holder.binding.apply {
            tvJenisSimpanan.text = "Kode Pegawai: ${simpanan.kodePegawai}"
            tvJumlahSimpanan.text = simpananDetail
            tvTanggalSimpanan.text = "—"
        }
    }

    override fun getItemCount(): Int = listSimpanan.size
}