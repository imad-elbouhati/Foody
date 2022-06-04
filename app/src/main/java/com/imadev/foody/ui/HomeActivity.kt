package com.imadev.foody.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
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
class MainActivity : AppCompatActivity() {

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
                R.id.userFragment,
                R.id.historyFragment,
                R.id.favoritesFragment
            )
        )


        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)


        // val viewModel = ViewModelProvider(this)[GenerateFoodViewModel::class.java]

        navController.addOnDestinationChangedListener { controller, _, _ ->
            val currentDestinationId = controller.currentDestination?.id
            if (currentDestinationId == R.id.homeFragment ||
                currentDestinationId == R.id.favoritesFragment ||
                currentDestinationId == R.id.userFragment ||
                currentDestinationId == R.id.historyFragment
            ) {
                binding.menuIc.show()
            } else {
                binding.menuIc.hide()
            }

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



        binding.signOut.setOnClickListener {
            Toast.makeText(this@MainActivity, "To-Implement signout", Toast.LENGTH_SHORT).show()
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





        binding.signOut.setOnClickListener {
            Firebase.auth.signOut()
            getSharedPreferences(Constants.FCM_TOKEN_PREF, MODE_PRIVATE).edit().clear().apply()
            moveTo(LoginActivity::class.java)
        }


        binding.bottomNav.setOnItemSelectedListener { item ->
            // In order to get the expected behavior, you have to call default Navigation method manually
            NavigationUI.onNavDestinationSelected(item, navController)

            return@setOnItemSelectedListener true
        }

    }


    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun setToolbarTitle(@StringRes title: Int) {
        binding.toolbarTitle.text = getString(title)
    }

    fun setHomeToolbarIcon() {
        binding.favoriteIcon.hide()
        binding.bubbleCart.show()
    }


    fun setFavoriteToolbarIcon() {
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

        if(isDrawerActive) {
            binding.motionLayout.transitionToStart()
            return
        }
        super.onBackPressed()
    }

}

