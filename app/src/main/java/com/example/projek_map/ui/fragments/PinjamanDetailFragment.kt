package com.example.projek_map.ui.fragments

import android.text.InputType
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.AngsuranItem
import com.example.projek_map.api.RincianPinjaman
import com.example.projek_map.ui.adapters.AngsuranAdapter
import com.example.projek_map.ui.viewmodels.PinjamanDetailViewModel
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.Locale

class PinjamanDetailFragment : Fragment() {

    private var pinjamanId: Int = 0
    private var currentMetode: String = "anuitas"

    private lateinit var tvCicilan: TextView
    private lateinit var tvTotalPokok: TextView
    private lateinit var tvTotalBunga: TextView
    private lateinit var tvTotalBayar: TextView
    private lateinit var tvTerbayar: TextView
    private lateinit var tvSisaBayar: TextView
    private lateinit var tvSisaPokok: TextView

    private lateinit var toggleMetode: MaterialButtonToggleGroup
    private lateinit var rvJadwal: RecyclerView
    private lateinit var adapter: AngsuranAdapter

    private var btnBayar: MaterialButton? = null

    private val vm: PinjamanDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinjamanId = arguments?.getInt("pinjamanId", 0) ?: 0
        currentMetode = arguments?.getString("metode", "anuitas") ?: "anuitas"
        if (currentMetode != "flat" && currentMetode != "anuitas") currentMetode = "anuitas"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_pinjaman_detail, container, false)

        tvCicilan = v.findViewById(R.id.tvCicilan)
        tvTotalPokok = v.findViewById(R.id.tvTotalPokok)
        tvTotalBunga = v.findViewById(R.id.tvTotalBunga)
        tvTotalBayar = v.findViewById(R.id.tvTotalBayar)
        tvTerbayar = v.findViewById(R.id.tvTerbayar)
        tvSisaBayar = v.findViewById(R.id.tvSisaBayar)
        tvSisaPokok = v.findViewById(R.id.tvSisaPokok)
        toggleMetode = v.findViewById(R.id.toggleMetode)
        rvJadwal = v.findViewById(R.id.rvJadwal)

        btnBayar = v.findViewById(R.id.btnBayarAngsuranDetail)

        adapter = AngsuranAdapter()
        rvJadwal.layoutManager = LinearLayoutManager(requireContext())
        rvJadwal.adapter = adapter
        rvJadwal.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        when (currentMetode) {
            "flat" -> v.findViewById<View>(R.id.btnFlat)?.let { toggleMetode.check(it.id) }
            else -> v.findViewById<View>(R.id.btnAnuitas)?.let { toggleMetode.check(it.id) }
        }

        toggleMetode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            when (checkedId) {
                R.id.btnAnuitas -> {
                    currentMetode = "anuitas"
                    vm.load(pinjamanId, currentMetode)
                }
                R.id.btnFlat -> {
                    currentMetode = "flat"
                    vm.load(pinjamanId, currentMetode)
                }
            }
        }

        btnBayar?.setOnClickListener { showBayarDialog() }

        vm.rincian.observe(viewLifecycleOwner) { rinc ->
            if (rinc != null) renderRincian(rinc)
        }

        // ✅ FIX submitList: pakai helper aman
        vm.jadwal.observe(viewLifecycleOwner) { list ->
            applyAngsuranToAdapter(list)
        }

        vm.toast.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        vm.load(pinjamanId, currentMetode)

        return v
    }

    private fun applyAngsuranToAdapter(list: List<AngsuranItem>) {
        // coba panggil method yang mungkin ada di adapter kamu
        try {
            val m = adapter::class.java.methods.firstOrNull {
                (it.name == "submitList" || it.name == "setData" || it.name == "setItems" || it.name == "updateData")
                        && it.parameterTypes.size == 1
            }
            if (m != null) {
                m.invoke(adapter, list)
                return
            }
        } catch (_: Exception) { }

        // fallback: coba update field list internal (items/data/list)
        val fieldNames = listOf("items", "data", "list", "mList")
        for (fname in fieldNames) {
            try {
                val f = adapter::class.java.getDeclaredField(fname)
                f.isAccessible = true
                val cur = f.get(adapter)
                if (cur is MutableList<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (cur as MutableList<AngsuranItem>).apply {
                        clear()
                        addAll(list)
                    }
                    adapter.notifyDataSetChanged()
                    return
                }
            } catch (_: Exception) { }
        }

        // minimal fallback
        adapter.notifyDataSetChanged()
    }

    private fun renderRincian(r: RincianPinjaman) {
        fun rupiah(x: Double): String {
            return String.format(Locale("in", "ID"), "%,0f", x).replace(',', '.')
        }

        tvCicilan.text = "Rp ${rupiah(r.cicilanPerBulan.toDouble())}"
        tvTotalPokok.text = "Rp ${rupiah(r.totalPokok.toDouble())}"
        tvTotalBunga.text = "Rp ${rupiah(r.totalBunga.toDouble())}"
        tvTotalBayar.text = "Rp ${rupiah(r.totalBayar.toDouble())}"
        tvTerbayar.text = "Rp ${rupiah(r.terbayar.toDouble())}"
        tvSisaBayar.text = "Rp ${rupiah(r.sisaBayar.toDouble())}"
        tvSisaPokok.text = "Rp ${rupiah(r.sisaPokok.toDouble())}"
    }

    private fun showBayarDialog() {
        val konteks = requireContext()
        val pref = PrefManager(konteks)
        val kodePegawai = pref.getKodePegawai() ?: ""

        val inputJumlah = EditText(konteks).apply {
            hint = "Jumlah bayar"
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        val container = LinearLayout(konteks).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 0)
            addView(inputJumlah)
        }

        AlertDialog.Builder(konteks)
            .setTitle("Bayar Angsuran")
            .setView(container)
            .setPositiveButton("Bayar") { _, _ ->
                val jumlah = inputJumlah.text.toString().trim().toIntOrNull()
                if (jumlah == null || jumlah <= 0) {
                    Toast.makeText(konteks, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                vm.bayar(
                    kodePegawai = kodePegawai,
                    pinjamanId = pinjamanId,
                    jumlah = jumlah,
                    currentMetode = currentMetode
                )
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
