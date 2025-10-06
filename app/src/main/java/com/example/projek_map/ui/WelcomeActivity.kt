package com.example.projek_map.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_map.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Referensi TextView
        val tvAppName = findViewById<TextView>(R.id.tvAppName)

        // Ambil animasi
        val fadeInZoom = AnimationUtils.loadAnimation(this, R.anim.fade_in_zoom)
        tvAppName.startAnimation(fadeInZoom)

        // Setelah animasi selesai, pindah ke LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000) // delay 3 detik
    }
}
