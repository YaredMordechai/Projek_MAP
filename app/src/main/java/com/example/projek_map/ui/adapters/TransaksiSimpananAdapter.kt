package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.TransaksiSimpanan
import java.text.NumberFormat
import java.util.Locale

@Suppress("DEPRECATION")
class TransaksiSimpananAdapter(
    private val transaksiList: MutableList<TransaksiSimpanan>,
    private val onEdit: (TransaksiSimpanan, Int) -> Unit = { _, _ -> },
    private val onDelete: (TransaksiSimpanan, Int) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<TransaksiSimpananAdapter.ViewHolder>() {

    companion object {
        private const val MENU_EDIT = 1001
        private const val MENU_DELETE = 1002
    }

    private val rupiah: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKode: TextView = itemView.findViewById(R.id.tvKodePegawai)
        val tvJenis: TextView = itemView.findViewById(R.id.tvJenisSimpanan)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlahSimpanan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalTransaksi)

        init {
            // Long press untuk memunculkan menu Edit/Hapus
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnLongClickListener true

                val item = transaksiList[position]
                val popup = PopupMenu(itemView.context, itemView)

                // Pakai ID konstanta → tidak butuh ids.xml
                popup.menu.add(0, MENU_EDIT, 0, "Edit")
                popup.menu.add(0, MENU_DELETE, 1, "Hapus")

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        MENU_EDIT -> {
                            onEdit(item, position)
                            true
                        }
                        MENU_DELETE -> {
                            onDelete(item, position)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
                true
            }
        }
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
        holder.tvJumlah.text = rupiah.format(item.jumlah) // contoh: Rp12.345
        holder.tvTanggal.text = item.tanggal
    }

    override fun getItemCount(): Int = transaksiList.size

    // ===== Optional helpers =====

    fun removeAt(position: Int) {
        if (position in 0 until transaksiList.size) {
            transaksiList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateAt(position: Int, newItem: TransaksiSimpanan) {
        if (position in 0 until transaksiList.size) {
            transaksiList[position] = newItem
            notifyItemChanged(position)
        }
    }

    fun replaceAll(newList: List<TransaksiSimpanan>) {
        transaksiList.clear()
        transaksiList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): TransaksiSimpanan? =
        if (position in 0 until transaksiList.size) transaksiList[position] else null
}
