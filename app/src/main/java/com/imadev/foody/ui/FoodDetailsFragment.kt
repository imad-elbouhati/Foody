package com.imadev.foody.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentFoodDetailsBinding
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.checkout.CheckoutViewModel
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.ui.home.HomeViewModel
import com.imadev.foody.utils.Constants.MEAL_ARG
import com.imadev.foody.utils.loadFromUrl
import com.imadev.foody.utils.setIcon
import com.imadev.foody.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "FoodDetailsFragment"
@AndroidEntryPoint
class FoodDetailsFragment : BaseFragment<FragmentFoodDetailsBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private val cartViewModel: CheckoutViewModel by activityViewModels()

    private var mSelected = false

    private var mMealID: String = ""

    private var meal: Meal? = null


    @Inject
    lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let { args ->
            args?.let { bundle ->
                meal = bundle.getParcelable(MEAL_ARG)
                mMealID = meal?.id.toString()
            }
        }

    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFoodDetailsBinding = FragmentFoodDetailsBinding.inflate(inflater, container, false)




    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        with(binding) {

            meal?.let {
                foodImg.loadFromUrl(requireContext(), meal?.image)
                foodTitle.text = it.name
                price.text = requireContext().getString(R.string.price, it.price.toString())
                description.text = it.ingredient.joinToString(separator = "\n")
            }
        }

        binding.addToCartButton.setOnClickListener { _ ->

            if (cartViewModel.cartList.contains(meal)) {
                Snackbar.make(binding.root, "Item already exist in cart ", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            Snackbar.make(binding.root, "Item added to cart ", Snackbar.LENGTH_SHORT).setIcon(
                getDrawable(requireContext(), R.drawable.ic_success_24)!!,
                ContextCompat.getColor(requireContext(), R.color.foody_green)
            ).show()

            meal?.let { it -> cartViewModel.addToCart(it) }

        }



        (requireActivity() as MainActivity).apply {
            getFavoriteToolbarIcon().setOnClickListener {
                mSelected = !mSelected

                val drawable = if (mSelected) {
                    addToFavorites()
                    R.drawable.ic_heart_selected
                } else {
                    removeFromFavorites()
                    R.drawable.ic_heart
                }

                (activity as MainActivity).getFavoriteToolbarIcon().setImageResource(drawable)
            }
        }
    }

    private fun removeFromFavorites() {
        meal?.let {
            firestore.collection("favorites").document(mMealID).delete().addOnFailureListener {
                showErrorToast()
            }
        }
    }

    private fun addToFavorites() {

        meal?.let { m ->
            m.favorite = true
            m.uid = FirebaseAuth.getInstance().uid.toString()
            firestore.collection("favorites").document(mMealID).set(m).addOnFailureListener {
                showErrorToast()
            }
        }
    }


    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.food_details)
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(requireActivity() as MainActivity)

        firestore.collection("favorites").whereEqualTo("uid", FirebaseAuth.getInstance().uid.toString()).get().addOnSuccessListener {
            it.forEach { meal ->
                val id = meal.toObject(Meal::class.java).id.toString()
                mSelected = id == mMealID

                val drawable = if (mSelected) {
                    R.drawable.ic_heart_selected
                } else {

                    R.drawable.ic_heart
                }

                (activity as MainActivity).getFavoriteToolbarIcon().setImageResource(drawable)
            }
        }

    }

}


