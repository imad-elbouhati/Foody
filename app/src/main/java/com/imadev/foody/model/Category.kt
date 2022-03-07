package com.imadev.foody.model

import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId val id: String="",
    val name: String
){
    @Suppress("unused")
    constructor() : this("", "")
}