package com.imadev.foody.utils

import android.os.Bundle
import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class ToDirection(val directions: NavDirections) : NavigationCommand()
    data class ToDirectionAction(val directions: Int,val bundle: Bundle) : NavigationCommand()
    object Back : NavigationCommand()
}