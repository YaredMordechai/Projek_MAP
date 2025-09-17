package com.example.projek_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class ProfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val tvNamaUser = view.findViewById<TextView>(R.id.tvNamaUser)
        val tvIdUser = view.findViewById<TextView>(R.id.tvIdUser)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvTelepon = view.findViewById<TextView>(R.id.tvTelepon)

        val btnEditProfil = view.findViewById<MaterialButton>(R.id.btnEditProfil)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val btnBackDashboard = view.findViewById<MaterialButton>(R.id.btnBackDashboard)

        // --- Dummy Data (nanti bisa ambil dari database / API / SharedPreferences) ---
        tvNamaUser.text = "Andi Setiawan"
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
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            // contoh: arahkan ke LoginActivity
            // startActivity(Intent(requireContext(), LoginActivity::class.java))
            // requireActivity().finish()
        }

        // --- Tombol Kembali ke Dashboard ---
        btnBackDashboard.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
