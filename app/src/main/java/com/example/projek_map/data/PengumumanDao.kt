package com.example.projek_map.data

import androidx.room.*

@Dao
interface PengumumanDao {

    @Query("SELECT * FROM pengumuman ORDER BY tanggal DESC, id DESC")
    suspend fun getAllPengumuman(): List<Pengumuman>

    @Query("SELECT * FROM pengumuman WHERE id = :id LIMIT 1")
    suspend fun getPengumumanById(id: Int): Pengumuman?

    @Query("SELECT MAX(id) FROM pengumuman")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengumuman(pengumuman: Pengumuman)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPengumuman(list: List<Pengumuman>)

    @Update
    suspend fun updatePengumuman(pengumuman: Pengumuman)

    @Delete
    suspend fun deletePengumuman(pengumuman: Pengumuman)
}
