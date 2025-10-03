package com.example.projek_map.model

data class Pinjaman(
    val id: Int,
    val jenis: String,
    val nama: String,
    val jumlah: Long,
    val status: String,
    val tanggal: String
)