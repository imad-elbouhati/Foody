package com.imadev.foody.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.imadev.foody.utils.NavigationCommand
import kotlinx.coroutines.flow.collect

abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel>() : Fragment() {


    protected abstract val viewModel: VM


    private var _binding: V? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = createViewBinding(inflater, container)

        observeNavigation()

        return binding.root
    }

    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): V


    private fun observeNavigation() {

        lifecycleScope.launchWhenCreated {
            viewModel.navigation.collect { navigationCommand ->
                handleNavigation(navigationCommand)
            }
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}



