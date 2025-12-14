package com.example.projek_map.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.ui.LoginActivity
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

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
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val btnBackDashboard = view.findViewById<MaterialButton>(R.id.btnBackDashboard)

        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        val nama = pref.getUserName().orEmpty()
        val email = pref.getEmail().orEmpty()

        // Tampilkan dulu dari pref (biar UI langsung keisi)
        tvNamaUser.text = if (nama.isNotBlank()) nama else "Pengguna"
        tvIdUser.text = "Kode Pegawai: ${if (kodePegawai.isNotBlank()) kodePegawai else "-"}"
        tvEmail.text = "Email: ${if (email.isNotBlank()) email else "-"}"
        tvStatus.text = "Status Keanggotaan: -"

        // Optional: update dari API (ambil statusKeanggotaan terbaru)
        if (kodePegawai.isNotBlank()) {
            lifecycleScope.launch {
                try {
                    val resp = ApiClient.apiService.getUser(kodePegawai)
                    val user = resp.body()?.data
                    if (user != null) {
                        tvNamaUser.text = user.nama
                        tvIdUser.text = "Kode Pegawai: ${user.kodePegawai}"
                        tvEmail.text = "Email: ${user.email}"
                        tvStatus.text = "Status Keanggotaan: ${user.statusKeanggotaan}"
                    }
                } catch (_: Exception) {
                    // kalau gagal, biar tetap pakai pref
                }
            }
        }

        imgProfile.setImageResource(R.drawable.ic_profil)

        btnLogout.setOnClickListener {
            pref.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        btnBackDashboard.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
