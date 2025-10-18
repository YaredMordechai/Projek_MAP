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

            // 🔹 Cek user biasa
            val user = DummyUserData.users.find {
                (it.email == email || it.kodePegawai == email) && it.password == password
            }

            // 🔹 Cek admin
            val admin = DummyUserData.admins.find {
                (it.email == email || it.kodePegawai == email) && it.password == password
            }

            when {
                admin != null -> {
                    pref.saveLogin(admin.nama, admin.email, admin.kodePegawai)
                    Toast.makeText(this, "Login Admin berhasil: ${admin.nama}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userName", admin.nama)
                    intent.putExtra("userEmail", admin.email)
                    intent.putExtra("isAdmin", true)
                    startActivity(intent)
                    finish()
                }

                user != null -> {
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)
                    Toast.makeText(this, "Login berhasil! Selamat datang ${user.nama}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userName", user.nama)
                    intent.putExtra("userEmail", user.email)
                    intent.putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                    intent.putExtra("userKodePegawai", user.kodePegawai)
                    intent.putExtra("isAdmin", false)
                    startActivity(intent)
                    finish()
                }

                else -> {
                    Toast.makeText(this, "Email / Password salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}