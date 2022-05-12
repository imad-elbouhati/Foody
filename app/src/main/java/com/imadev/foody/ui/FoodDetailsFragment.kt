package com.imadev.foody.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentFoodDetailsBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.checkout.CheckoutViewModel
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.ui.home.HomeViewModel
import com.imadev.foody.utils.Constants.Companion.MEAL_ARG
import com.imadev.foody.utils.loadFromUrl
import com.imadev.foody.utils.setIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailsFragment : BaseFragment<FragmentFoodDetailsBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    val cartViewModel: CheckoutViewModel by activityViewModels()

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


    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            meal?.let {
                foodImg.loadFromUrl(requireContext(), meal?.image)
                foodTitle.text = it.name
                price.text = requireContext().getString(R.string.price, it.price.toString())
                description.text = it.ingredient.joinToString(separator = "\n")
            }
        }

        binding.addToCartButton.setOnClickListener { _ ->

            if(cartViewModel.cartList.contains(meal)) {
                Snackbar.make(binding.root,"Item already exist in cart ",Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Snackbar.make(binding.root,"Item added to cart ",Snackbar.LENGTH_SHORT).setIcon(
                getDrawable(requireContext(),R.drawable.ic_success_24)!!,
                ContextCompat.getColor(requireContext(), R.color.foody_green)
            ).show()

            meal?.let { it -> cartViewModel.addToCart(it) }
        }
    }


    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.food_details)
    }
}


