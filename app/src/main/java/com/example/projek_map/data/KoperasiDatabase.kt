package com.example.projek_map.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        Admin::class,
        Pinjaman::class,
        Simpanan::class,
        HistoriPembayaran::class,
        HistoriSimpanan::class,
        BuktiPembayaranAnggota::class,
        Pengumuman::class,
        TransaksiSimpanan::class,
        KasTransaksi::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KoperasiDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun adminDao(): AdminDao
    abstract fun pinjamanDao(): PinjamanDao
    abstract fun simpananDao(): SimpananDao
    abstract fun historiPembayaranDao(): HistoriPembayaranDao
    abstract fun historiSimpananDao(): HistoriSimpananDao
    abstract fun buktiPembayaranAnggotaDao(): BuktiPembayaranAnggotaDao
    abstract fun transaksiSimpananDao(): TransaksiSimpananDao
    abstract fun kasTransaksiDao(): KasTransaksiDao
    abstract fun pengumumanDao(): PengumumanDao

    companion object {
        @Volatile
        private var INSTANCE: KoperasiDatabase? = null

        fun getInstance(context: Context): KoperasiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KoperasiDatabase::class.java,
                    "koperasi_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
