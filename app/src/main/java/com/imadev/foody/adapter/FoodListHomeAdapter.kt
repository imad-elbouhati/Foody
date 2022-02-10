package com.imadev.foody.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imadev.foody.R
import com.imadev.foody.databinding.ItemRowFoodHomBinding
import com.imadev.foody.model.Food

class FoodListHomeAdapter(private val foods: List<Food>) :
    RecyclerView.Adapter<FoodListHomeAdapter.ViewHolder>() {


    private var onClick: ((Food, Int) -> Unit)? = null


    class ViewHolder(private val binding: ItemRowFoodHomBinding) :
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
            ItemRowFoodHomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)
        holder.itemView.setOnClickListener {
            onClick?.let { it1 -> it1(food, position) }
        }
    }

    override fun getItemCount(): Int = foods.size

    fun setItemClickListener(onClick: (Food, Int) -> Unit) {
        this.onClick = onClick
    }


}