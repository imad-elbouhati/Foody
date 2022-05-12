package com.imadev.foody.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentLoginBinding
import com.imadev.foody.databinding.FragmentOTPBinding
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPFragment :  BaseFragment<FragmentOTPBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModels()


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOTPBinding = FragmentOTPBinding.inflate(layoutInflater, container, false)

    override fun setToolbarTitle(activity: MainActivity) {
        TODO("Not yet implemented")
    }


}
