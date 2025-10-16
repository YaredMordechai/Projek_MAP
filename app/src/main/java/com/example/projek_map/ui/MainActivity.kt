package com.example.projek_map.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.databinding.ActivityMainBinding
import com.example.projek_map.ui.fragments.*
import com.example.projek_map.utils.NotificationHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar
    private var isAdmin: Boolean = false
    private var toggle: ActionBarDrawerToggle? = null

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* ignored */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
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

        // 🔹 setup views
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 🔹 setup drawer (only if admin)
        if (isAdmin) {
            navigationView.visibility = View.VISIBLE
            toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle!!)
            toggle!!.syncState()

            navigationView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_kelola_anggota -> {
                        toolbar.title = "Kelola Anggota"
                        loadFragment(KelolaAnggotaFragment())
                    }
                    R.id.navigation_kelola_simpanan -> {
                        toolbar.title = "Kelola Simpanan"
                        loadFragment(KelolaSimpananFragment())
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }

        // 🔹 fragment awal
        if (savedInstanceState == null) {
            toolbar.title = "Dashboard"
            loadFragment(DashboardFragment())
        }

        // 🔹 bottom nav
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    toolbar.title = "Dashboard"
                    loadFragment(DashboardFragment())
                }
                R.id.navigation_pengumuman -> {
                    toolbar.title = "Pengumuman"
                    loadFragment(PengumumanFragment())
                }
                R.id.navigation_simpanan -> {
                    toolbar.title = "Simpanan"
                    loadFragment(SimpananFragment())
                }
                R.id.navigation_pinjaman -> {
                    toolbar.title = "Pinjaman"
                    loadFragment(PinjamanFragment())
                }
                R.id.navigation_profil -> {
                    toolbar.title = "Profil"
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
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toggle?.onOptionsItemSelected(item) ?: super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment: Fragment) {
        val bundle = fragment.arguments ?: Bundle()
        bundle.putBoolean("isAdmin", isAdmin)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}