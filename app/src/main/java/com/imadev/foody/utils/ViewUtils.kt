package com.imadev.foody.utils

import android.content.res.Resources
import android.util.TypedValue
import android.view.View


fun View.hide(invisible: Boolean = false) {
    when (invisible) {
        false -> this.visibility = View.GONE
        true -> this.visibility = View.INVISIBLE
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}


