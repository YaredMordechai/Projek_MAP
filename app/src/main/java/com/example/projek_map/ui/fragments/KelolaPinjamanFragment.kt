package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.HistoriPembayaran
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
import com.example.projek_map.ui.adapters.KelolaPinjamanAdapter
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class KelolaPinjamanFragment : Fragment() {

    // --- View Persetujuan Pinjaman
    private lateinit var rvPinjaman: RecyclerView
    private lateinit var adapterPinjaman: KelolaPinjamanAdapter

    // --- View Pembayaran Angsuran (blok di layout yang sama)
    private lateinit var layoutPembayaran: ViewGroup
    private lateinit var rvHistori: RecyclerView
    private lateinit var btnCatat: MaterialButton
    private lateinit var tvEmpty: TextView
    private lateinit var adapterHistori: HistoriPembayaranAdapter
    private val displayHistori = mutableListOf<HistoriPembayaran>()
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    // --- Tombol toggle mode
    private lateinit var btnKelolaPembayaran: MaterialButton
    private var modePembayaran: Boolean = false // default: mode persetujuan

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_kelola_pinjaman, container, false)

        // ====== Bind view umum ======
        btnKelolaPembayaran = v.findViewById(R.id.btnKelolaPembayaranAngsuran)

        // ====== Persetujuan Pinjaman ======
        rvPinjaman = v.findViewById(R.id.rvKelolaPinjaman)
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())
        adapterPinjaman = KelolaPinjamanAdapter(
            pendingOnly().toMutableList(),
            onApprove = { pinjaman -> showApprovalDialog(pinjaman, true) },
            onReject = { pinjaman -> showApprovalDialog(pinjaman, false) }
        )
        rvPinjaman.adapter = adapterPinjaman

        // ====== Pembayaran Angsuran (blok di layout yang sama) ======
        layoutPembayaran = v.findViewById(R.id.layoutPembayaranContainer)
        rvHistori = v.findViewById(R.id.rvHistoriPembayaran)
        btnCatat = v.findViewById(R.id.btnCatatPembayaran)
        tvEmpty = v.findViewById(R.id.tvEmptyPembayaran)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        adapterHistori = HistoriPembayaranAdapter(displayHistori)
        rvHistori.adapter = adapterHistori

        btnCatat.setOnClickListener { showCatatDialog() }

        // ====== Toggle mode via satu tombol ======
        btnKelolaPembayaran.setOnClickListener {
            modePembayaran = !modePembayaran
            applyMode()
        }

        // Default masuk ke mode persetujuan
        modePembayaran = false
        applyMode()

        return v
    }

    override fun onResume() {
        super.onResume()
        // Refresh keduanya (aman)
        adapterPinjaman.replaceAll(pendingOnly())
        refreshHistori()
    }

    // =========================
    // Mode & UI toggle
    // =========================
    private fun applyMode() {
        if (modePembayaran) {
            // Tampilkan blok pembayaran, sembunyikan daftar pinjaman
            layoutPembayaran.visibility = View.VISIBLE
            rvPinjaman.visibility = View.GONE
            btnKelolaPembayaran.text = "Kembali ke Persetujuan"
            refreshHistori()
        } else {
            // Tampilkan daftar pinjaman, sembunyikan blok pembayaran
            layoutPembayaran.visibility = View.GONE
            rvPinjaman.visibility = View.VISIBLE
            btnKelolaPembayaran.text = "Kelola Pembayaran Angsuran"
        }
    }

    private fun refreshHistori() {
        val list = DummyUserData.historiPembayaranList.sortedByDescending { it.tanggal }
        displayHistori.clear()
        displayHistori.addAll(list)
        adapterHistori.notifyDataSetChanged()

        val empty = displayHistori.isEmpty()
        tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        rvHistori.visibility = if (empty) View.GONE else View.VISIBLE
    }

    // =========================
    // Dialog Catat Pembayaran
    // =========================
    private fun showCatatDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_catat_pembayaran, null)
        val edtKode = dialogView.findViewById<EditText>(R.id.edtKodePegawai)
        val edtPinjamanId = dialogView.findViewById<EditText>(R.id.edtPinjamanId)
        val edtJumlah = dialogView.findViewById<EditText>(R.id.edtJumlahBayar)

        AlertDialog.Builder(requireContext())
            .setTitle("Catat Pembayaran Angsuran")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val kode = edtKode.text.toString().trim()
                val pinjamanId = edtPinjamanId.text.toString().trim().toIntOrNull()
                // parsing jumlah: tahan format 200.000 atau 200,000
                val jumlahRaw = edtJumlah.text.toString().trim()
                val jumlah = jumlahRaw.replace(".", "").replace(",", "").toLongOrNull()?.toInt()

                if (kode.isEmpty() || pinjamanId == null || jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Isi data dengan benar.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val pinj = DummyUserData.pinjamanList.find { it.id == pinjamanId }
                if (pinj == null) {
                    Toast.makeText(requireContext(), "Pinjaman #$pinjamanId tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (!pinj.kodePegawai.equals(kode, true)) {
                    Toast.makeText(requireContext(), "Pinjaman #$pinjamanId bukan milik $kode.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val sisa = DummyUserData.getSisaAngsuran(pinjamanId)
                if (sisa <= 0) {
                    Toast.makeText(requireContext(), "Pinjaman sudah lunas.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (jumlah > sisa) {
                    Toast.makeText(requireContext(), "Jumlah melebihi sisa (${rupiah.format(sisa)}).", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Catat pembayaran ke dummy store
                DummyUserData.catatPembayaranAngsuranAdmin(
                    kodePegawai = kode,
                    pinjamanId = pinjamanId,
                    jumlahBayar = jumlah
                )

                // Auto ubah status ke Lunas bila sisa habis
                val sisaBaru = DummyUserData.getSisaAngsuran(pinjamanId)
                if (sisaBaru == 0) DummyUserData.setStatusPinjaman(pinjamanId, "Lunas")

                refreshHistori()
                Toast.makeText(requireContext(), "Pembayaran dicatat.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // =========================
    // Dialog approve / reject
    // =========================
    private fun showApprovalDialog(pinjaman: Pinjaman, approve: Boolean) {
        val actionText = if (approve) "Setujui" else "Tolak"

        AlertDialog.Builder(requireContext())
            .setTitle("$actionText Pinjaman")
            .setMessage("Apakah kamu yakin ingin $actionText pinjaman dari ${pinjaman.kodePegawai}?")
            .setPositiveButton(actionText) { _, _ ->
                if (approve) {
                    DummyUserData.approvePinjaman(pinjaman.id)
                } else {
                    DummyUserData.rejectPinjaman(pinjaman.id)
                }

                DummyUserData.enqueueDecisionNotification(
                    kodePegawai = pinjaman.kodePegawai,
                    pinjamanId = pinjaman.id,
                    decision = if (approve) "disetujui" else "ditolak",
                    jumlah = pinjaman.jumlah
                )

                sendDecisionBroadcast(pinjaman, approve)
                adapterPinjaman.removeItemById(pinjaman.id)

                Toast.makeText(
                    requireContext(),
                    "Pinjaman ${pinjaman.kodePegawai} telah ${if (approve) "disetujui" else "ditolak"}.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // =========================
    // Broadcast helper (opsional)
    // =========================
    private fun sendDecisionBroadcast(pinjaman: Pinjaman, approve: Boolean) {
        val intent = Intent(requireContext(), com.example.projek_map.utils.AlarmReceiver::class.java).apply {
            putExtra("type", "keputusan_pinjaman")
            putExtra("decision", if (approve) "disetujui" else "ditolak")
            putExtra("pinjamanId", pinjaman.id)
            putExtra("jumlah", pinjaman.jumlah)
            putExtra("kodePegawai", pinjaman.kodePegawai)
        }
        requireContext().sendBroadcast(intent)
    }

    // Hanya tampilkan pinjaman status Proses/Menunggu
    private fun pendingOnly(): List<Pinjaman> =
        DummyUserData.pinjamanList.filter {
            val s = it.status.lowercase()
            s == "proses" || s == "menunggu"
        }
}
