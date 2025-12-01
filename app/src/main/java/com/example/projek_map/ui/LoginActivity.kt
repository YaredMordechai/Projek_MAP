package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.databinding.ActivityLoginBinding
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.viewmodel.KoperasiViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: PrefManager

    private val viewModel: KoperasiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PrefManager(this)

        // observe hasil login
        viewModel.loginState.observe(this) { state ->
            val admin = state.admin
            val user = state.user

            when {
                admin != null -> {
                    pref.saveLogin(admin.nama, admin.email, admin.kodePegawai)
                    Toast.makeText(
                        this,
                        "Login Admin berhasil: ${admin.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("userName", admin.nama)
                        putExtra("userEmail", admin.email)
                        putExtra("userKodePegawai", admin.kodePegawai)
                        putExtra("userStatusKeanggotaan", "Admin")
                        putExtra("isAdmin", true)
                    }
                    startActivity(intent)
                    finish()
                }

                user != null -> {
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)
                    Toast.makeText(
                        this,
                        "Login berhasil! Selamat datang ${user.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("userName", user.nama)
                        putExtra("userEmail", user.email)
                        putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                        putExtra("userKodePegawai", user.kodePegawai)
                        putExtra("isAdmin", false)
                    }
                    startActivity(intent)
                    finish()
                }

                else -> {
                    Toast.makeText(this, "Email / Kode / Password salah!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val identifier = binding.etEmail.text.toString().trim()   // email ATAU kodePegawai
            val password = binding.etPassword.text.toString().trim()

            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi email/kode dan password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.login(identifier, password)
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
