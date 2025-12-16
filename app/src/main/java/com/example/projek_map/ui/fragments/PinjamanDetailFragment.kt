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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.RincianPinjaman
import com.example.projek_map.data.AngsuranItem
import com.example.projek_map.data.PinjamanRepository
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.launch
import java.util.Locale

class PinjamanDetailFragment : Fragment() {

    private var pinjamanId: Int = 0
    private var currentMetode: String = "anuitas" // nambah: track metode aktif

    private lateinit var tvCicilan: TextView
    private lateinit var tvTotalPokok: TextView
    private lateinit var tvTotalBunga: TextView
    private lateinit var tvTotalBayar: TextView
    private lateinit var tvTerbayar: TextView
    private lateinit var tvSisaBayar: TextView
    private lateinit var tvSisaPokok: TextView

    private lateinit var toggleMetode: MaterialButtonToggleGroup
    private lateinit var rvJadwal: RecyclerView
    private lateinit var adapter: com.example.projek_map.ui.adapters.AngsuranAdapter

    // nambah: tombol bayar
    private var btnBayar: MaterialButton? = null

    // repo (nambah aja)
    private val repo = PinjamanRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinjamanId = arguments?.getInt(ARG_PINJAMAN_ID, 0) ?: 0
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

        // nambah: tombol bayar
        btnBayar = v.findViewById(R.id.btnBayarAngsuranDetail)

        adapter = com.example.projek_map.ui.adapters.AngsuranAdapter()
        rvJadwal.layoutManager = LinearLayoutManager(requireContext())
        rvJadwal.adapter = adapter
        rvJadwal.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // default: ANUITAS (struktur tetap)
        v.findViewById<View>(R.id.btnAnuitas)?.let { toggleMetode.check(it.id) }
        currentMetode = "anuitas"
        renderAnuitas()

