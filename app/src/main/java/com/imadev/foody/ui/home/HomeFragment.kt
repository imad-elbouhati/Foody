package com.imadev.foody.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.imadev.foody.R
import com.imadev.foody.adapter.FoodListHomeAdapter
import com.imadev.foody.databinding.FragmentHomeBinding
import com.imadev.foody.factory.FoodFactory
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.Constants.Companion.FOOD_ARG


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)


    override fun onResume() {
        setToolbarTitle(activity as MainActivity)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val foodAdapter = FoodListHomeAdapter(FoodFactory.foodList())
        //Get 10% of screen size and set padding to recyclerView
        val padding = resources.displayMetrics.widthPixels * 0.1


        with(binding.foodList) {
            setPadding(padding.toInt(), 0, 0, 0)
            adapter = foodAdapter

            foodAdapter.setItemClickListener { food, _ ->

                viewModel.navigate(
                    R.id.action_homeFragment_to_foodDetailsFragment,
                    bundleOf(FOOD_ARG to food)
                )
            }
        }


    }

    override fun setToolbarTitle(activity: MainActivity) {
        activity.apply {
            setToolbarTitle(R.string.home)
            setToolbarIcon(R.drawable.ic_cart, false)
            getToolbarIcon().setOnClickListener {
                viewModel.navigate(R.id.action_homeFragment_to_cartFragment)
            }
        }
    }


    override fun onPause() {
        (activity as MainActivity).setToolbarIcon(R.drawable.ic_cart, true)
        super.onPause()
    }


}