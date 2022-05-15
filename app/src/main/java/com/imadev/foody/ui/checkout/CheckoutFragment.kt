package com.imadev.foody.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentCheckoutBinding
import com.imadev.foody.model.Client
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CheckoutFragment"

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>() {

    override val viewModel: CheckoutViewModel by activityViewModels()

    private var client: Client = Client()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        FirebaseAuth.getInstance().uid?.let { viewModel.getClient(it) }


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.client.collect { result ->

                when(result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "onViewCreated: Loading")
                    }

                    is Resource.Success -> {
                        val client = result.data
                        Log.d(TAG, "onViewCreated: Success ${result.data}")
                        with(binding) {
                            clientName.text = client?.username
                            address.text = client?.address
                            phone.text = client?.phone
                            total.text = requireContext().resources.getString(R.string.price, viewModel.getTotal())
                        }
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "onViewCreated: Error")
                    }
                }

            }
        }



        binding.cashOnDeliveryRadio.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.creditCardRadio.isChecked = !b
            }
        }

        binding.creditCardRadio.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.cashOnDeliveryRadio.isChecked = !b
            }
        }
    }

    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.checkout)

    }




}