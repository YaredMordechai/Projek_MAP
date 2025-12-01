package com.example.projek_map.data

import androidx.room.*

@Dao
interface HistoriPembayaranDao {

    @Query("SELECT * FROM histori_pembayaran ORDER BY tanggal DESC, id DESC")
    suspend fun getAllHistori(): List<HistoriPembayaran>

    @Query("SELECT * FROM histori_pembayaran WHERE pinjamanId = :pinjamanId ORDER BY tanggal DESC, id DESC")
    suspend fun getHistoriByPinjaman(pinjamanId: Int): List<HistoriPembayaran>

    @Query("SELECT * FROM histori_pembayaran WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    suspend fun getHistoriByUser(kode: String): List<HistoriPembayaran>

    @Query("SELECT * FROM histori_pembayaran WHERE kodePegawai = :kode AND buktiPembayaranUri IS NOT NULL ORDER BY tanggal DESC, id DESC")
    suspend fun getHistoriWithBuktiByUser(kode: String): List<HistoriPembayaran>

    // Untuk getTotalAngsuranDibayar()
    @Query("SELECT COALESCE(SUM(jumlah), 0) FROM histori_pembayaran WHERE pinjamanId = :pinjamanId")
    suspend fun getTotalTerbayarForPinjaman(pinjamanId: Int): Int

    // Untuk getTotalAngsuranBulanan(); bulanPattern misal: "%-07-%"
    @Query("""
        SELECT COALESCE(CAST(SUM(jumlah) AS REAL), 0.0) 
        FROM histori_pembayaran 
        WHERE kodePegawai = :kode AND tanggal LIKE :bulanPattern
    """)
    suspend fun getTotalAngsuranBulanan(kode: String, bulanPattern: String): Double

    @Query("SELECT MAX(id) FROM histori_pembayaran")
    suspend fun getMaxId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistori(histori: HistoriPembayaran)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHistori(list: List<HistoriPembayaran>)

    @Update
    suspend fun updateHistori(histori: HistoriPembayaran)

    @Delete
    suspend fun deleteHistori(histori: HistoriPembayaran)
}
