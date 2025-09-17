package com.example.projek_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Header
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)

        tvWelcome.text = "Halo, Andi"
        tvUserId.text = "ID: K001"
        imgProfile.setImageResource(R.drawable.ic_person) // bisa load dari URL dengan Glide/Picasso

        // Ringkasan (dummy data dulu)
        val tvTotalSimpanan = view.findViewById<TextView>(R.id.tvTotalSimpanan)
        val tvPinjamanAktif = view.findViewById<TextView>(R.id.tvPinjamanAktif)
        val tvCicilanBulanan = view.findViewById<TextView>(R.id.tvCicilanBulanan)

        tvTotalSimpanan.text = "Total Simpanan: Rp 3.500.000"
        tvPinjamanAktif.text = "Pinjaman Aktif: Rp 4.000.000"
        tvCicilanBulanan.text = "Cicilan / bulan: Rp 525.000"

        // Tombol Navigasi (nanti isi dengan fragment navigation)
        view.findViewById<MaterialButton>(R.id.btnGoSimpanan).setOnClickListener {
            // TODO: navigate to SimpananFragment
        }
        view.findViewById<MaterialButton>(R.id.btnGoPinjaman).setOnClickListener {
            // TODO: navigate to PinjamanFragment
        }
        view.findViewById<MaterialButton>(R.id.btnGoCicilan).setOnClickListener {
            // TODO: navigate to CicilanFragment
        }
        view.findViewById<MaterialButton>(R.id.btnGoLaporan).setOnClickListener {
            // TODO: navigate to LaporanFragment
        }
        view.findViewById<MaterialButton>(R.id.btnGoProfil).setOnClickListener {
            // TODO: navigate to ProfilFragment
        }
    }
}
