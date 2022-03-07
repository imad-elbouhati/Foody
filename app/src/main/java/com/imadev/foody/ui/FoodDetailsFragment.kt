package com.imadev.foody.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentFoodDetailsBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.ui.home.HomeViewModel
import com.imadev.foody.utils.Constants.Companion.MEAL_ARG
import com.imadev.foody.utils.loadFromUrl

class FoodDetailsFragment : BaseFragment<FragmentFoodDetailsBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private var selected = false


    private var meal: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let { args ->
            args?.let { bundle ->
                meal = bundle.getParcelable(MEAL_ARG)
            }
        }

    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFoodDetailsBinding = FragmentFoodDetailsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            meal?.let {
                foodImg.loadFromUrl(requireContext(),meal?.image)
                foodTitle.text = it.name
                price.text = requireContext().getString(R.string.price, it.price.toString())
                description.text = it.ingredient.joinToString(separator = "\n")
            }
        }
    }



    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.food_details)
    }
}


