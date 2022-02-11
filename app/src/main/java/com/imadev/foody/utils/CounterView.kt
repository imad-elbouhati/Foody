package com.imadev.foody.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.imadev.foody.R
import com.imadev.foody.model.Food

class CounterView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var food: Food? = null
    private val plus: ImageView
    private val minus: ImageView
    private val quantityTv: TextView


    private var onCountChangeListener: OnCountChangeListener? = null

    init {
        val view = inflate(context, R.layout.counter_layout, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CounterView)

        plus = view.findViewById(R.id.plus)
        minus = view.findViewById(R.id.minus)
        quantityTv = view.findViewById(R.id.quantity)

        val quantity = typedArray.getInt(0, 0)

        quantityTv.text = quantity.toString()

        typedArray.recycle()

    }

    private fun setClickListeners() {

        food?.let {

            plus.setOnClickListener { v ->
                it.quantity += 1
                quantityTv.text = it.quantity.toString()
                onCountChangeListener?.onCountChange(it.quantity)
            }

            minus.setOnClickListener { v ->
                if (it.quantity > 0) {
                    it.quantity -= 1
                    setQuantity(it.quantity)
                    onCountChangeListener?.onCountChange(it.quantity)
                }
            }
        }

    }


    fun addOnCountChangeListener(listener: OnCountChangeListener) {
        this.onCountChangeListener = listener
    }

    interface OnCountChangeListener {
        fun onCountChange(count: Int)
    }


    fun setQuantity(value: Int) {
        food?.quantity = value
        quantityTv.text = value.toString()
    }



    fun setFoodModel(model: Food) {
        this.food = model
        setClickListeners()
    }


}