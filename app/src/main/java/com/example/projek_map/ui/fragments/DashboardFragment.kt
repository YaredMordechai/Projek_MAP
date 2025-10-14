package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager

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

        // ✅ Disesuaikan ke CardView (bukan MaterialButton)
        val cardSimpanan = view.findViewById<CardView>(R.id.cardSimpanan)
        val cardPinjaman = view.findViewById<CardView>(R.id.cardPinjaman)
        val cardLaporan = view.findViewById<CardView>(R.id.cardLaporan)
        val cardProfil = view.findViewById<CardView>(R.id.cardProfil)
//        val cardLogout = view.findViewById<CardView>(R.id.cardLogout)

        val pref = PrefManager(requireContext())

        if (isAdmin) {
            tvWelcome.text = "Halo, Admin"
            tvUserId.text = ""
            cardSimpanan.visibility = View.GONE
            cardPinjaman.visibility = View.GONE
            cardLaporan.visibility = View.GONE
            cardProfil.visibility = View.GONE
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

        // ✅ Navigasi (tetap sama seperti versi sebelumnya)
        cardSimpanan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, SimpananFragment())
                .addToBackStack(null)
                .commit()
        }

        cardPinjaman.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, PinjamanFragment())
                .addToBackStack(null)
                .commit()
        }

        cardLaporan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, LaporanFragment())
                .addToBackStack(null)
                .commit()
        }

        cardProfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

//        cardLogout.setOnClickListener {
//            // TODO: Implementasikan logout (kalau sudah siap)
//        }
    }
}
