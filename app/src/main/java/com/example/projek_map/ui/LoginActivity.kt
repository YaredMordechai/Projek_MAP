package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.data.AuthRepository
import com.example.projek_map.databinding.ActivityLoginBinding
import com.example.projek_map.utils.PrefManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: PrefManager
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PrefManager(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                binding.btnLogin.isEnabled = false

                // 1) coba login USER dulu (biar alur lama kamu tetap jalan)
                val userResult = authRepository.login(email, password)

                if (userResult?.success == true && userResult.data != null) {
                    binding.btnLogin.isEnabled = true
                    val user = userResult.data

                    // Simpan ke PrefManager (struktur lama tetap)
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login berhasil! Selamat datang ${user.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("userName", user.nama)
                    intent.putExtra("userEmail", user.email)
                    intent.putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                    intent.putExtra("userKodePegawai", user.kodePegawai)
                    intent.putExtra("isAdmin", false)
                    startActivity(intent)
                    finish()
                    return@launch
                }

                // 2) kalau USER gagal, coba login ADMIN (ADM001, ADM002, atau email admin)
                val adminResult = authRepository.loginAdmin(email, password)

                binding.btnLogin.isEnabled = true

                if (adminResult?.success == true && adminResult.data != null) {
                    val admin = adminResult.data

                    // Simpan ke PrefManager juga (biar fragment lain bisa pakai kodePegawai)
                    // Admin juga punya kodePegawai, email, nama.
                    pref.saveLogin(admin.nama, admin.email, admin.kodePegawai)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login admin berhasil! Selamat datang ${admin.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("userName", admin.nama)
                    intent.putExtra("userEmail", admin.email)
                    intent.putExtra("userStatusKeanggotaan", admin.role) // optional (role)
                    intent.putExtra("userKodePegawai", admin.kodePegawai)
                    intent.putExtra("isAdmin", true)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        adminResult?.message
                            ?: userResult?.message
                            ?: "Email / password salah atau server error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
