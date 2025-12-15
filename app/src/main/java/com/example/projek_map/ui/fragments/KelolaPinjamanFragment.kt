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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.api.AddHistoriPembayaranRequest
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.DecidePinjamanRequest
import com.example.projek_map.api.HistoriPembayaran
import com.example.projek_map.data.HistoriPembayaranAdminRepository
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.PinjamanRepository
import com.example.projek_map.ui.adapters.HistoriPembayaranAdapter
import com.example.projek_map.ui.adapters.KelolaPinjamanAdapter
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class KelolaPinjamanFragment : Fragment() {

    // Repo histori admin
    private val historiAdminRepo = HistoriPembayaranAdminRepository()

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
    private var modePembayaran: Boolean = false

    private lateinit var pref: PrefManager

    private val pinjamanRepo = PinjamanRepository()
    private val apiPinjamanList = mutableListOf<Pinjaman>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_kelola_pinjaman, container, false)

        pref = PrefManager(requireContext())

        btnKelolaPembayaran = v.findViewById(R.id.btnKelolaPembayaranAngsuran)

        // ====== Persetujuan Pinjaman ======
        rvPinjaman = v.findViewById(R.id.rvKelolaPinjaman)
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())
        adapterPinjaman = KelolaPinjamanAdapter(
            apiPinjamanList,
            onApprove = { pinjaman -> showApprovalDialog(pinjaman, true) },
            onReject = { pinjaman -> showApprovalDialog(pinjaman, false) }
        )
        rvPinjaman.adapter = adapterPinjaman

        // ====== Pembayaran Angsuran ======
        layoutPembayaran = v.findViewById(R.id.layoutPembayaranContainer)
        rvHistori = v.findViewById(R.id.rvHistoriPembayaran)
        btnCatat = v.findViewById(R.id.btnCatatPembayaran)
        tvEmpty = v.findViewById(R.id.tvEmptyPembayaran)

        rvHistori.layoutManager = LinearLayoutManager(requireContext())
        adapterHistori = HistoriPembayaranAdapter(displayHistori)
        rvHistori.adapter = adapterHistori

        btnCatat.setOnClickListener { showCatatDialog() }

        btnKelolaPembayaran.setOnClickListener {
            modePembayaran = !modePembayaran
            applyMode()
        }

        modePembayaran = false
        applyMode()

        return v
    }

    override fun onResume() {
        super.onResume()
        loadPinjamanAdmin()
        refreshHistori()
    }

    private fun applyMode() {
        if (modePembayaran) {
            layoutPembayaran.visibility = View.VISIBLE
            rvPinjaman.visibility = View.GONE
            btnKelolaPembayaran.text = "Kembali ke Persetujuan"
            refreshHistori()
        } else {
            layoutPembayaran.visibility = View.GONE
            rvPinjaman.visibility = View.VISIBLE
            btnKelolaPembayaran.text = "Kelola Pembayaran Angsuran"
        }
    }

    private fun refreshHistori() {
        viewLifecycleOwner.lifecycleScope.launch {
            val resp = historiAdminRepo.getAll()
            if (resp.success) {
                val rows = resp.data ?: emptyList()
                displayHistori.clear()
                displayHistori.addAll(rows.sortedByDescending { it.tanggal })
                adapterHistori.notifyDataSetChanged()

                val empty = displayHistori.isEmpty()
                tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
                rvHistori.visibility = if (empty) View.GONE else View.VISIBLE
            } else {
                Toast.makeText(
                    requireContext(),
                    resp.message ?: "Gagal ambil histori pembayaran",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

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
                val jumlah = edtJumlah.text.toString().trim().replace(".", "").replace(",", "").toIntOrNull()

                if (kode.isEmpty() || pinjamanId == null || jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Isi data dengan benar", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val today = java.text.SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                            .format(java.util.Date())

                        val resp = ApiClient.apiService.addHistoriPembayaran(
                            AddHistoriPembayaranRequest(
                                kodePegawai = kode,
                                pinjamanId = pinjamanId,
                                tanggal = today,
                                jumlah = jumlah,
                                status = "Dibayar (Admin)",
                                buktiPembayaranUri = null
                            )
                        )

                        if (resp.isSuccessful && resp.body()?.success == true) {
                            Toast.makeText(requireContext(), "Pembayaran berhasil dicatat", Toast.LENGTH_SHORT).show()
                            refreshHistori()
                            loadPinjamanAdmin()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                resp.body()?.message ?: "Gagal menyimpan pembayaran",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showApprovalDialog(pinjaman: Pinjaman, approve: Boolean) {
        val actionText = if (approve) "Setujui" else "Tolak"

        AlertDialog.Builder(requireContext())
            .setTitle("$actionText Pinjaman")
            .setMessage("Apakah kamu yakin ingin $actionText pinjaman dari ${pinjaman.kodePegawai}?")
            .setPositiveButton(actionText) { _, _ ->
                decidePinjamanViaApi(pinjaman, approve) { success, message ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Pinjaman ${pinjaman.kodePegawai} berhasil ${actionText.lowercase()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadPinjamanAdmin()
                    } else {
                        Toast.makeText(requireContext(), message ?: "Gagal memproses pinjaman", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun decidePinjamanViaApi(
        pinjaman: Pinjaman,
        approve: Boolean,
        onDone: (Boolean, String) -> Unit
    ) {
        val adminKode = pref.getKodePegawai().orEmpty()
        if (adminKode.isEmpty()) {
            onDone(false, "Admin belum login (kode admin kosong).")
            return
        }

        val decision = if (approve) "approve" else "reject"

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val res = ApiClient.apiService.decidePinjaman(
                    DecidePinjamanRequest(
                        pinjamanId = pinjaman.id,
                        decision = decision,
                        adminKode = adminKode
                    )
                )
                val body = res.body()
                val ok = res.isSuccessful && body?.success == true
                onDone(ok, body?.message ?: if (ok) "OK" else "Gagal memproses keputusan")
            } catch (e: Exception) {
                onDone(false, "Server error: ${e.message}")
            }
        }
    }

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

    private fun loadPinjamanAdmin() {
        viewLifecycleOwner.lifecycleScope.launch {
            val resp = pinjamanRepo.getAllPinjamanAdmin()

            if (resp.success) {
                val pending = (resp.data ?: emptyList()).filter {
                    val s = it.status.lowercase()
                    s == "proses" || s == "menunggu"
                }
                apiPinjamanList.clear()
                apiPinjamanList.addAll(pending)
                adapterPinjaman.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), resp.message ?: "Gagal ambil pinjaman", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
