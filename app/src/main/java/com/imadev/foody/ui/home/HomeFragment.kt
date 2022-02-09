package com.imadev.foody.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imadev.foody.R
import com.imadev.foody.adapter.FoodListHomeAdapter
import com.imadev.foody.databinding.FragmentHomeBinding
import com.imadev.foody.factory.FoodFactory
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as MainActivity).setToolbarTitle(R.string.home)


        val foodAdapter = FoodListHomeAdapter(FoodFactory.foodList())
        //Get 10% of screen size and set padding to recyclerView
        val padding = resources.displayMetrics.widthPixels * 0.1


        with(binding.foodList) {
            setPadding(padding.toInt(), 0, 0, 0)
            adapter = foodAdapter

            foodAdapter.setItemClickListener { food, i ->

                viewModel.navigate(R.id.action_homeFragment_to_foodDetailsFragment)
            }
        }


    }


}