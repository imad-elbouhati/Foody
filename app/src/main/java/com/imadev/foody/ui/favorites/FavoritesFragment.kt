package com.imadev.foody.ui.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.imadev.foody.R
import com.imadev.foody.adapter.FavoriteMealsAdapter
import com.imadev.foody.model.Meal
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.utils.hide
import com.imadev.foody.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "FavoritesFragment"
@AndroidEntryPoint
class FavoritesFragment : Fragment() {


    @Inject
    lateinit var firestore: FirebaseFirestore

    lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbarTitle(R.string.favourites)

        val uid = FirebaseAuth.getInstance().uid.toString()

        recyclerView = view.findViewById(R.id.list)

        firestore.collection("favorites").whereEqualTo("uid",uid).get().addOnSuccessListener {
            val list = mutableListOf<Meal>()
            it.forEach {
                list.add(it.toObject(Meal::class.java))
            }

            recyclerView.adapter = FavoriteMealsAdapter(list)

        }.addOnFailureListener {
            showErrorToast()
        }
    }



}