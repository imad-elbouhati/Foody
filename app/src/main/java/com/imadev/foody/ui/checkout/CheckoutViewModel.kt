package com.imadev.foody.ui.checkout

import com.imadev.foody.model.Food
import com.imadev.foody.ui.common.BaseViewModel
import com.imadev.foody.utils.formatDecimal

class CheckoutViewModel : BaseViewModel() {

    var cartList:MutableList<Food> = mutableListOf()


    fun getTotal(): String {
        return formatDecimal(cartList.sumOf { it.quantity * it.price })
    }
}