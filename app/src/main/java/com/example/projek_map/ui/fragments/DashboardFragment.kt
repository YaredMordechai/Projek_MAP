package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager

class DashboardFragment : Fragment() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvUserId: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvTotalSimpanan: TextView
    private lateinit var tvTotalPinjaman: TextView
    private lateinit var tvTotalTransaksi: TextView
    private lateinit var tvLastLogin: TextView

    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        prefManager = PrefManager(requireContext())

        tvWelcome = view.findViewById(R.id.tvWelcome)
        tvUserId = view.findViewById(R.id.tvUserId)
        tvStatus = view.findViewById(R.id.tvStatusKeanggotaan)
        tvTotalSimpanan = view.findViewById(R.id.tvTotalSimpanan)
        tvTotalPinjaman = view.findViewById(R.id.tvTotalPinjaman)
        tvTotalTransaksi = view.findViewById(R.id.tvTotalTransaksi)
        tvLastLogin = view.findViewById(R.id.tvLastLogin)

        bindData()

        return view
    }

    private fun bindData() {
        val kodePegawai = prefManager.getKodePegawai() ?: "EMP001"
        val nama = prefManager.getUserName() ?: "Anggota"
        val lastLogin = prefManager.getLastLogin() ?: "-"

        val user = DummyUserData.users.find { it.kodePegawai == kodePegawai }

        tvWelcome.text = "Halo, $nama"
        tvUserId.text = "ID: $kodePegawai"
        tvLastLogin.text = "Login terakhir: $lastLogin"

        tvStatus.text = user?.statusKeanggotaan ?: "Anggota"

        val totalSimpanan = DummyUserData.getTotalSimpanan(kodePegawai)
        val totalPinjaman = DummyUserData.getTotalPinjaman(kodePegawai)
        val totalTransaksi = DummyUserData.getTotalTransaksiSimpanan(kodePegawai)

        tvTotalSimpanan.text = "Rp ${formatRupiah(totalSimpanan)}"
        tvTotalPinjaman.text = "Rp ${formatRupiah(totalPinjaman)}"
        tvTotalTransaksi.text = "Rp ${formatRupiah(totalTransaksi)}"
    }

    private fun formatRupiah(value: Double): String {
        return String.format("%,.0f", value).replace(',', '.')
    }
}
