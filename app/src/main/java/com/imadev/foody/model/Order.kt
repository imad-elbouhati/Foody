package com.imadev.foody.model

import com.google.firebase.firestore.DocumentId


enum class PaymentMethod {
    CASH, CARD
}

data class Order(
    @DocumentId
    val id: String = "",
    val orderNumber: String = "",
    val date: Long = -1L,
    val meals: List<Meal> = listOf(),
    val client: Client = Client(),
    val paymentMethod: PaymentMethod = PaymentMethod.CARD,
    val accepted: Boolean = false,
    val to: String?="",
    var uid:String =""
)