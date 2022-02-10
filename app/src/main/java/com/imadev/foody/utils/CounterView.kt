package com.imadev.foody.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.imadev.foody.R

class CounterView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val plus: ImageView
    private val minus: ImageView
    private val quantityTv: TextView

    private var count = 0

    private var onCountChangeListener: OnCountChangeListener? = null

    init {
        val view = inflate(context, R.layout.counter_layout, this)

        plus = view.findViewById(R.id.plus)
        minus = view.findViewById(R.id.minus)
        quantityTv = view.findViewById(R.id.quantity)

        setClickListeners()
    }

    private fun setClickListeners() {
        plus.setOnClickListener {
            count += 1
            quantityTv.text = count.toString()
            onCountChangeListener?.onCountChange(count)
        }

        minus.setOnClickListener {
            if (count > 0) {
                count -= 1
                quantityTv.text = count.toString()
                onCountChangeListener?.onCountChange(count)
            }
        }
    }


    fun addOnCountChangeListener(listener: OnCountChangeListener) {
        this.onCountChangeListener = listener
    }

    interface OnCountChangeListener {
        fun onCountChange(count: Int)
    }

}