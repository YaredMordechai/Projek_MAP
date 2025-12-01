package com.example.projek_map.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BuktiPembayaranAnggotaDao {

    @Query("SELECT * FROM bukti_pembayaran_anggota WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    suspend fun getBuktiForUser(kode: String): List<BuktiPembayaranAnggota>

    @Query("SELECT MAX(id) FROM bukti_pembayaran_anggota")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBukti(item: BuktiPembayaranAnggota)
}
