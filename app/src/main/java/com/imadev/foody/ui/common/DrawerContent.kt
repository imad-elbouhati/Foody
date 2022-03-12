package com.imadev.foody.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.drawerlayout.widget.DrawerLayout

class DrawerContent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MotionLayout(context, attrs),DrawerLayout.DrawerListener {
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        progress = slideOffset
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerStateChanged(newState: Int) {
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as DrawerLayout).addDrawerListener(this)
    }

}