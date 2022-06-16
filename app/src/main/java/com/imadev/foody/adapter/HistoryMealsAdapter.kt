package com.imadev.foody.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imadev.foody.databinding.ItemRowHistoryLayoutBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.utils.loadFromUrl
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "CartAdapter"

class HistoryMealsAdapter(private var meals: MutableList<Meal> = mutableListOf()) :
    RecyclerView.Adapter<HistoryMealsAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ItemRowHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        @SuppressLint("SetTextI18n")
        fun bind(meal: Meal) {

            with(binding) {
                foodImg.loadFromUrl(context, meal.image)
                foodTitle.text = meal.name
                date.text = getFormatTimeWithTZ(Date(meal.date))
                foodQuantity.text = "×" + meal.quantity.toString()
            }

        }

        private fun getFormatTimeWithTZ(currentTime: Date): String {
            val timeZoneDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return timeZoneDate.format(currentTime)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)

    }

    override fun getItemCount(): Int = meals.size


}