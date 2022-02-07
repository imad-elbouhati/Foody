package com.imadev.foody.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.imadev.foody.R

class FoodDetailsFragment : Fragment() {

    private var selected = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.heart_iv).setOnClickListener {
            selected = !selected
            if(selected) view.findViewById<ImageView>(R.id.heart_iv).setImageResource(R.drawable.ic_heart_selected)
            else view.findViewById<ImageView>(R.id.heart_iv).setImageResource(R.drawable.ic_heart)

        }
    }

}