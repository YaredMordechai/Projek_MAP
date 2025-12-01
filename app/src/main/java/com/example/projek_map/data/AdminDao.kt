package com.example.projek_map.data

import androidx.room.*

@Dao
interface AdminDao {

    @Query("SELECT * FROM admins")
    suspend fun getAllAdmins(): List<Admin>

    @Query("SELECT * FROM admins WHERE kodePegawai = :kode LIMIT 1")
    suspend fun getAdminByKode(kode: String): Admin?

    @Query("SELECT * FROM admins WHERE email = :email LIMIT 1")
    suspend fun getAdminByEmail(email: String): Admin?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAdmin(admin: Admin)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAdmins(admins: List<Admin>)

    @Update
    suspend fun updateAdmin(admin: Admin)

    @Delete
    suspend fun deleteAdmin(admin: Admin)

    @Query("SELECT COUNT(*) FROM admins")
    suspend fun countAdmins(): Int
}
