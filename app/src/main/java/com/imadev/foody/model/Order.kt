package com.imadev.foody.model


enum class PaymentMethod {
    CASH, CARD
}

data class Order(
    val id: String = "",
    val orderNumber: String = "",
    val date: Long = -1L,
    val meals: List<Meal> = listOf(),
    val client: Client = Client(),
    val paymentMethod: PaymentMethod = PaymentMethod.CARD,
    val accepted: Boolean = false
)