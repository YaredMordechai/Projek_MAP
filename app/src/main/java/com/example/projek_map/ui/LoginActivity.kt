package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.MainActivity
import com.example.projek_map.api.ApiClient
import com.example.projek_map.databinding.ActivityLoginBinding
import com.example.projek_map.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            } else {
                ApiClient.instance.loginUser(email, password)
                    .enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    response.body()?.message ?: "Login gagal",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}
