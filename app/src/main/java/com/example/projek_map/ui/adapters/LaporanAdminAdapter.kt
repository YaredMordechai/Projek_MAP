package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.ui.fragments.LaporanAdminFragment
import java.text.NumberFormat
import java.util.Locale

class LaporanAdminAdapter(
    private val data: MutableList<LaporanAdminFragment.UserSummary>,
    private val onItemClick: (LaporanAdminFragment.UserSummary) -> Unit
) : RecyclerView.Adapter<LaporanAdminAdapter.VH>() {

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvNama: TextView = v.findViewById(R.id.tvNamaAnggota)
        val tvKode: TextView = v.findViewById(R.id.tvKodeAnggota)
        val tvSimpanan: TextView = v.findViewById(R.id.tvTotalSimpananAnggota)
        val tvPinjaman: TextView = v.findViewById(R.id.tvTotalPinjamanAnggota)
        val tvStatus: TextView = v.findViewById(R.id.tvStatusAnggota)
        val btnDetail: Button = v.findViewById(R.id.btnDetailAnggota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_anggota_summary, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val item = data[position]                   // <- ikat item data di sini
        h.tvNama.text = item.user.nama
        h.tvKode.text = item.user.kodePegawai
        h.tvStatus.text = item.user.statusKeanggotaan
        h.tvSimpanan.text = rupiah.format(item.totalSimpanan)
        h.tvPinjaman.text = rupiah.format(item.totalPinjamanAktif)

        h.btnDetail.setOnClickListener {
            onItemClick(item)                      // <- kirim item, bukan View
        }
    }

    override fun getItemCount() = data.size

    fun replaceAll(newList: List<LaporanAdminFragment.UserSummary>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }
}
