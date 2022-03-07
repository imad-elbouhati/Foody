package com.imadev.foody.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.imadev.foody.db.FireStoreManager
import com.imadev.foody.db.FireStoreRepo
import com.imadev.foody.model.Category
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.common.BaseViewModel
import com.imadev.foody.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.w3c.dom.Document

class HomeViewModel() : BaseViewModel() {
    private val repository = FireStoreManager()

    private val _categories = MutableStateFlow<Resource<List<Category?>>>(Resource.Loading())
    val categories = _categories.asSharedFlow()


    private val _meals = MutableSharedFlow<Resource<List<Meal?>>>()
    val meals = _meals.asSharedFlow()

    init {
        getCategories()
    }

    private fun getCategories() = viewModelScope.launch {
        repository.getCategories().collect {
            _categories.value = it
        }
    }

    fun getMealsByCategory(categoryID :String)  = viewModelScope.launch {
        repository.getMealsByCategory(categoryID).collect {
           _meals.emit(it)
        }
    }
}