package com.imadev.foody.ui.home

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.imadev.foody.R
import com.imadev.foody.adapter.MealListHomeAdapter
import com.imadev.foody.databinding.FragmentSearchBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.Constants
import com.imadev.foody.utils.Resource
import com.imadev.foody.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, HomeViewModel>(){

    override val viewModel: HomeViewModel by viewModels()

    private var mealAdapter: MealListHomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater,container,false)

    override fun onResume() {
        super.onResume()
        setToolbarTitle(activity as MainActivity)
    }

    override fun setToolbarTitle(activity: MainActivity) {
       activity.setToolbarTitle(R.string.search)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.list.layoutManager = layoutManager
        binding.list.itemAnimator = DefaultItemAnimator()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                
                mealAdapter?.filter?.filter(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                mealAdapter?.filter?.filter(p0)
                return false
            }


        })



        viewModel.getMeals()

        viewModel.meals.collectFlow(viewLifecycleOwner) {
            val data = it.data
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "Loading...")
                    (activity as MainActivity).showProgressBar()
                }
                is Resource.Success -> {
                    (activity as MainActivity).hideProgressBar()
                    data?.let {
                        mealAdapter = MealListHomeAdapter(data as ArrayList<Meal?>)
                        Log.d(TAG, "getMealsByCategory: $data")
                        binding.list.adapter = mealAdapter
                        mealAdapter?.setItemClickListener { meal, _ ->
                            Log.d(TAG, "onViewCreated: $mealAdapter")
                            viewModel.navigate(
                                R.id.action_searchFragment_to_foodDetailsFragment,
                                bundleOf(Constants.MEAL_ARG to meal)
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