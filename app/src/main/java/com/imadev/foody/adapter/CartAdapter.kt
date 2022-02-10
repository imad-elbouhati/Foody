package com.imadev.foody.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imadev.foody.R
import com.imadev.foody.databinding.ItemRowOrderLayoutBinding
import com.imadev.foody.model.Food
import com.imadev.foody.utils.CounterView

class CartAdapter(val foods: MutableList<Food> = mutableListOf()) : RecyclerView.Adapter<CartAdapter.ViewHolder>(),
    CounterView.OnCountChangeListener {


    private var listener: ((Int) -> Unit)? = null


    class ViewHolder(private val binding: ItemRowOrderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        fun bind(food: Food) {
            with(binding) {
                foodImg.setImageResource(food.image)
                foodTitle.text = food.title
                foodPrice.text = context.resources.getString(R.string.price, food.formattedPrice)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowOrderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.counter.addOnCountChangeListener(this)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)

    }

    override fun getItemCount(): Int = foods.size

    fun addOnCountChanged(listener: (Int) -> Unit) {
        this.listener = listener
    }


    override fun onCountChange(count: Int) {
        listener?.invoke(count)
    }


}