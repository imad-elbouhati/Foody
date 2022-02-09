package com.imadev.foody.utils

import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class ToDirection(val directions: NavDirections) : NavigationCommand()
    data class ToDirectionAction(val directions: Int) : NavigationCommand()
    object Back : NavigationCommand()
}