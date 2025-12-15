package com.example.projek_map.data

import com.example.projek_map.api.KoperasiApiService
import com.example.projek_map.api.MarkReadRequest
import com.example.projek_map.api.DecisionNotificationDto

class DecisionNotificationRepository(
    private val api: KoperasiApiService
) {

    suspend fun fetchUnread(kodePegawai: String): List<DecisionNotificationDto> {
        val res = api.getDecisionNotifications(kodePegawai, onlyUnread = 1)
        val body = res.body()
        return if (res.isSuccessful && body?.success == true) {
            body.data.orEmpty()
        } else {
            emptyList()
        }
    }

    suspend fun markRead(ids: List<Int>) {
        if (ids.isEmpty()) return
        api.markDecisionNotificationsRead(MarkReadRequest(ids))
    }
}
