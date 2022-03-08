package com.imadev.foody.ui.home

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentSearchBinding
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment

class SearchFragment :BaseFragment<FragmentSearchBinding,HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()


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

    }

}