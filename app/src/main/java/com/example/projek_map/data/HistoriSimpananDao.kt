package com.example.projek_map.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoriSimpananDao {

    @Query("SELECT * FROM histori_simpanan")
    suspend fun getAllHistori(): List<HistoriSimpanan>

    @Query("SELECT * FROM histori_simpanan WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    suspend fun getHistoriForUser(kode: String): List<HistoriSimpanan>

    @Query("SELECT MAX(id) FROM histori_simpanan")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistori(item: HistoriSimpanan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHistori(list: List<HistoriSimpanan>)
}
