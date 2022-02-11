package com.imadev.foody.model

import android.os.Parcelable
import com.imadev.foody.R
import com.imadev.foody.utils.formatDecimal
import kotlinx.parcelize.Parcelize


@Parcelize
data class Food(
    val image: Int = R.drawable.foodd,
    val title: String = "Veggie tomato mix",
    val price: Double = 0.0,
    var quantity: Int = 0
) : Parcelable {


    var formattedPrice: String = formatDecimal(price)


}

