package com.imadev.foody.fcm.remote

import com.imadev.foody.model.Order

data class PushNotification(
    val notification: Notification,
    val to: String?
)