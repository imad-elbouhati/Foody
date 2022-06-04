package com.imadev.foody.model

class Client(
    val username: String? = null,
    var address: Address? = null,
    val phone: String? = null,
    val email: String? = null,
    val token: String? = null
) {
    override fun toString(): String {
        return "$username $address $phone $email $token"
    }
}