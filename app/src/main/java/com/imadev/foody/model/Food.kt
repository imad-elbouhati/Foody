package com.imadev.foody.model

import android.os.Parcelable
import com.imadev.foody.R
import com.imadev.foody.utils.formatDecimal
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Parcelize
data class Food(
    val image: Int = R.drawable.ic_launcher_background,
    val title: String = "Veggie tomato mix",
    val price: Double = 0.0,
    var quantity: Int = 0
) : Parcelable {


    @IgnoredOnParcel
    var formattedPrice: String = formatDecimal(price)


}

