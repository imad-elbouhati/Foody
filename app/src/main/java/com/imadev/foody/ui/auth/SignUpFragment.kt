package com.imadev.foody.ui.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imadev.foody.databinding.FragmentSignUpBinding
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
    class SignUpFragment : BaseFragment<FragmentSignUpBinding, AuthViewModel>() {
        override val viewModel: AuthViewModel by viewModels()

        override fun createViewBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
        ): FragmentSignUpBinding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        override fun setToolbarTitle(activity: MainActivity) {
            TODO("Not yet implemented")
        }

}