package com.example.projek_map.data

import androidx.room.*

@Dao
interface PinjamanDao {

    @Query("SELECT * FROM pinjaman ORDER BY id DESC")
    suspend fun getAllPinjaman(): List<Pinjaman>

    @Query("SELECT * FROM pinjaman WHERE kodePegawai = :kode ORDER BY id DESC")
    suspend fun getPinjamanForUser(kode: String): List<Pinjaman>

    @Query("SELECT * FROM pinjaman WHERE id = :id LIMIT 1")
    suspend fun getPinjamanById(id: Int): Pinjaman?

    @Query("SELECT * FROM pinjaman WHERE status = 'Disetujui' OR status = 'Aktif'")
    suspend fun getPinjamanAktif(): List<Pinjaman>

    @Query("SELECT * FROM pinjaman WHERE status = 'Lunas'")
    suspend fun getPinjamanLunas(): List<Pinjaman>

    @Query("SELECT MAX(id) FROM pinjaman")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPinjaman(pinjaman: Pinjaman)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPinjaman(list: List<Pinjaman>)

    @Update
    suspend fun updatePinjaman(pinjaman: Pinjaman)

    @Delete
    suspend fun deletePinjaman(pinjaman: Pinjaman)

    @Query("UPDATE pinjaman SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    // Untuk fitur "setSukuBungaBaru" di DummyUserData
    @Query("UPDATE pinjaman SET bunga = :bunga WHERE status = 'Proses' OR status = 'Disetujui'")
    suspend fun updateBungaUntukPinjamanAktif(bunga: Double)
}
