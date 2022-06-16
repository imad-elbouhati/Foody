package com.imadev.foody.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentCheckoutBinding
import com.imadev.foody.fcm.remote.Notification
import com.imadev.foody.fcm.remote.PushNotification
import com.imadev.foody.model.Address
import com.imadev.foody.model.Client
import com.imadev.foody.model.Order
import com.imadev.foody.model.PaymentMethod
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.ui.map.MapsViewModel
import com.imadev.foody.utils.Constants.MOROCCO_PREFIX_PHONE_NUMBER
import com.imadev.foody.utils.Constants.PHONE_NUMBER_LENGTH
import com.imadev.foody.utils.Constants.STRING_LENGTH
import com.imadev.foody.utils.Resource
import com.imadev.foody.utils.collectFlow
import com.imadev.foody.utils.stickPrefix
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "CheckoutFragment"


@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>(),
    View.OnClickListener {

    override val viewModel: CheckoutViewModel by activityViewModels()

    private val mapsViewModel: MapsViewModel by activityViewModels()

    private var mClient = Client()

    private var mPaymentMethod: PaymentMethod? = null

    private var uid: String = ""

    private var newAddress = Address()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        FirebaseAuth.getInstance().uid?.let {
            uid = it
            viewModel.getClient(uid)
        }


    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCheckoutBinding = FragmentCheckoutBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.proceedPaymentBtn.setOnClickListener(this)
        binding.changeAdress.setOnClickListener(this)
        binding.changePhone.setOnClickListener(this)

        binding.phone.stickPrefix(MOROCCO_PREFIX_PHONE_NUMBER)

        viewModel.client.collectFlow(viewLifecycleOwner) { result ->

            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {

                    if (result.data != null)
                        mClient = result.data


                    with(binding) {
                        clientName.text = mClient?.username
                        if (mClient.address?.toString()?.isNotEmpty() == true) {
                            address.text = mClient.address?.address
                            mClient.address?.let {
                                newAddress = it
                            }
                        }

                        phone.setText(mClient?.phone)
                        total.text = requireContext().resources.getString(
                            R.string.price,
                            viewModel.getTotal()
                        )
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, "onViewCreated: ${result.error?.message}")
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }




        binding.cashOnDeliveryRadio.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.creditCardRadio.isChecked = !b
                mPaymentMethod = PaymentMethod.CASH
            }
        }

        binding.creditCardRadio.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.cashOnDeliveryRadio.isChecked = !b
                mPaymentMethod = PaymentMethod.CARD

            }
        }







        mapsViewModel.address.observe(viewLifecycleOwner) {

            binding.address.text = it?.address

            newAddress = it
            Log.d(TAG, "launchMapFragment: $it")

            //Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

        }


    }

    private fun proceedToPayment() {


        if (binding.address.text.isEmpty()) {
            binding.address.error = getString(R.string.please_add_your_address)
        }

        if (binding.phone.text?.isEmpty() == true) {
            binding.phone.error = getString(R.string.please_add_your_phone_number)
            // return
        }


        if (!binding.cashOnDeliveryRadio.isChecked && !binding.creditCardRadio.isChecked) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.please_select_a_payment_method),
                Toast.LENGTH_SHORT
            ).show()
            //return
        }



        if (binding.phone.text.isNotEmpty()) {
            val phone = binding.phone.text
            val isValid =
                phone.trim().length == PHONE_NUMBER_LENGTH && phone.drop(MOROCCO_PREFIX_PHONE_NUMBER.length)
                    .isDigitsOnly()


            if (!isValid) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.please_write_a_valid_number),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }


        //Update address if changed
        if (newAddress.toString() != mClient.address.toString()) {
            newAddress.let { viewModel.updateAddress(uid, it) }
        }


        processTheOrder()

    }


    private fun processTheOrder() {


        viewModel.getAvailableDeliveryUsers()


        viewModel.availableDeliveryUsers.collectFlow(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    (activity as MainActivity).hideProgressBar()

                    Log.d(TAG, "processTheOrder: ${it.error?.message}")
                }
                is Resource.Loading -> {
                    (activity as MainActivity).showProgressBar()
                }
                is Resource.Success -> {

                    val deliveryUser = it.data?.get(0)

                    val order = mPaymentMethod?.let { payment ->
                        Order(
                            orderNumber = getRandomString(),
                            date = Date().time,
                            meals = viewModel.cartList,
                            client = mClient,
                            paymentMethod = payment,
                            to = deliveryUser?.id
                        )
                    }


                    val notification = Notification(
                        getString(R.string.new_odrer),
                        getString(R.string.new_order_messgae, deliveryUser?.username)
                    )
                    order?.let {
                        val pushNotification = PushNotification(notification, deliveryUser?.token)

                        viewModel.sendOrderToDeliveryUser(order, pushNotification)

                        observeNotification(order)
                    }

                }
            }
        }


    }

    private fun observeNotification(order: Order) {
        viewModel.notificationSent.collectFlow(viewLifecycleOwner) { sent ->
            if (sent) {
                (activity as MainActivity).hideProgressBar()
                
                saveOrder(order)
                
                viewModel.navigate(R.id.action_checkoutFragment_to_homeFragment)

                viewModel.resetList()
            }
        }
        
    }

    private fun saveOrder(order: Order) {
        order.uid = uid
        
        FirebaseFirestore.getInstance().collection("order_history").add(order).addOnFailureListener {
            Log.d(TAG, "saveOrder: ${it.message}")
        }

    }


    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.checkout)
    }


    private fun launchMapFragment() {

        viewModel.navigate(R.id.action_checkoutFragment_to_mapsFragment)


    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(requireActivity() as MainActivity)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            binding.proceedPaymentBtn.id -> {
                proceedToPayment()
            }

            binding.changeAdress.id -> {
                launchMapFragment()
            }

            binding.changePhone.id -> {
                Toast.makeText(requireContext(), "TO-IMPLEMENT", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun getRandomString(): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..STRING_LENGTH)
            .map { charset.random() }
            .joinToString("")
    }

}