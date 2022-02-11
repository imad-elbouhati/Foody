package com.imadev.foody.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentFoodDetailsBinding
import com.imadev.foody.model.Food
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.ui.home.HomeViewModel
import com.imadev.foody.utils.Constants.Companion.FOOD_ARG

class FoodDetailsFragment : BaseFragment<FragmentFoodDetailsBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private var selected = false

    private var food: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let { args ->
            args?.let { bundle ->
                food = bundle.getParcelable(FOOD_ARG)
            }
        }

    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFoodDetailsBinding = FragmentFoodDetailsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbarTitle(R.string.food_details)

        with(binding) {

            food?.let {
                foodImg.setImageResource(it.image)
                foodTitle.text = it.title
                price.text = requireContext().getString(R.string.price, it.formattedPrice)
            }
        }
    }
}


