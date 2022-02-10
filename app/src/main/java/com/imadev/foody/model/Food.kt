package com.imadev.foody.model

import com.imadev.foody.R
import java.text.DecimalFormat

data class Food(
    val image: Int = R.drawable.foodd,
    val title: String = "Veggie tomato mix",
    private val price: Double = 180.5,
    val quantity: Int = 0
) {



    private val decimalFormat = DecimalFormat("#.##")

    var formattedPrice: String = decimalFormat.format(price)


}