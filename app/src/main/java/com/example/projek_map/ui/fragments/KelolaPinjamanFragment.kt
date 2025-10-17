package com.example.projek_map.ui.fragments

import android.app.AlertDialog
import android.content.Intent // ⬅️ TAMBAHAN (tetap boleh)
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.ui.adapters.KelolaPinjamanAdapter

class KelolaPinjamanFragment : Fragment() {

    private lateinit var rvPinjaman: RecyclerView
    private lateinit var adapter: KelolaPinjamanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kelola_pinjaman, container, false)

        rvPinjaman = view.findViewById(R.id.rvKelolaPinjaman)
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())

        // 🔹 Tampilkan HANYA permohonan yang menunggu (Proses/Menunggu)
        adapter = KelolaPinjamanAdapter(
            pendingOnly().toMutableList(),
            onApprove = { pinjaman -> showApprovalDialog(pinjaman, true) },
            onReject = { pinjaman -> showApprovalDialog(pinjaman, false) }
        )

        rvPinjaman.adapter = adapter
        return view
    }

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

                // 🔔 Simpan event ke queue agar device pegawai menerima saat login
                DummyUserData.enqueueDecisionNotification(
                    kodePegawai = pinjaman.kodePegawai,
                    pinjamanId = pinjaman.id,
                    decision = if (approve) "disetujui" else "ditolak",
                    jumlah = pinjaman.jumlah
                )

                // (opsional) broadcast lokal langsung — boleh tetap ada
                sendDecisionBroadcast(pinjaman, approve)

                // ⬇️ Hapus dari list UI agar langsung hilang
                adapter.removeItemById(pinjaman.id)

                Toast.makeText(
                    requireContext(),
                    "Pinjaman ${pinjaman.kodePegawai} telah ${if (approve) "disetujui" else "ditolak"}.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        // 🔄 Refresh list pending saat kembali ke layar
        adapter.replaceAll(pendingOnly())
    }

    // =========================
    // TAMBAHAN: Broadcast helper
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

    // 🔹 Helper: ambil cuma yang status Proses/Menunggu
    private fun pendingOnly(): List<Pinjaman> =
        DummyUserData.pinjamanList.filter {
            val s = it.status.lowercase()
            s == "proses" || s == "menunggu"
        }
}
