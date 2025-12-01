package com.example.projek_map.data

import android.content.Context

class KoperasiRepository(context: Context) {

    private val db = KoperasiDatabase.getInstance(context)

    private val userDao = db.userDao()
    private val adminDao = db.adminDao()
    private val pinjamanDao = db.pinjamanDao()
    private val simpananDao = db.simpananDao()
    private val historiPembayaranDao = db.historiPembayaranDao()
    private val historiSimpananDao = db.historiSimpananDao()
    private val buktiPembayaranAnggotaDao = db.buktiPembayaranAnggotaDao()
    private val transaksiSimpananDao = db.transaksiSimpananDao()
    private val kasTransaksiDao = db.kasTransaksiDao()
    private val pengumumanDao = db.pengumumanDao()

    // --- PREPOPULATE (pindahkan semua dari DummyUserData ke DB sekali saja) ---
    suspend fun prepopulateIfNeeded() {
        if (userDao.countUsers() > 0) return

        // 1. Users
        userDao.insertUsers(DummyUserData.users)

        // 2. Admins
        adminDao.insertAdmins(DummyUserData.admins)

        // 3. Simpanan
        simpananDao.insertAllSimpanan(DummyUserData.simpananList)

        // 4. Pinjaman
        pinjamanDao.insertAllPinjaman(DummyUserData.pinjamanList)

        // 5. Histori pembayaran
        historiPembayaranDao.insertAllHistori(DummyUserData.historiPembayaranList)

        // 6. Histori simpanan
        historiSimpananDao.insertAllHistori(DummyUserData.historiSimpananList)

        // 7. Transaksi simpanan
        transaksiSimpananDao.insertAllTransaksi(DummyUserData.transaksiSimpananList)

        // 8. Kas transaksi
        kasTransaksiDao.insertAllTransaksi(DummyUserData.kasTransaksiList)

        // 9. Pengumuman
        pengumumanDao.insertAllPengumuman(DummyUserData.pengumumanList)
    }

    // ======================
    // ========== AUTH ======
    // ======================

    // login user via email ATAU kode pegawai
    suspend fun loginUserByIdentifier(identifier: String, password: String): User? {
        val userByEmail = userDao.getUserByEmail(identifier)
        val userByKode = userDao.getUserByKode(identifier)
        val user = userByEmail ?: userByKode ?: return null
        return if (user.password == password) user else null
    }

    // login admin via email ATAU kode pegawai
    suspend fun loginAdminByIdentifier(identifier: String, password: String): Admin? {
        val adminByEmail = adminDao.getAdminByEmail(identifier)
        val adminByKode = adminDao.getAdminByKode(identifier)
        val admin = adminByEmail ?: adminByKode ?: return null
        return if (admin.password == password) admin else null
    }

    // ==========================
    // ========== ANGGOTA =======
    // ==========================

    suspend fun getAllUsers(): List<User> = userDao.getAllUsers()

    suspend fun tambahAnggota(nama: String, email: String): User {
        val all = userDao.getAllUsers()
        val nextNumber = all.size + 1
        val kodePegawai = "EMP" + nextNumber.toString().padStart(3, '0')

        val user = User(
            kodePegawai = kodePegawai,
            email = email,
            password = "1234",
            nama = nama,
            statusKeanggotaan = "Anggota Aktif"
        )
        userDao.insertUser(user)
        return user
    }

    suspend fun updateAnggota(kodePegawai: String, namaBaru: String, emailBaru: String) {
        val existing = userDao.getUserByKode(kodePegawai) ?: return
        val updated = existing.copy(
            nama = namaBaru,
            email = emailBaru
        )
        userDao.updateUser(updated)
    }

    suspend fun nonaktifkanAnggota(user: User) {
        val updated = user.copy(statusKeanggotaan = "Nonaktif")
        userDao.updateUser(updated)
    }

    suspend fun hapusAnggota(user: User) {
        userDao.deleteUser(user)
    }

    // ==========================
    // ========== SIGNUP ========
    // ==========================

    suspend fun isUserExist(kodePegawai: String, email: String): Boolean {
        val byKode = userDao.getUserByKode(kodePegawai)
        val byEmail = userDao.getUserByEmail(email)
        return byKode != null || byEmail != null
    }

    suspend fun registerUser(
        kodePegawai: String,
        email: String,
        password: String,
        nama: String
    ): User {
        val user = User(
            kodePegawai = kodePegawai,
            email = email,
            password = password,
            nama = nama,
            statusKeanggotaan = "Anggota Baru"
        )
        userDao.insertUser(user)
        return user
    }
}
