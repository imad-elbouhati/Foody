package com.imadev.foody.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.imadev.foody.R
import com.imadev.foody.databinding.ItemRowFavoriteLayoutBinding
import com.imadev.foody.model.Food
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.CounterView
import com.imadev.foody.utils.loadFromUrl


private const val TAG = "CartAdapter"

class FavoriteMealsAdapter(private var meals: MutableList<Meal> = mutableListOf()) : RecyclerView.Adapter<FavoriteMealsAdapter.ViewHolder>(){




    class ViewHolder(private val binding: ItemRowFavoriteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        fun bind(meal: Meal) {

            with(binding) {
                foodImg.loadFromUrl(context,meal.image)
                foodTitle.text = meal.name
                foodPrice.text = context.resources.getString(R.string.price, meal.price.toString())
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowFavoriteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)

    }

    override fun getItemCount(): Int = meals.size








}