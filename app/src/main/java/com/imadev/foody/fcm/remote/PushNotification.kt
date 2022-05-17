package com.imadev.foody.fcm.remote

import com.imadev.foody.model.Order

data class PushNotification(
    val data: Order,
    val notification: Notification,
    val to: String
)