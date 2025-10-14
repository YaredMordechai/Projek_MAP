package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.google.android.material.button.MaterialButton

class DashboardFragment : Fragment() {

    private var isAdmin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val tvTotalSimpanan = view.findViewById<TextView>(R.id.tvTotalSimpanan)
        val tvPinjamanAktif = view.findViewById<TextView>(R.id.tvPinjamanAktif)
        val tvCicilanBulanan = view.findViewById<TextView>(R.id.tvCicilanBulanan)
        val btnSimpanan = view.findViewById<MaterialButton>(R.id.btnGoSimpanan)
        val btnPinjaman = view.findViewById<MaterialButton>(R.id.btnGoPinjaman)
        val btnCicilan = view.findViewById<MaterialButton>(R.id.btnGoCicilan)
        val btnLaporan = view.findViewById<MaterialButton>(R.id.btnGoLaporan)
        val btnProfil = view.findViewById<MaterialButton>(R.id.btnGoProfil)

        if (isAdmin) {
            // 🔹 Admin Mode — hide user-specific data
            tvWelcome.text = "Halo, Admin"
            tvUserId.text = ""
            tvTotalSimpanan.visibility = View.GONE
            tvPinjamanAktif.visibility = View.GONE
            tvCicilanBulanan.visibility = View.GONE
            btnSimpanan.visibility = View.GONE
            btnPinjaman.visibility = View.GONE
            btnCicilan.visibility = View.GONE
            btnLaporan.visibility = View.GONE
            btnProfil.visibility = View.GONE

        } else {
            // 🔹 Normal User (dummy data)
            tvWelcome.text = "Halo, Andi"
            tvUserId.text = "ID: K001"
            imgProfile.setImageResource(R.drawable.ic_profil)
            tvTotalSimpanan.text = "Total Simpanan: Rp 3.500.000"
            tvPinjamanAktif.text = "Pinjaman Aktif: Rp 4.000.000"
            tvCicilanBulanan.text = "Cicilan / bulan: Rp 525.000"
        }
    }
}