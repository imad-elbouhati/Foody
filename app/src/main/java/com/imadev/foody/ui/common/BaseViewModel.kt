package com.imadev.foody.ui.common

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.imadev.foody.utils.Event
import com.imadev.foody.utils.NavigationCommand

abstract class BaseViewModel : ViewModel() {
    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    fun navigate(navDirections: NavDirections) {
        _navigation.value = Event(NavigationCommand.ToDirection(navDirections))
    }

    fun navigateBack() {
        _navigation.value = Event(NavigationCommand.Back)
    }


    fun navigate(navDirections: Int,bundle: Bundle = bundleOf()) {
        _navigation.value = Event(NavigationCommand.ToDirectionAction(navDirections,bundle))
    }

}