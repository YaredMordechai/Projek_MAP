package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.Pinjaman
import com.google.android.material.button.MaterialButton

class KelolaPinjamanAdapter(
    private val pinjamanList: MutableList<Pinjaman>,
    private val onApprove: (Pinjaman) -> Unit,
    private val onReject: (Pinjaman) -> Unit
) : RecyclerView.Adapter<KelolaPinjamanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaPemohon: TextView = view.findViewById(R.id.tvNamaPemohon)
        val tvKodePegawai: TextView = view.findViewById(R.id.tvKodePegawai)
        val tvJumlahPinjaman: TextView = view.findViewById(R.id.tvJumlahPinjaman)
        val tvStatusPinjaman: TextView = view.findViewById(R.id.tvStatusPinjaman)
        val btnApprove: MaterialButton = view.findViewById(R.id.btnApprove)
        val btnReject: MaterialButton = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pinjaman_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pinjaman = pinjamanList[position]

        holder.tvNamaPemohon.text = pinjaman.kodePegawai
        holder.tvKodePegawai.text = pinjaman.kodePegawai
        holder.tvJumlahPinjaman.text = "Rp %,.0f".format(pinjaman.jumlah.toDouble())
        holder.tvStatusPinjaman.text = pinjaman.status

        holder.btnApprove.setOnClickListener { onApprove(pinjaman) }
        holder.btnReject.setOnClickListener { onReject(pinjaman) }
    }

    override fun getItemCount() = pinjamanList.size

    // 🔹 Tambahan: hapus item by id (agar hilang dari list tanpa reload penuh)
    fun removeItemById(id: Int) {
        val idx = pinjamanList.indexOfFirst { it.id == id }
        if (idx >= 0) {
            pinjamanList.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }

    // 🔹 Tambahan: replace semua data (dipakai saat onResume refresh)
    fun replaceAll(newData: List<Pinjaman>) {
        pinjamanList.clear()
        pinjamanList.addAll(newData)
        notifyDataSetChanged()
    }
}
