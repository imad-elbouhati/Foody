package com.imadev.foody.ui.checkout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
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
import com.imadev.foody.factory.FoodFactory
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment


private const val TAG = "CartFragment"

class CartFragment : BaseFragment<FragmentCartBinding, CheckoutViewModel>() {

    override val viewModel: CheckoutViewModel by activityViewModels()

    private var adapter = CartAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.cartList = FoodFactory.foodList()
        Toast.makeText(requireContext(), "onCreate Triggered", Toast.LENGTH_SHORT).show()

    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbarTitle(R.string.cart)

        setSwipeAnimationIcon()


        adapter = CartAdapter(viewModel.cartList)

        setRecyclerView(adapter)

        adapter.addOnCountChanged { count ->

        }


        binding.completeOrderBtn.setOnClickListener {

            viewModel.navigate(R.id.action_cartFragment_to_checkoutFragment)
        }

    }

    private fun setRecyclerView(adapterR: CartAdapter) = binding.cartList.apply {
        adapter = adapterR
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
    }


    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val food = viewModel.cartList[position]
            viewModel.cartList.remove(food)
            adapter.notifyItemRemoved(position)

            Snackbar.make(requireView(), "Order successfully deleted", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.cartList.add(position,food)
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