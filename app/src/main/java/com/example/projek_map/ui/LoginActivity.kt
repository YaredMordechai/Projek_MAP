package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.databinding.ActivityLoginBinding
import com.example.projek_map.utils.PrefManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PrefManager(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val user = DummyUserData.users.find {
                (it.email == email || it.kodePegawai == email) && it.password == password
            }

            if (user != null) {
                // 🔹 Simpan data user login ke PrefManager
                pref.saveLogin(user.nama, user.email, user.kodePegawai)

                Toast.makeText(this, "Login berhasil! Selamat datang ${user.nama}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                // Kirim semua data user ke MainActivity
                intent.putExtra("userName", user.nama)
                intent.putExtra("userEmail", user.email)
                intent.putExtra("userTelepon", user.telepon)
                intent.putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                intent.putExtra("userKodePegawai", user.kodePegawai)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email/Kode Pegawai atau password salah", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
