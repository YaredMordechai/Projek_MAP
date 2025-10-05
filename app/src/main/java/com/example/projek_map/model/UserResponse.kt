package com.example.projek_map.model

data class UserResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null
)

