package com.example.projek_map.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.utils.NotificationHelper // 🔔
import com.example.projek_map.utils.AlarmReceiver // 🔔
import java.util.Calendar
import com.google.android.material.button.MaterialButton
import android.widget.LinearLayout

class DashboardFragment : Fragment() {

    private var isAdmin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)

        // ✅ Disesuaikan ke CardView (bukan MaterialButton)
        val cardSimpanan = view.findViewById<CardView>(R.id.cardSimpanan)
        val cardPinjaman = view.findViewById<CardView>(R.id.cardPinjaman)
        val cardLaporan = view.findViewById<CardView>(R.id.cardLaporan)
        val cardProfil = view.findViewById<CardView>(R.id.cardProfil)
//        val cardLogout = view.findViewById<CardView>(R.id.cardLogout)

        val pref = PrefManager(requireContext())

        if (isAdmin) {
            tvWelcome.text = "Halo, Admin"
            tvUserId.text = ""
            cardSimpanan.visibility = View.GONE
            cardPinjaman.visibility = View.GONE
            cardLaporan.visibility = View.GONE
            cardProfil.visibility = View.GONE
        } else {
            val loggedEmail = pref.getEmail() ?: ""
            val loggedKode = pref.getKodePegawai() ?: ""

            val currentUser = DummyUserData.users.find {
                it.email.equals(loggedEmail, ignoreCase = true) || it.kodePegawai == loggedKode
            }

            if (currentUser != null) {
                tvWelcome.text = "Halo, ${currentUser.nama}"
                tvUserId.text = "Kode Pegawai: ${currentUser.kodePegawai}"
            } else {
                tvWelcome.text = "Halo, Pengguna"
                tvUserId.text = "Kode Pegawai: -"
            }

            imgProfile.setImageResource(R.drawable.ic_profil)
        }

        // ✅ Navigasi (tetap sama seperti versi sebelumnya)
        cardSimpanan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, SimpananFragment())
                .addToBackStack(null)
                .commit()
        }

        cardPinjaman.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, PinjamanFragment())
                .addToBackStack(null)
                .commit()
        }

        cardLaporan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, LaporanFragment())
                .addToBackStack(null)
                .commit()
        }

        cardProfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // 🔔 Schedule example alarm (daily at 09:00) to remind about due installments
        scheduleDailyJatuhTempo(requireContext(), 9, 0)

        // 🔔 Add a programmatic button at bottom of grid to "Kirim Pengumuman (Tes)".
        // This keeps original XML intact; kita hanya menambahkan view programmatically.
        val btnKirimPengumuman = view.findViewById<MaterialButton>(R.id.btnKirimPengumuman)
        if (isAdmin) {
            btnKirimPengumuman.visibility = View.VISIBLE
            btnKirimPengumuman.setOnClickListener {
                NotificationHelper.showNotification(
                    requireContext(),
                    2001,
                    "Pengumuman Koperasi",
                    "Halo anggota, ada pengumuman penting dari pengurus. Cek aplikasi."
                )
            }
        }

    }

    private fun scheduleDailyJatuhTempo(context: Context, hour: Int, minute: Int) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("type", "jatuh_tempo")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                3001,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    // jika waktu sudah lewat hari ini -> pangkas ke besok
                    add(Calendar.DATE, 1)
                }
            }

            // setRepeating deprecated di beberapa API; gunakan setInexactRepeating untuk efisiensi
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
