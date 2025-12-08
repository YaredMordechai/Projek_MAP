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
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        // Kalau sudah login, langsung ke MainActivity
        if (prefManager.isLoggedIn()) {
            goToMain()
            return
        }

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun doLogin() {
        val email = binding.inputEmail.text?.toString()?.trim() ?: ""
        val password = binding.inputPassword.text?.toString()?.trim() ?: ""

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Cek login sebagai admin dulu
        val admin = DummyUserData.admins.find { it.email == email && it.password == password }
        if (admin != null) {
            prefManager.saveLogin(
                kodePegawai = admin.kodePegawai,
                email = admin.email,
                nama = admin.nama,
                isAdmin = true
            )
            Toast.makeText(this, "Login sebagai Admin", Toast.LENGTH_SHORT).show()
            goToMain()
            return
        }

        // Cek login sebagai user
        val user = DummyUserData.users.find { it.email == email && it.password == password }
        if (user != null) {
            prefManager.saveLogin(
                kodePegawai = user.kodePegawai,
                email = user.email,
                nama = user.nama,
                isAdmin = false
            )
            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
            goToMain()
            return
        }

        Toast.makeText(this, "Email / password salah", Toast.LENGTH_SHORT).show()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
