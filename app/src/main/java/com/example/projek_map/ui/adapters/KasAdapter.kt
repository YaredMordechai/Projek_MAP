package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.KasTransaksi
 // <-- ini akan resolve karena typealias
import java.text.NumberFormat
import java.util.Locale

class KasAdapter(
    private val data: MutableList<KasTransaksi>,
    private val onEdit: (KasTransaksi, Int) -> Unit = { _, _ -> },
    private val onDelete: (KasTransaksi, Int) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<KasAdapter.VH>() {

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTanggal: TextView = v.findViewById(R.id.tvTanggalKas)
        val tvJenis: TextView = v.findViewById(R.id.tvJenisKas)
        val tvKategori: TextView = v.findViewById(R.id.tvKategoriKas)
        val tvDeskripsi: TextView = v.findViewById(R.id.tvDeskripsiKas)
        val tvJumlah: TextView = v.findViewById(R.id.tvJumlahKas)

        init {
            v.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION) return@setOnLongClickListener true
                val item = data[pos]
                val popup = PopupMenu(v.context, v)
                val ID_EDIT = 1001
                val ID_DELETE = 1002
                popup.menu.add(0, ID_EDIT, 0, "Edit")
                popup.menu.add(0, ID_DELETE, 1, "Hapus")
                popup.setOnMenuItemClickListener { mi ->
                    when (mi.itemId) {
                        ID_EDIT -> { onEdit(item, pos); true }
                        ID_DELETE -> { onDelete(item, pos); true }
                        else -> false
                    }
                }
                popup.show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kas_transaksi, parent, false) // pastikan pakai file kamu
        return VH(view)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val it = data[position]
        h.tvTanggal.text = it.tanggal
        h.tvJenis.text = it.jenis
        h.tvKategori.text = it.kategori
        h.tvDeskripsi.text = it.deskripsi
        h.tvJumlah.text = rupiah.format(it.jumlah)

        val color = if (it.jenis.equals("Masuk", true))
            h.itemView.context.getColor(R.color.green_700)
        else
            h.itemView.context.getColor(R.color.red_600)
        h.tvJumlah.setTextColor(color)
    }

    override fun getItemCount() = data.size

    fun replaceAll(newList: List<KasTransaksi>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        if (position in 0 until data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateAt(position: Int, newItem: KasTransaksi) {
        if (position in 0 until data.size) {
            data[position] = newItem
            notifyItemChanged(position)
        }
    }
}
