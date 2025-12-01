package com.example.projek_map.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SimpananDao {

    @Query("SELECT * FROM simpanan")
    suspend fun getAllSimpanan(): List<Simpanan>

    @Query("SELECT * FROM simpanan WHERE kodePegawai = :kode LIMIT 1")
    suspend fun getSimpananByKode(kode: String): Simpanan?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSimpanan(list: List<Simpanan>)
}
