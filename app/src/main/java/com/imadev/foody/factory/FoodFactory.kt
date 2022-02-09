package com.imadev.foody.factory

import com.imadev.foody.model.Food

object FoodFactory {

    fun foodList() = mutableListOf<Food>(
        Food(),
        Food(),
        Food(),
        Food(),
        Food(),
    )

}