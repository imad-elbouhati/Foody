package com.imadev.foody.ui

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.userFragment,
                R.id.historyFragment,
                R.id.favoritesFragment
            ),
            binding.drawerLayout
        )


        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)




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

}

