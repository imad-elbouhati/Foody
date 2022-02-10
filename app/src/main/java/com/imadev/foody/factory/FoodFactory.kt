package com.imadev.foody.factory

import com.imadev.foody.R
import com.imadev.foody.model.Food

object FoodFactory {

    fun foodList() = mutableListOf(
        Food(image = R.drawable.food2, price = 35.0),
        Food(image = R.drawable.food3,price = 40.0),
        Food(image = R.drawable.food4,price = 50.0),
        Food(image = R.drawable.food5,price = 55.0),
        Food(image = R.drawable.food6,price = 38.0),
    )

}