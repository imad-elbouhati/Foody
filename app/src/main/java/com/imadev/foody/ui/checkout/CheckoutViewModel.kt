package com.imadev.foody.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.common.BaseViewModel
import com.imadev.foody.utils.formatDecimal

private const val TAG = "CheckoutViewModel"

class CheckoutViewModel : BaseViewModel() {


    private var _cartList: MutableList<Meal> = mutableListOf()

    val cartList = _cartList as List<Meal>

    fun getTotal(): String {
        return formatDecimal(_cartList.sumOf { it.quantity * it.price })
    }

    fun addToCart(meal: Meal, position: Int) {
        _cartList.add(position, meal)
        updateCartEmptiness()
    }

    fun addToCart(meal: Meal) {
        _cartList.add(meal)
        updateCartEmptiness()
    }

    fun removeFromCart(meal: Meal) {
        _cartList.remove(meal)

        updateCartEmptiness()
    }

    fun removeFromCart(position: Int) {
        _cartList.removeAt(position)
    }

    private val _cartIsEmpty = MutableLiveData<Boolean>()
    val cartIsEmpty: LiveData<Boolean> = _cartIsEmpty

    val canProceedToPayment = MutableLiveData<Boolean>()


    init {

        updateCartEmptiness()
        observeQuantity()
    }

    private fun updateCartEmptiness() {
        _cartIsEmpty.postValue(cartList.isEmpty())
    }


    fun observeQuantity() {
        canProceedToPayment.postValue(cartList.fold(1) { acc, meal -> acc * meal.quantity } > 0)
    }


}