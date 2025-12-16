package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.R
import com.example.projek_map.ui.viewmodels.SignupUiState
import com.example.projek_map.ui.viewmodels.SignupViewModel
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton

class SignupActivity : AppCompatActivity() {

    private lateinit var pref: PrefManager
    private val vm: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        pref = PrefManager(this)

        val btnSignup: MaterialButton = findViewById(R.id.btnSignup)
        val btnToLogin: MaterialButton = findViewById(R.id.btnToLogin)

        val etKodePegawai: EditText = findViewById(R.id.etKodePegawai)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val etNama: EditText = findViewById(R.id.etNama)

        observeVm(btnSignup)

        btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnSignup.setOnClickListener {
            val kodePegawai = etKodePegawai.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val nama = etNama.text.toString().trim()

            if (kodePegawai.isEmpty() || email.isEmpty() || password.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.register(kodePegawai, email, password, nama)
        }
    }

    private fun observeVm(btnSignup: MaterialButton) {
        vm.state.observe(this) { state ->
            when (state) {
                is SignupUiState.Idle -> {
                    btnSignup.isEnabled = true
                }
                is SignupUiState.Loading -> {
                    btnSignup.isEnabled = false
                }
                is SignupUiState.Success -> {
                    val user = state.user

                    pref.saveLogin(user.nama, user.email, user.kodePegawai)
                    pref.setIsAdmin(false)

                    Toast.makeText(this@SignupActivity, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    intent.putExtra("userName", user.nama)
                    intent.putExtra("userEmail", user.email)
                    intent.putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                    intent.putExtra("userKodePegawai", user.kodePegawai)
                    intent.putExtra("isAdmin", false)
                    startActivity(intent)
                    finish()
                }
                is SignupUiState.Error -> {
                    btnSignup.isEnabled = true
                    Toast.makeText(
                        this@SignupActivity,
                        state.message.ifBlank { "Pendaftaran gagal" },
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
