package com.imadev.foody.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.imadev.foody.utils.NavigationCommand
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel : ViewModel() {

    private val _navigation = MutableSharedFlow<NavigationCommand>()
    val navigation: SharedFlow<NavigationCommand> get() = _navigation.asSharedFlow()

    suspend fun navigate(navDirections: NavDirections) {
        _navigation.emit(NavigationCommand.ToDirection(navDirections))
    }

    suspend fun navigateBack() {
        _navigation.emit(NavigationCommand.Back)
    }


}