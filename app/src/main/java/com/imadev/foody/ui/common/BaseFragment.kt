package com.imadev.foody.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.utils.NavigationCommand
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel> : Fragment() {


    protected abstract val viewModel: VM


    private var _binding: V? = null
    protected val binding get() = _binding!!


    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): V

    abstract fun setToolbarTitle(activity: MainActivity)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = createViewBinding(inflater, container)

        observeNavigation()

        return binding.root
    }



    private fun observeNavigation() {

        viewModel.navigation.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { navigationCommand ->
                handleNavigation(navigationCommand)
            }
        }
    }

    private fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        this.observe(
            owner
        ) {
            it?.let(observer)
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions)
            is NavigationCommand.Back -> findNavController().navigateUp()
            is NavigationCommand.ToDirectionAction -> findNavController().navigate(navCommand.directions,navCommand.bundle)
        }
    }




    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}



