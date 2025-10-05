package com.example.projek_map.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.api.ApiClient
import com.example.projek_map.databinding.ActivitySignupBinding
import com.example.projek_map.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.instance.registerUser(nama, email, password)
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        val body = response.body()
                        if (response.isSuccessful && body?.success == true) {
                            Toast.makeText(this@SignupActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@SignupActivity, body?.message ?: "Registrasi gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(this@SignupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
