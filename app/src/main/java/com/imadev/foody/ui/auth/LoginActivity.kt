package com.imadev.foody.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imadev.foody.R
import com.imadev.foody.databinding.ActivitySignInBinding
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.utils.applyFullscreen
import com.imadev.foody.utils.moveTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.applyFullscreen()



    }


    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser

        user?.let {
            Toast.makeText(this, user.displayName, Toast.LENGTH_SHORT).show()
            moveTo(MainActivity::class.java)
        }

    }
}