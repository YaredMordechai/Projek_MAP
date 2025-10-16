package com.example.projek_map.data

data class TransaksiSimpanan(
    val id: Int,
    val kodePegawai: String,
    val jenis: String,      // "Pokok", "Wajib", atau "Sukarela"
    val jumlah: Double,
    val tanggal: String
)