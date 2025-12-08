package com.example.projek_map.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.data.User
import com.example.projek_map.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            doSignup()
        }
    }

    private fun doSignup() {
        val kodePegawai = binding.inputKodePegawai.text?.toString()?.trim()
        val nama = binding.inputNama.text?.toString()?.trim()
        val email = binding.inputEmail.text?.toString()?.trim()
        val password = binding.inputPassword.text?.toString()?.trim()
        val confirm = binding.inputConfirmPassword.text?.toString()?.trim()

        if (kodePegawai.isNullOrEmpty() ||
            nama.isNullOrEmpty() ||
            email.isNullOrEmpty() ||
            password.isNullOrEmpty() ||
            confirm.isNullOrEmpty()
        ) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirm) {
            Toast.makeText(this, "Konfirmasi password tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        if (DummyUserData.users.any { it.kodePegawai == kodePegawai }) {
            Toast.makeText(this, "Kode pegawai sudah terdaftar", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(
            kodePegawai = kodePegawai,
            email = email,
            password = password,
            nama = nama,
            statusKeanggotaan = "Anggota Aktif"
        )

        DummyUserData.users.add(newUser)
        Toast.makeText(this, "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show()
        finish()
    }
}
