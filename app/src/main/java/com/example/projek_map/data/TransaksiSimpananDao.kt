package com.example.projek_map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransaksiSimpananDao {

    @Query("SELECT * FROM transaksi_simpanan ORDER BY tanggal DESC, id DESC")
    suspend fun getAllTransaksi(): List<TransaksiSimpanan>

    @Query("SELECT * FROM transaksi_simpanan WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    suspend fun getTransaksiForUser(kode: String): List<TransaksiSimpanan>

    @Query("SELECT MAX(id) FROM transaksi_simpanan")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransaksi(list: List<TransaksiSimpanan>)

    @Update
    suspend fun updateTransaksi(item: TransaksiSimpanan)

    @Delete
    suspend fun deleteTransaksi(item: TransaksiSimpanan)
}
