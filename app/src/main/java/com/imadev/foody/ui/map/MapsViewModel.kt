package com.imadev.foody.ui.map

import androidx.lifecycle.MutableLiveData
import com.imadev.foody.model.Address
import com.imadev.foody.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MapsViewModel:BaseViewModel() {

    val address = MutableLiveData<Address>()
    // val address = _address.asStateFlow()



}