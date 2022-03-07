package com.imadev.foody.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imadev.foody.R
import com.imadev.foody.databinding.ItemRowFoodHomBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.loadFromUrl

private const val TAG = "MealListHomeAdapter"
class MealListHomeAdapter(private val meals: List<Meal?>) :
    RecyclerView.Adapter<MealListHomeAdapter.ViewHolder>() {


    private var onClick: ((Meal, Int) -> Unit)? = null


    class ViewHolder(private val binding: ItemRowFoodHomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context

        fun bind(meal: Meal) {
            with(binding) {
                meal.image?.let { foodImg.loadFromUrl(context, it) }
                foodTitle.text = meal.name
                foodPrice.text = context.resources.getString(R.string.price, meal.price.toString())


            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowFoodHomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position] ?: return
        holder.bind(meal)
        holder.itemView.setOnClickListener {
            onClick?.let { it1 -> it1(meal, position) }
        }
    }

    override fun getItemCount(): Int = meals.size

    fun setItemClickListener(onClick: (Meal, Int) -> Unit) {
        this.onClick = onClick
    }


}