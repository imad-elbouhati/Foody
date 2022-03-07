package com.imadev.foody.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide


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


fun ImageView.loadFromUrl(context: Context, url: String?) {
    Glide.with(context).load(url).into(this)
}


