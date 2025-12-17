package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.BuktiPembayaranAnggotaRequest
import com.example.projek_map.api.SimpananPendingDecideRequest
import com.example.projek_map.api.SimpananTransaksiRequest

class SimpananRepository {

    private val api = ApiClient.apiService

    // USER
    suspend fun getSimpanan(kodePegawai: String) = api.getSimpanan(kodePegawai)
    suspend fun getHistoriByKodePegawai(kodePegawai: String) = api.getHistoriSimpananByKodePegawai(kodePegawai)
    suspend fun transaksi(req: SimpananTransaksiRequest) = api.simpananTransaksi(req)
    suspend fun uploadBukti(req: BuktiPembayaranAnggotaRequest) = api.addBuktiPembayaranAnggota(req)

    // ADMIN
    suspend fun getAllSimpanan() = api.getAllSimpanan()
    suspend fun getAllHistori() = api.getAllHistoriSimpanan()

    // PENDING (ADMIN)
    suspend fun getPending() = api.getSimpananPending()
    suspend fun decidePending(req: SimpananPendingDecideRequest) = api.decideSimpananPending(req)
}
