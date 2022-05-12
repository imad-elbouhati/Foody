package com.imadev.foody.ui.home

import androidx.lifecycle.viewModelScope
import com.imadev.foody.repository.FoodyRepo
import com.imadev.foody.model.Category
import com.imadev.foody.model.Meal
import com.imadev.foody.repository.FoodyRepoImp
import com.imadev.foody.ui.common.BaseViewModel
import com.imadev.foody.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: FoodyRepo
) : BaseViewModel() {

    private val _categories = MutableStateFlow<Resource<List<Category?>>>(Resource.Loading())
    val categories = _categories.asStateFlow()


    private val _meals = MutableStateFlow<Resource<List<Meal?>>>(Resource.Loading())
    val meals = _meals.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories() = viewModelScope.launch {
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