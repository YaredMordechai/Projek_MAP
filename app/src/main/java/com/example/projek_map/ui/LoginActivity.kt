package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.databinding.ActivityLoginBinding
import com.example.projek_map.ui.viewmodels.LoginUiState
import com.example.projek_map.ui.viewmodels.LoginViewModel
import com.example.projek_map.utils.PrefManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: PrefManager

    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PrefManager(this)

        observeVm()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.login(email, password)
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun observeVm() {
        vm.state.observe(this) { state ->
            when (state) {
                is LoginUiState.Idle -> {
                    binding.btnLogin.isEnabled = true
                }
                is LoginUiState.Loading -> {
                    binding.btnLogin.isEnabled = false
                }
                is LoginUiState.SuccessAdmin -> {
                    val admin = state.admin

                    pref.logout() // bersihkan session lama
                    pref.saveLogin(admin.nama, admin.email, admin.kodePegawai)
                    pref.setIsAdmin(true)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login admin berhasil! Selamat datang ${admin.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is LoginUiState.SuccessUser -> {
                    val user = state.user

                    pref.logout() // bersihkan session lama
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)
                    pref.setIsAdmin(false)

                    Toast.makeText(
                        this@LoginActivity,
                        "Login berhasil! Selamat datang ${user.nama}",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is LoginUiState.Error -> {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(
                        this@LoginActivity,
                        state.message.ifBlank { "Email / password salah" },
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
