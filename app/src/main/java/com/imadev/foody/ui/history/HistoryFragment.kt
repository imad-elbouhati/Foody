package com.imadev.foody.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imadev.foody.R
import com.imadev.foody.adapter.FavoriteMealsAdapter
import com.imadev.foody.adapter.HistoryMealsAdapter
import com.imadev.foody.databinding.FragmentHistoryBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.model.Order
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val TAG = "HistoryFragment"

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override val viewModel: HistoryViewModel by viewModels()

    @Inject
    lateinit var firestore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = FirebaseAuth.getInstance().uid.toString()



        firestore.collection("order_history").whereEqualTo("uid", uid).get().addOnSuccessListener {
            val list = mutableListOf<Meal>()

            it.forEach { doc ->
                val order = doc.toObject(Order::class.java)
                order.meals.forEach { meal ->
                    list.add(meal.apply {
                        date = order.date
                    })
                }
            }
            Log.d(TAG, "onViewCreated: ${list}")

            binding.list.adapter = HistoryMealsAdapter(list)

        }.addOnFailureListener {
            Log.d(TAG, "onViewCreated: ${it?.message}")
        }

    }


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false)


    override fun onResume() {
        super.onResume()
        setToolbarTitle(requireActivity() as MainActivity)
    }

    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.history)
    }


}