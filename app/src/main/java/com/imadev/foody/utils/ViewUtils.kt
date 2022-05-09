package com.imadev.foody.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.imadev.foody.R


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
    Glide.with(context).load(url).error(R.drawable.error_loading).into(this)
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


fun ViewBinding.show() {
    this.root.show()
}

fun ViewBinding.hide() {
    this.root.hide()
}


fun Fragment.moveTo(activity: Class<out Activity>) {
    val intent = Intent(requireActivity(), activity)
    finishAffinity(requireActivity())
    this.startActivity(intent)
}


fun Activity.moveTo(activity: Class<out Activity>) {
    val intent = Intent(this, activity)
    finishAffinity(this)
    this.startActivity(intent)
}


fun ViewBinding.applyFullscreen() {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= 30) {
        this.root.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    } else {
        this.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

}
