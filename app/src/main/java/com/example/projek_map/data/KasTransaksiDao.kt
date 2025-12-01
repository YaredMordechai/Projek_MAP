package com.example.projek_map.data

import androidx.room.*

@Dao
interface KasTransaksiDao {

    @Query("SELECT * FROM kas_transaksi ORDER BY tanggal DESC, id DESC")
    suspend fun getAllTransaksi(): List<KasTransaksi>

    @Query("SELECT * FROM kas_transaksi WHERE id = :id LIMIT 1")
    suspend fun getTransaksiById(id: Int): KasTransaksi?

    @Query("SELECT MAX(id) FROM kas_transaksi")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: KasTransaksi)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransaksi(list: List<KasTransaksi>)

    @Update
    suspend fun updateTransaksi(transaksi: KasTransaksi)

    @Delete
    suspend fun deleteTransaksi(transaksi: KasTransaksi)

    // Untuk getSaldoKas(): Masuk (+), Keluar (-)
    @Query("""
        SELECT COALESCE(SUM(
            CASE 
                WHEN jenis = 'Masuk' THEN jumlah 
                ELSE -jumlah 
            END
        ), 0.0)
        FROM kas_transaksi
    """)
    suspend fun getSaldoKas(): Double
}
