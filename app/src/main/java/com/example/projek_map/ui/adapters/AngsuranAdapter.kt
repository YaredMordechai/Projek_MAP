package com.example.projek_map.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.AngsuranItem
import java.util.Locale

@Suppress("DEPRECATION")
class AngsuranAdapter(
    private val items: MutableList<AngsuranItem> = mutableListOf()
) : RecyclerView.Adapter<AngsuranAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvPeriode: TextView = v.findViewById(R.id.tvPeriode)
        val tvPokok: TextView = v.findViewById(R.id.tvPokok)
        val tvBunga: TextView = v.findViewById(R.id.tvBunga)
        val tvTotal: TextView = v.findViewById(R.id.tvTotal)
        val tvSisa: TextView = v.findViewById(R.id.tvSisa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_angsuran, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val it = items[position]
        h.tvPeriode.text = it.periode.toString()
        h.tvPokok.text = formatRupiah(it.pokok)
        h.tvBunga.text = formatRupiah(it.bunga)
        h.tvTotal.text = formatRupiah(it.total)
        h.tvSisa.text = formatRupiah(it.sisaPokok)
    }

    override fun getItemCount() = items.size

    fun setData(newItems: List<AngsuranItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun formatRupiah(n: Int): String {
        return String.format(Locale("id","ID"), "Rp %,d", n).replace(',', '.')
    }
}
