package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.User

@Suppress("DEPRECATION")
class AnggotaAdapter(
    private val anggotaList: MutableList<User>,
    private val onEdit: (User) -> Unit,
    private val onDeactivate: (User) -> Unit,
    private val onDelete: (User, Int) -> Unit
) : RecyclerView.Adapter<AnggotaAdapter.AnggotaViewHolder>() {

    inner class AnggotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDeactivate: ImageButton = itemView.findViewById(R.id.btnDeactivate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnggotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anggota, parent, false)
        return AnggotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnggotaViewHolder, position: Int) {
        val anggota = anggotaList[position]
        holder.tvNama.text = anggota.nama
        holder.tvEmail.text = anggota.email
        holder.tvStatus.text = anggota.statusKeanggotaan

        holder.btnEdit.setOnClickListener { onEdit(anggota) }
        holder.btnDeactivate.setOnClickListener {
            onDelete(anggota, holder.adapterPosition)
        }
    }

    override fun getItemCount() = anggotaList.size

    fun refreshData(newList: List<User>) {
        val copy = newList.toList()      // <-- penting: bikin salinan
        anggotaList.clear()
        anggotaList.addAll(copy)
        notifyDataSetChanged()
    }
}