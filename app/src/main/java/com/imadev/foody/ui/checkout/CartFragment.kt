package com.imadev.foody.ui.checkout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.imadev.foody.R
import com.imadev.foody.adapter.CartAdapter
import com.imadev.foody.databinding.FragmentCartBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.hide
import com.imadev.foody.utils.show


private const val TAG = "CartFragment"

class CartFragment : BaseFragment<FragmentCartBinding, CheckoutViewModel>() {

    override val viewModel: CheckoutViewModel by activityViewModels()

    private var adapter = CartAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  viewModel.cartList = FoodFactory.foodList()
        viewModel.observeQuantity()

    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setSwipeAnimationIcon()


        adapter = CartAdapter(viewModel.cartList as MutableList<Meal>)

        adapter.addOnCountChanged {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.cartIsEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                binding.emptyCart.show()
                binding.constraint.hide()
            } else {
                binding.emptyCart.hide()
                binding.constraint.show()
            }
        }



        setRecyclerView(adapter)


        viewModel.canProceedToPayment.observe(viewLifecycleOwner) {
            binding.completeOrderBtn.isEnabled = it
        }

        adapter.addOnCountChanged {
            viewModel.observeQuantity()
        }


        binding.completeOrderBtn.setOnClickListener {

            viewModel.navigate(R.id.action_cartFragment_to_checkoutFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(activity as MainActivity)
    }
    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.cart)
    }

    private fun setRecyclerView(adapterR: CartAdapter) = binding.cartList.apply {
        adapter = adapterR
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
    }


    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.layoutPosition
            val meal = viewModel.cartList[position]
            viewModel.removeFromCart(meal)
            adapter.notifyItemRemoved(position)

            Snackbar.make(
                requireView(),
                getString(R.string.order_delete_successfully),
                Snackbar.LENGTH_LONG
            ).apply {

                setAction(getString(R.string.undo)) {
                    viewModel.addToCart(meal, position)
                    adapter.notifyItemInserted(position)
                }

                show()
            }
        }
    }


    private fun setSwipeAnimationIcon() {

        val rotate = ObjectAnimator.ofFloat(binding.swipeIcon, "rotation", 10f, -40f)

        with(rotate) {
            repeatCount = 5
            repeatMode = ValueAnimator.REVERSE
            duration = 500
            start()
        }
    }


}