package com.imadev.foody.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentCheckoutBinding
import com.imadev.foody.model.Client
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.formatDecimal


class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>() {

    override val viewModel: CheckoutViewModel by activityViewModels()

    private lateinit var client: Client

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbarTitle(R.string.checkout)

        client = Client()

        with(binding) {
            clientName.text = client.username
            address.text = client.address
            phone.text = client.phone


            total.text = requireContext().resources.getString(R.string.price, getTotal())

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


    private fun getTotal(): String {
        return formatDecimal(viewModel.cartList.sumOf { it.quantity * it.price })
    }

}