package com.example.projek_map.ui

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import java.util.Calendar

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
            } else {
                scheduleDailyDueCheck()
            }
        } else {
            scheduleDailyDueCheck()
        }


        // ✅ Jadwalkan pengecekan harian jatuh tempo & pengumuman (Mode B - AlarmReceiver tanpa extra "type")
        scheduleDailyDueCheck()

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
                    R.id.navigation_kelola_pinjaman -> {
                        toolbar.title = "Kelola Pinjaman"
                        loadFragment(KelolaPinjamanFragment())
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

    // =========================================
    // Alarm harian untuk due & pengumuman (Mode B)
    // =========================================
    private fun scheduleDailyDueCheck() {
        try {
            val am = getSystemService(AlarmManager::class.java)

            // Intent ke AlarmReceiver dengan action unik
            val intent = Intent(this, com.example.projek_map.utils.AlarmReceiver::class.java).apply {
                action = "com.example.projek_map.ALARM_DUE_CHECK_DAILY"
                // (opsional) untuk Mode A:
                // putExtra("type", "jatuh_tempo")
            }

            val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

            val pi = PendingIntent.getBroadcast(this, 9001, intent, flags)

            // Batalkan alarm lama agar tidak dobel
            am?.cancel(pi)

            // Jadwalkan ke jam tertentu (ubah sesuai kebutuhanmu)
            val cal = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 22)
                set(Calendar.MINUTE, 46)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            // === Exact jika diizinkan, kalau tidak fallback ke inexact ===
            val canExact = if (Build.VERSION.SDK_INT >= 31) {
                am?.canScheduleExactAlarms() == true
            } else true

            if (canExact) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
                } else {
                    am?.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
                }
            } else {
                // fallback: tidak butuh permission khusus, tetap jalan
                am?.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            }

        } catch (t: Throwable) {
            // Jika tetap kena SecurityException, fallback sekali lagi ke inexact supaya tidak crash
            android.util.Log.e("MainActivity", "scheduleDailyDueCheck failed", t)
            val am = getSystemService(AlarmManager::class.java)
            val intent = Intent(this, com.example.projek_map.utils.AlarmReceiver::class.java).apply {
                action = "com.example.projek_map.ALARM_DUE_CHECK_DAILY"
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            val pi = PendingIntent.getBroadcast(this, 9001, intent, flags)
            val fallbackTime = System.currentTimeMillis() + 2 * 60 * 1000 // 2 menit lagi
            am?.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, fallbackTime, pi)
        }
    }


    // (Opsional) untuk membatalkan alarm saat logout
    @Suppress("unused")
    private fun cancelDailyDueCheck() {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, com.example.projek_map.utils.AlarmReceiver::class.java)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val pi = PendingIntent.getBroadcast(this, 9001, intent, flags)
        am.cancel(pi)
    }
}
