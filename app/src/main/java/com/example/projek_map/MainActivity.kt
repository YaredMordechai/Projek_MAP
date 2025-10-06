package com.example.projek_map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.projek_map.databinding.ActivityMainBinding
import com.example.projek_map.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var userNama: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data user dari intent (dummy login)
        userNama = intent.getStringExtra("userName") ?: ""

        // Jika user belum login (tidak ada nama)
        if (userNama.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Set fragment default (Dashboard)
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        // Setup bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }
                R.id.navigation_simpanan -> {
                    loadFragment(SimpananFragment())
                    true
                }
                R.id.navigation_pinjaman -> {
                    val fragment = PinjamanFragment()
                    val bundle = Bundle()
                    bundle.putString("nama", userNama)
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }
                R.id.navigation_profil -> {
                    val fragment = ProfilFragment()
                    val bundle = Bundle()
                    bundle.putString("nama", userNama)
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