        toggleMetode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            when (checkedId) {
                R.id.btnAnuitas -> {
                    currentMetode = "anuitas"
                    renderAnuitas()
                }
                R.id.btnFlat -> {
                    currentMetode = "flat"
                    renderFlat()
                }
            }
        }

        // nambah: aksi bayar
        btnBayar?.setOnClickListener {
            if (pinjamanId <= 0) {
                toast("Pinjaman tidak valid."); return@setOnClickListener
            }
            showBayarAngsuranDialog()
        }

        return v
    }

    private fun renderAnuitas() {
        loadFromDb(metode = "anuitas")
    }

    private fun renderFlat() {
        loadFromDb(metode = "flat")
    }

    private fun loadFromDb(metode: String) {
        if (pinjamanId <= 0) {
            tvCicilan.text = rupiah(0)
            tvTotalPokok.text = rupiah(0)
            tvTotalBunga.text = rupiah(0)
            tvTotalBayar.text = rupiah(0)
            tvTerbayar.text = rupiah(0)
            tvSisaBayar.text = rupiah(0)
            tvSisaPokok.text = rupiah(0)
            adapter.setData(emptyList())
            return
        }

        lifecycleScope.launch {
            // 1) rincian
            val rincianResp = repo.getRincianPinjamanDb(pinjamanId, metode)
            val rincian: RincianPinjaman? = if (rincianResp.success) rincianResp.data else null

            if (rincian != null) {
                tvCicilan.text = rupiah(rincian.cicilanPerBulan)
                tvTotalPokok.text = rupiah(rincian.totalPokok)
                tvTotalBunga.text = rupiah(rincian.totalBunga)
                tvTotalBayar.text = rupiah(rincian.totalBayar)
                tvTerbayar.text = rupiah(rincian.terbayar)
                tvSisaBayar.text = rupiah(rincian.sisaBayar)
                tvSisaPokok.text = rupiah(rincian.sisaPokok)
            } else {
                tvCicilan.text = rupiah(0)
                tvTotalPokok.text = rupiah(0)
                tvTotalBunga.text = rupiah(0)
                tvTotalBayar.text = rupiah(0)
                tvTerbayar.text = rupiah(0)
                tvSisaBayar.text = rupiah(0)
                tvSisaPokok.text = rupiah(0)

                // nambah: biar kelihatan kalau gagal
                toast(rincianResp.message ?: "Gagal ambil rincian pinjaman")
            }

            // 2) jadwal
            val jadwalResp = repo.getJadwalPinjamanDb(pinjamanId, metode)
            val jadwal: List<AngsuranItem> = if (jadwalResp.success && jadwalResp.data != null) {
                jadwalResp.data
            } else {
                if (!jadwalResp.success) toast(jadwalResp.message ?: "Gagal ambil jadwal angsuran")
                emptyList()
            }

            adapter.setData(jadwal)
        }
    }

    // ======== nambah: fitur bayar dari halaman detail ========
    private fun showBayarAngsuranDialog() {
        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        if (kodePegawai.isBlank()) {
            toast("Kode pegawai kosong. Silakan login ulang."); return
        }

        lifecycleScope.launch {
            // rekomendasi cicilan dari rincian DB sesuai metode yang sedang dipilih
            val rincResp = repo.getRincianPinjamanDb(pinjamanId, currentMetode)
            val rinc = if (rincResp.success) rincResp.data else null
            val defaultJumlah = (rinc?.cicilanPerBulan ?: 0).coerceAtLeast(0)

            val info = buildString {
                if (defaultJumlah > 0) append("Rekomendasi cicilan/bulan: ${rupiah(defaultJumlah)}\n")
                if (rinc != null) append("Sisa bayar: ${rupiah(rinc.sisaBayar)}\n")
                append("\nIsi nominal pembayaran (Rp):")
            }

            val container = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(48, 24, 48, 0)
            }

            val tv = TextView(requireContext()).apply { text = info }

            val edtJumlah = EditText(requireContext()).apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                hint = if (defaultJumlah > 0) "Contoh: $defaultJumlah" else "Contoh: 200000"
                if (defaultJumlah > 0) setText(defaultJumlah.toString())
            }

            container.addView(tv)
            container.addView(edtJumlah)

            AlertDialog.Builder(requireContext())
                .setTitle("Bayar Angsuran #$pinjamanId")
                .setView(container)
                .setPositiveButton("Bayar") { _, _ ->
                    val jumlah = edtJumlah.text?.toString()?.trim()
                        ?.replace(".", "")?.replace(",", "")?.toIntOrNull()
                    if (jumlah == null || jumlah <= 0) {
                        toast("Nominal tidak valid."); return@setPositiveButton
                    }
                    submitPembayaranAngsuran(
                        kodePegawai = kodePegawai,
                        pinjamanId = pinjamanId,
                        jumlah = jumlah
                    )
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun submitPembayaranAngsuran(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int
    ) {
        lifecycleScope.launch {
            // ✅ FIX UTAMA:
            // addHistoriBukti() di repo kamu butuh buktiBase64 & buktiExt (bukan buktiUri).
            val res = repo.addHistoriBukti(
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                jumlah = jumlah,
                status = "Dibayar (User)",
                buktiBase64 = null,
                buktiExt = "jpg"
            )

            if (res.success) {
                toast("Pembayaran tercatat.")
                // refresh detail + jadwal
                loadFromDb(currentMetode)
            } else {
                toast(res.message ?: "Gagal mencatat pembayaran")
            }
        }
    }
    // =========================================================

    private fun rupiah(n: Int): String {
        return String.format(Locale("id", "ID"), "Rp %,d", n).replace(',', '.')
    }

    private fun toast(msg: String) {
        if (!isAdded) return
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_PINJAMAN_ID = "pinjaman_id"
        fun newInstance(pinjamanId: Int) = PinjamanDetailFragment().apply {
            arguments = Bundle().apply { putInt(ARG_PINJAMAN_ID, pinjamanId) }
        }
    }
}
