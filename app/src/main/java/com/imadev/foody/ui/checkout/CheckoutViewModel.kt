package com.imadev.foody.ui.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imadev.foody.fcm.remote.PushNotification
import com.imadev.foody.model.*
import com.imadev.foody.repository.FoodyRepo
import com.imadev.foody.ui.common.BaseViewModel
import com.imadev.foody.utils.Constants
import com.imadev.foody.utils.Resource
import com.imadev.foody.utils.formatDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CheckoutViewModel"

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    val repository: FoodyRepo
) : BaseViewModel() {


    private val _client = MutableStateFlow<Resource<Client?>>(Resource.Loading())
    val client = _client.asStateFlow()

    private var _cartList: MutableList<Meal> = mutableListOf()
    val cartList = _cartList as List<Meal>

    private var _availableDeliveryUsers =
        MutableSharedFlow<Resource<List<DeliveryUser?>>>()
    val availableDeliveryUsers = _availableDeliveryUsers.asSharedFlow()


    var order = Order()
        private set

    fun setOrder(order: Order) {
        this.order = order
    }

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


    fun getClient(uid: String) = viewModelScope.launch {
        repository.getClient(uid).collectLatest {
            _client.emit(it)
        }
    }


    fun updateAddress(uid: String, address: Address) = GlobalScope.launch {
        repository.updateField(Constants.CLIENTS_COLLECTION, uid, "address", address).collect {
            when (it) {
                is Resource.Error -> {
                    Log.d(TAG, "updateAddress: ${it.error?.message}")
                }
                is Resource.Loading -> {


                }
                is Resource.Success -> {
                    Log.d(TAG, "updateAddress: success")
                }
            }
        }
    }


    private fun sendNotification(pushNotification: PushNotification) = viewModelScope.launch {
        val response = repository.sendNotification(pushNotification)
        Log.d(TAG, "sendNotification: ${response.isSuccessful}")
    }


    fun getAvailableDeliveryUsers() {
        viewModelScope.launch {
            repository.getAvailableDeliveryUsers().collect {
                _availableDeliveryUsers.emit(it)
            }
        }
    }


    fun sendOrderToDeliveryUser(order: Order,pushNotification: PushNotification) = viewModelScope.launch {
        repository.sendOrderToDeliveryUser(order).collectLatest { 
            when(it) {
                is Resource.Error ->{
                    Log.d(TAG, "sendOrderToDeliveryUser: ${it.error?.message}")
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
            }
        }
        sendNotification(pushNotification)
    }

}