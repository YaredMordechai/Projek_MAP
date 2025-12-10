package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.R
import com.example.projek_map.data.AuthRepository
import com.example.projek_map.utils.PrefManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var pref: PrefManager
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)   // <-- pastikan nama layout ini benar

        pref = PrefManager(this)

        // Ambil view dari layout (ID harus sama dengan yang di XML)
        val btnSignup: MaterialButton = findViewById(R.id.btnSignup)
        val etKodePegawai: EditText = findViewById(R.id.etKodePegawai)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val etNama: EditText = findViewById(R.id.etNama)

        btnSignup.setOnClickListener {
            val kodePegawai = etKodePegawai.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val nama = etNama.text.toString().trim()

            if (kodePegawai.isEmpty() || email.isEmpty() || password.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                btnSignup.isEnabled = false

                val result = authRepository.register(kodePegawai, email, password, nama)

                btnSignup.isEnabled = true

                if (result?.success == true && result.data != null) {
                    val user = result.data

                    // Simpan data login ke PrefManager
                    pref.saveLogin(user.nama, user.email, user.kodePegawai)

                    Toast.makeText(
                        this@SignupActivity,
                        "Pendaftaran berhasil!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    intent.putExtra("userName", user.nama)
                    intent.putExtra("userEmail", user.email)
                    intent.putExtra("userStatusKeanggotaan", user.statusKeanggotaan)
                    intent.putExtra("userKodePegawai", user.kodePegawai)
                    intent.putExtra("isAdmin", false)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        result?.message ?: "Pendaftaran gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
