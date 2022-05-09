package com.imadev.foody.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imadev.foody.R
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.utils.moveTo

class SignInActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


    }


    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser

        user?.let {
            moveTo(MainActivity::class.java)
        }

    }
}