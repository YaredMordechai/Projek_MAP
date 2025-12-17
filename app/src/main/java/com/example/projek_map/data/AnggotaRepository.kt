package com.example.projek_map.data

import com.example.projek_map.api.AddUserRequest
import com.example.projek_map.api.DeleteUserRequest
import com.example.projek_map.api.KoperasiApiService
import com.example.projek_map.api.SetUserStatusRequest
import com.example.projek_map.api.UpdateUserRequest
import com.example.projek_map.api.User

sealed class RepoResult<out T> {
    data class Success<T>(val data: T, val message: String? = null) : RepoResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : RepoResult<Nothing>()
}

class AnggotaRepository(
    private val api: KoperasiApiService
) {
    suspend fun getAllUsers(): RepoResult<List<User>> {
        return try {
            val res = api.getAllUsers()
            val body = res.body()
            if (res.isSuccessful && body?.success == true) {
                RepoResult.Success(body.data.orEmpty(), body.message)
            } else {
                RepoResult.Error(body?.message ?: "Gagal memuat anggota")
            }
        } catch (e: Exception) {
            RepoResult.Error("Error: ${e.message}", e)
        }
    }

    suspend fun addUser(req: AddUserRequest): RepoResult<Unit> {
        return try {
            val res = api.addUser(req)
            val body = res.body()
            if (res.isSuccessful && body?.success == true) {
                RepoResult.Success(Unit, body.message ?: "Anggota ditambahkan")
            } else {
                RepoResult.Error(body?.message ?: "Gagal tambah anggota")
            }
        } catch (e: Exception) {
            RepoResult.Error("Error: ${e.message}", e)
        }
    }

    suspend fun updateUser(req: UpdateUserRequest): RepoResult<Unit> {
        return try {
            val res = api.updateUser(req)
            val body = res.body()
            if (res.isSuccessful && body?.success == true) {
                RepoResult.Success(Unit, body.message ?: "Data diperbarui")
            } else {
                RepoResult.Error(body?.message ?: "Gagal update")
            }
        } catch (e: Exception) {
            RepoResult.Error("Error: ${e.message}", e)
        }
    }

    suspend fun setUserStatus(req: SetUserStatusRequest): RepoResult<Unit> {
        return try {
            val res = api.setUserStatus(req)
            val body = res.body()
            if (res.isSuccessful && body?.success == true) {
                RepoResult.Success(Unit, body.message ?: "Status diubah")
            } else {
                RepoResult.Error(body?.message ?: "Gagal ubah status")
            }
        } catch (e: Exception) {
            RepoResult.Error("Error: ${e.message}", e)
        }
    }

    suspend fun deleteUser(req: DeleteUserRequest): RepoResult<Unit> {
        return try {
            val res = api.deleteUser(req)
            val body = res.body()
            if (res.isSuccessful && body?.success == true) {
                RepoResult.Success(Unit, body.message ?: "Anggota dihapus")
            } else {
                RepoResult.Error(body?.message ?: "Gagal hapus")
            }
        } catch (e: Exception) {
            RepoResult.Error("Error: ${e.message}", e)
        }
    }
}
