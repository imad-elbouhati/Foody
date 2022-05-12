package com.imadev.foody.ui.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imadev.foody.model.Client
import com.imadev.foody.model.Meal
import com.imadev.foody.repository.FoodyRepo
import com.imadev.foody.repository.FoodyRepoImp
import com.imadev.foody.ui.common.BaseViewModel
import com.imadev.foody.utils.formatDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CheckoutViewModel"

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    val repository: FoodyRepo
) : BaseViewModel() {


    val client = MutableStateFlow<Client?>(null)

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

    fun resetList() {
        _cartList.clear()
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


    fun getClient(uid:String) = viewModelScope.launch {
        repository.getClient(uid).collectLatest {
            Log.d(TAG, "getClient: ")
            client.emit(it)
        }
    }

}