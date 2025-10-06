package com.example.projek_map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.example.projek_map.ui.LoginActivity
import com.example.projek_map.utils.PrefManager

class ProfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Inisialisasi komponen ---
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val tvNamaUser = view.findViewById<TextView>(R.id.tvNamaUser)
        val tvIdUser = view.findViewById<TextView>(R.id.tvIdUser)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvTelepon = view.findViewById<TextView>(R.id.tvTelepon)

        val btnEditProfil = view.findViewById<MaterialButton>(R.id.btnEditProfil)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val btnBackDashboard = view.findViewById<MaterialButton>(R.id.btnBackDashboard)

        // --- Ambil data dari argument (dikirim dari MainActivity) ---
        val userNama = arguments?.getString("nama") ?: "Andi Setiawan"

        // --- Dummy data pengguna ---
        tvNamaUser.text = userNama
        tvIdUser.text = "ID: K001"
        tvEmail.text = "Email: andi@gmail.com"
        tvTelepon.text = "Nomor Telepon: 08123456789"
        imgProfile.setImageResource(R.drawable.ic_profil)

        // --- Tombol Edit Profil ---
        btnEditProfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, EditProfilFragment())
                .addToBackStack(null)
                .commit()
        }

        // --- Tombol Logout ---
        btnLogout.setOnClickListener {
            val pref = PrefManager(requireContext())
            pref.logout()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // --- Tombol Kembali ke Dashboard ---
        btnBackDashboard.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
