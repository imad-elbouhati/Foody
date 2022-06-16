package com.imadev.foody.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imadev.foody.R
import com.imadev.foody.databinding.ActivityMainBinding
import com.imadev.foody.ui.auth.LoginActivity
import com.imadev.foody.utils.*
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mMotionProgress: Float = 0f
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var isDrawerActive = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.applyFullscreen()


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.historyFragment,
                R.id.favoritesFragment
            )
        )


        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)







        navController.addOnDestinationChangedListener { controller, _, _ ->
            val currentDestinationId = controller.currentDestination?.id

            configToolbar(currentDestinationId)


            //TODO to remove later
            if (currentDestinationId == R.id.homeFragment ||
                currentDestinationId == R.id.favoritesFragment ||
                currentDestinationId == R.id.historyFragment
            ) {
                binding.menuIc.show()
            } else {
                binding.menuIc.hide()
            }


        }





        binding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                mMotionProgress = progress
                isDrawerActive = progress > 0

                Log.d(TAG, "onTransitionChange: $progress")
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                //In case of onSwipe the progress doesn't reach 1 when animation completed so I have to force it when onTransitionCompleted

                if(mMotionProgress > 0) isDrawerActive = true
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }
        })





        subscribeClickListeners()


        binding.bottomNav.setOnItemSelectedListener { item ->
            // In order to get the expected behavior, you have to call default Navigation method manually
            NavigationUI.onNavDestinationSelected(item, navController)

            return@setOnItemSelectedListener true
        }

    }

    private fun subscribeClickListeners() {
        binding.signOut.setOnClickListener(this)
        binding.favorite.setOnClickListener(this)
        binding.home.setOnClickListener(this)
        binding.history.setOnClickListener(this)
    }

    private fun configToolbar(currentDestinationId: Int?) {

        when (currentDestinationId) {
            R.id.homeFragment -> {
                setToolbarTitle(R.string.home)
                setHomeToolbarIcon()
            }

            R.id.foodDetailsFragment -> {
                setFavoriteToolbarIcon()
            }
            else -> {
                binding.favoriteIcon.hide()
                binding.bubbleCart.hide()
            }

        }

    }


    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun setToolbarTitle(@StringRes title: Int) {
        binding.toolbarTitle.text = getString(title)
    }

    private fun setHomeToolbarIcon() {
        binding.favoriteIcon.hide()
        binding.bubbleCart.show()
    }


    private fun setFavoriteToolbarIcon() {
        binding.bubbleCart.hide()
        binding.favoriteIcon.show()
    }


    fun getBubbleCart() = binding.bubbleCart


    fun getHomeToolbarIcon() = binding.bubbleCart.toolbarIcon

    fun getFavoriteToolbarIcon() = binding.favoriteIcon

    fun getToolbar() = binding.toolbar

    fun getBottomNav() = binding.bottomNav

    fun showProgressBar() {
        binding.progressBar.show()
    }

    fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: $isDrawerActive")

        if (isDrawerActive) {
            binding.motionLayout.transitionToStart()
            return
        }
        super.onBackPressed()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.sign_out -> {
                signOut()
            }

            R.id.favorite -> {
                navController.navigate(R.id.action_homeFragment_to_favoritesFragment)
                binding.motionLayout.transitionToStart()
            }
            R.id.history -> {
                navController.navigate(R.id.action_homeFragment_to_historyFragment)
                binding.motionLayout.transitionToStart()
            }
            R.id.home -> {
                navController.navigate(R.id.action_homeFragment_self)
                binding.motionLayout.transitionToStart()
            }
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        getSharedPreferences(Constants.FCM_TOKEN_PREF, MODE_PRIVATE).edit().clear().apply()
        moveTo(LoginActivity::class.java)
    }

}



