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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.imadev.foody.R
import com.imadev.foody.databinding.ActivityMainBinding
import com.imadev.foody.utils.hide
import com.imadev.foody.utils.show


private const val TAG = "MainActivity"

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





    }


    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun setToolbarTitle(@StringRes title: Int) {
        binding.toolbarTitle.text = getString(title)
    }

    fun setToolbarIcon(@DrawableRes icon: Int, hide: Boolean = false) {

        if (hide) {
            binding.bubbleCart.hide()
            return
        }
        binding.bubbleCart.toolbarIcon.setImageResource(icon)
        binding.bubbleCart.show()


    }


    fun getBubbleCart() = binding.bubbleCart


    fun getToolbarIcon() = binding.bubbleCart.toolbarIcon

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

