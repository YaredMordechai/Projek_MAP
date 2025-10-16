package com.example.projek_map.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.databinding.ActivityMainBinding
import com.example.projek_map.ui.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.projek_map.ui.LoginActivity
import com.example.projek_map.ui.fragments.DashboardFragment
import com.example.projek_map.ui.fragments.KelolaAnggotaFragment
import com.example.projek_map.ui.fragments.LaporanFragment
// 🔔 import NotificationHelper
import com.example.projek_map.utils.NotificationHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isAdmin: Boolean = false

    // request permission launcher for POST_NOTIFICATIONS (Android 13+)
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // nothing special to do here, it's OK if denied — notifications just won't show on Android 13+.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔔 Create notification channel (safe to call on all API levels)
        NotificationHelper.createNotificationChannel(this)

        // 🔔 Request runtime permission for notifications on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val userName = intent.getStringExtra("userName") ?: ""
        val userEmail = intent.getStringExtra("userEmail") ?: ""
        val userTelepon = intent.getStringExtra("userTelepon") ?: ""
        val userStatus = intent.getStringExtra("userStatusKeanggotaan") ?: ""
        val userKodePegawai = intent.getStringExtra("userKodePegawai") ?: ""
        isAdmin = intent.getBooleanExtra("isAdmin", false)

        if (userName.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 🔹 Hide menu admin for normal users
        if (!isAdmin) {
            bottomNav.menu.findItem(R.id.navigation_kelola_anggota)?.isVisible = false
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> loadFragment(DashboardFragment())
                R.id.navigation_pengumuman -> loadFragment(PengumumanFragment())
                R.id.navigation_simpanan -> loadFragment(SimpananFragment())
                R.id.navigation_pinjaman -> loadFragment(PinjamanFragment())
                R.id.navigation_profil -> {
                    val fragment = ProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString("nama", userName)
                            putString("email", userEmail)
                            putString("telepon", userTelepon)
                            putString("statusKeanggotaan", userStatus)
                            putString("kodePegawai", userKodePegawai)
                        }
                    }
                    loadFragment(fragment)
                }
                R.id.navigation_kelola_anggota -> {
                    loadFragment(KelolaAnggotaFragment()) // ✅ admin only
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val bundle = fragment.arguments ?: Bundle()
        bundle.putBoolean("isAdmin", intent.getBooleanExtra("isAdmin", false))
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
