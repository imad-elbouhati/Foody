package com.imadev.foody.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.imadev.foody.R
import com.imadev.foody.adapter.MealListHomeAdapter
import com.imadev.foody.databinding.FragmentHomeBinding
import com.imadev.foody.model.Category
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.checkout.CheckoutViewModel
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.Constants.Companion.MEAL_ARG
import com.imadev.foody.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    val cartViewModel: CheckoutViewModel by activityViewModels()

    private var mealsJob: Job? = null

    private var mealAdapter: MealListHomeAdapter? = null

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)


    override fun onResume() {
        setToolbarTitle(activity as MainActivity)
        updateCartCounter()

        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Get 10% of screen size and set padding to recyclerView
        val padding = resources.displayMetrics.widthPixels * 0.1


        with(binding.foodList) {
            setPadding(padding.toInt(), 0, 0, 0)

        }



        observeSubscribers()
        clickListeners()

        binding.searchView.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.searchView to "search_view_trans"
            )

            findNavController().navigate(
                R.id.action_homeFragment_to_searchFragment,
                null,
                null,
                extras
            )
        }



    }

    private fun updateCartCounter() {
        (activity as MainActivity).getBubbleCart().cartCount.text =
            (cartViewModel.cartList.size).toString()
    }

    private fun clickListeners() {
        binding.categoryTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${(tab?.tag as Category)}")
                val categoryID = (tab.tag as Category).id
                getMealsByCategory(categoryID)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


    }

    private fun getMealsByCategory(categoryID: String) {
        mealsJob?.cancel()
        mealsJob = lifecycleScope.launch {
            viewModel.getMealsByCategory(categoryID)

            viewModel.meals.collectLatest {
                val data = it.data
                when (it) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Loading...")
                        (activity as MainActivity).showProgressBar()
                    }
                    is Resource.Success -> {
                        (activity as MainActivity).hideProgressBar()
                        data?.let {
                            mealAdapter = MealListHomeAdapter(data)
                            Log.d(TAG, "getMealsByCategory: $data")
                            binding.foodList.adapter = mealAdapter
                            mealAdapter?.setItemClickListener { meal, _ ->
                                Log.d(TAG, "onViewCreated: $mealAdapter")
                                viewModel.navigate(
                                    R.id.action_homeFragment_to_foodDetailsFragment,
                                    bundleOf(MEAL_ARG to meal)
                                )
                            }
                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        Log.d(TAG, "Meals error: ${it.error?.message}")
                    }
                }
            }
        }

    }

    private fun observeSubscribers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.categories.collect {
                    val data = it.data
                    when (it) {
                        is Resource.Loading -> {
                            (activity as MainActivity).showProgressBar()
                        }
                        is Resource.Success -> {
                            (activity as MainActivity).hideProgressBar()

                            data?.reversed()?.forEach { category ->
                                val newTab = binding.categoryTabs.newTab().apply {
                                    tag = category
                                    text = category?.name
                                }
                                binding.categoryTabs.addTab(
                                    newTab
                                )
                            }

                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                        }
                    }
                }
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