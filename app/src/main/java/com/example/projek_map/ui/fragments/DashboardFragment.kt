package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager
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
        val btnSimpanan = view.findViewById<MaterialButton>(R.id.btnGoSimpanan)
        val btnPinjaman = view.findViewById<MaterialButton>(R.id.btnGoPinjaman)
//        val btnCicilan = view.findViewById<MaterialButton>(R.id.btnGoCicilan)
        val btnLaporan = view.findViewById<MaterialButton>(R.id.btnGoLaporan)
        val btnProfil = view.findViewById<MaterialButton>(R.id.btnGoProfil)

        val pref = PrefManager(requireContext())

        if (isAdmin) {
            tvWelcome.text = "Halo, Admin"
            tvUserId.text = ""
            btnSimpanan.visibility = View.GONE
            btnPinjaman.visibility = View.GONE
//            btnCicilan.visibility = View.GONE
            btnLaporan.visibility = View.GONE
            btnProfil.visibility = View.GONE
        } else {
            val loggedEmail = pref.getEmail() ?: ""
            val loggedKode = pref.getKodePegawai() ?: ""

            val currentUser = DummyUserData.users.find {
                it.email.equals(loggedEmail, ignoreCase = true) || it.kodePegawai == loggedKode
            }

            if (currentUser != null) {
                tvWelcome.text = "Halo, ${currentUser.nama}"
                tvUserId.text = "Kode Pegawai: ${currentUser.kodePegawai}"
            } else {
                tvWelcome.text = "Halo, Pengguna"
                tvUserId.text = "Kode Pegawai: -"
            }

            imgProfile.setImageResource(R.drawable.ic_profil)
        }

        // ✅ Navigasi aman tanpa unresolved reference
        btnSimpanan.setOnClickListener {
            findNavController().navigate(R.id.navigation_simpanan)
        }

        btnPinjaman.setOnClickListener {
            findNavController().navigate(R.id.navigation_pinjaman)
        }

//        btnCicilan.setOnClickListener {
//            findNavController().navigate(R.id.nav_cicilan)
//        }

        btnLaporan.setOnClickListener {
            findNavController().navigate(R.id.navigation_laporan)
        }

        btnProfil.setOnClickListener {
            findNavController().navigate(R.id.navigation_profil)
        }
    }
}
