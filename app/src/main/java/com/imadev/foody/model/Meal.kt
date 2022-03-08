package com.imadev.foody.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.imadev.foody.utils.formatDecimal
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    @DocumentId
    val id: String? = null,
    var name: String,
    var price: Double,
    var ingredient: List<String>,
    var image: String? = null,
    var favorite: Boolean = false,
    var categoryId: String? = null,
    var quantity: Int = 0
) : Parcelable {
    @Suppress("unused")
    constructor() : this("", "", 0.0, listOf(), "", false, "", 0)

    @IgnoredOnParcel
    @Exclude
    private var formattedPrice: String = formatDecimal(price)

}
