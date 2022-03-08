package com.imadev.foody.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.imadev.foody.ui.MainActivity


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

fun Snackbar.setIcon(drawable: Drawable, @ColorInt colorTint: Int): Snackbar {
    return this.apply {
        setAction(" ") {}
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        textView.text = ""

        drawable.setTint(colorTint)
        drawable.setTintMode(PorterDuff.Mode.SRC_ATOP)
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}



