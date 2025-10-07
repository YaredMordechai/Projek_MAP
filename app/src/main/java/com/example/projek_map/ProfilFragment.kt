package com.example.projek_map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.ui.LoginActivity
import com.example.projek_map.utils.PrefManager
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
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)

        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val btnBackDashboard = view.findViewById<MaterialButton>(R.id.btnBackDashboard)

        val pref = PrefManager(requireContext())

        // 🔹 Ambil data dari argument (jika dikirim dari MainActivity)
        val namaArg = arguments?.getString("nama")
        val emailArg = arguments?.getString("email")
        val teleponArg = arguments?.getString("telepon")
        val statusArg = arguments?.getString("statusKeanggotaan")
        val kodePegawaiArg = arguments?.getString("kodePegawai")

        if (!namaArg.isNullOrEmpty()) {
            // tampilkan data dari intent bundle
            tvNamaUser.text = namaArg
            tvIdUser.text = "Kode Pegawai: ${kodePegawaiArg ?: "-"}"
            tvEmail.text = "Email: ${emailArg ?: "-"}"
            tvTelepon.text = "Nomor Telepon: ${teleponArg ?: "-"}"
            tvStatus.text = "Status Keanggotaan: ${statusArg ?: "-"}"
        } else {
            // fallback ke PrefManager + DummyUserData
            val loggedEmail = pref.getEmail() ?: ""
            val loggedKode = pref.getKodePegawai() ?: ""

            val currentUser = DummyUserData.users.find {
                it.email.equals(loggedEmail, ignoreCase = true) || it.kodePegawai == loggedKode
            }

            if (currentUser != null) {
                tvNamaUser.text = currentUser.nama
                tvIdUser.text = "Kode Pegawai: ${currentUser.kodePegawai}"
                tvEmail.text = "Email: ${currentUser.email}"
                tvTelepon.text = "Nomor Telepon: ${currentUser.telepon}"
                tvStatus.text = "Status Keanggotaan: ${currentUser.statusKeanggotaan}"
            } else {
                tvNamaUser.text = pref.getUserName() ?: "Pengguna Tidak Dikenal"
                tvIdUser.text = "Kode Pegawai: ${pref.getKodePegawai() ?: "-"}"
                tvEmail.text = "Email: ${pref.getEmail() ?: "-"}"
                tvTelepon.text = "Nomor Telepon: -"
                tvStatus.text = "Status Keanggotaan: -"
            }
        }

        imgProfile.setImageResource(R.drawable.ic_profil)

        // Tombol Logout
        btnLogout.setOnClickListener {
            pref.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Tombol Kembali ke Dashboard
        btnBackDashboard.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
