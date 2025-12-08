package com.example.projek_map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.Admin
import com.example.projek_map.data.HistoriPembayaran
import com.example.projek_map.data.HistoriSimpanan
import com.example.projek_map.data.KasTransaksi
import com.example.projek_map.data.Pengumuman
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.Simpanan
import com.example.projek_map.data.User
import kotlinx.coroutines.launch

class KoperasiViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = KoperasiRepository()

    // ==== login user ====
    private val _loginUserResult = MutableLiveData<User?>()
    val loginUserResult: LiveData<User?> = _loginUserResult

    // ==== login admin ====
    private val _loginAdminResult = MutableLiveData<Admin?>()
    val loginAdminResult: LiveData<Admin?> = _loginAdminResult

    // ==== anggota ====
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    // ==== simpanan user ====
    private val _simpananUser = MutableLiveData<Simpanan?>()
    val simpananUser: LiveData<Simpanan?> = _simpananUser

    private val _historiSimpananUser = MutableLiveData<List<HistoriSimpanan>>()
    val historiSimpananUser: LiveData<List<HistoriSimpanan>> = _historiSimpananUser

    // ==== pinjaman user ====
    private val _pinjamanUser = MutableLiveData<List<Pinjaman>>()
    val pinjamanUser: LiveData<List<Pinjaman>> = _pinjamanUser

    private val _historiPembayaran = MutableLiveData<List<HistoriPembayaran>>()
    val historiPembayaran: LiveData<List<HistoriPembayaran>> = _historiPembayaran

    // ==== pengumuman & kas ====
    private val _pengumuman = MutableLiveData<List<Pengumuman>>()
    val pengumuman: LiveData<List<Pengumuman>> = _pengumuman

    private val _kas = MutableLiveData<List<KasTransaksi>>()
    val kas: LiveData<List<KasTransaksi>> = _kas

    // ================== FUNGSI ==================

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginUserResult.value = repo.loginUser(email, password)
        }
    }

    fun loginAdmin(email: String, password: String) {
        viewModelScope.launch {
            _loginAdminResult.value = repo.loginAdmin(email, password)
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repo.getAllUsers()
        }
    }

    fun tambahAnggota(nama: String, email: String) {
        viewModelScope.launch {
            repo.tambahAnggota(nama, email)
            loadUsers()
        }
    }

    fun nonaktifkanAnggota(user: User) {
        viewModelScope.launch {
            repo.nonaktifkanAnggota(user)
            loadUsers()
        }
    }

    fun hapusAnggota(user: User) {
        viewModelScope.launch {
            repo.hapusAnggota(user)
            loadUsers()
        }
    }

    fun loadSimpananUser(kodePegawai: String) {
        viewModelScope.launch {
            _simpananUser.value = repo.getSimpananUser(kodePegawai)
            _historiSimpananUser.value = repo.getHistoriSimpananUser(kodePegawai)
        }
    }

    fun tambahSimpanan(
        kodePegawai: String,
        jenis: String,
        jumlah: Double,
        keterangan: String
    ) {
        viewModelScope.launch {
            val ok = repo.tambahSimpanan(kodePegawai, jenis, jumlah, keterangan)
            if (ok) {
                loadSimpananUser(kodePegawai)
            }
        }
    }

    fun loadPinjamanUser(kodePegawai: String) {
        viewModelScope.launch {
            _pinjamanUser.value = repo.getPinjamanUser(kodePegawai)
        }
    }

    fun loadHistoriPembayaran(pinjamanId: Int) {
        viewModelScope.launch {
            _historiPembayaran.value = repo.getHistoriPembayaran(pinjamanId)
        }
    }

    fun ajukanPinjaman(kodePegawai: String, jumlah: Int, tenor: Int) {
        viewModelScope.launch {
            repo.ajukanPinjaman(kodePegawai, jumlah, tenor)
            loadPinjamanUser(kodePegawai)
        }
    }

    fun loadPengumuman() {
        viewModelScope.launch {
            _pengumuman.value = repo.getPengumuman()
        }
    }

    fun loadKas() {
        viewModelScope.launch {
            _kas.value = repo.getKas()
        }
    }
}