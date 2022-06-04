package com.imadev.foody.model

import com.google.firebase.firestore.DocumentId

data class DeliveryUser(
    @DocumentId val id: String?="",
    val username: String? = "",
    val token: String? = "",
    val available: Boolean = true
)