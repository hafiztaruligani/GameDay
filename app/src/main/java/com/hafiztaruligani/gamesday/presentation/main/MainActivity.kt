package com.hafiztaruligani.gamesday.presentation.main

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hafiztaruligani.gamesday.R
import com.hafiztaruligani.gamesday.databinding.ActivityMainBinding
import com.hafiztaruligani.gamesday.presentation.LoadingBar
import com.hafiztaruligani.gamesday.presentation.detail.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var splashScreen: SplashScreen
    private lateinit var navController: NavController

    private lateinit var currentDestination : NavDestination

    private lateinit var loadingBar: LoadingBar
    private var favoriteButton : MenuItem?=null
    private var favOn : Drawable? = null
    private var favOff : Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initiate splash screen
        // splash screen must be visible until home fragment data is ready
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host)

        setupLoadingBar()
        setupBottomNav()
        setupNavigation()

    }

    // setup actionbar and bottom nav, depending on currently used/visible fragment...
    // ... bottom nav only invisible when fragment detail used/visible
    // ... custom actionbar (with favorite button and back button) only visible when fragment detail used/visible
    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            currentDestination = destination
            when (destination.id){

                R.id.homeFragment -> {
                    binding.bottomNavigationView.isVisible = true

                    favoriteButton?.isVisible = false
                    supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
                    supportActionBar?.title = getString(R.string.games_for_you)
                }
                R.id.favouriteFragment -> {
                    binding.bottomNavigationView.isVisible = true

                    favoriteButton?.isVisible = false
                    supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
                    supportActionBar?.title = getString(R.string.favorite_games)
                }
                R.id.detailFragment -> {
                    binding.bottomNavigationView.isVisible = false

                    // setup title
                    supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                    supportActionBar?.setCustomView(R.layout.custom_toolbar)

                    // setup back button
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

                    //setup favorite button
                    favoriteButton?.isVisible = true

                }

            }
        }

    }

    // setup bottom nav background, item color, etc.
    private fun setupBottomNav() {

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )

        val colors = intArrayOf(
            ResourcesCompat.getColor(resources, R.color.bottom_nav_off, null),
            ResourcesCompat.getColor(resources, R.color.bottom_nav_on, null)
        )
        val colorState = ColorStateList(states, colors)

        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            background = ResourcesCompat.getDrawable(resources, R.color.bottom_navigation_background, null)
            itemIconTintList = colorState
            itemTextColor = colorState

        }

    }

    // setup favorite button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        favOff = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24_off, null)
        favOn = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24_on, null)

        menuInflater.inflate(R.menu.action_bar_menu, menu)
        favoriteButton = menu?.findItem(R.id.favorite_button)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.primaryNavigationFragment

        when (item.itemId){

            // when favorite button selected, calling detail fragment function to response this action
            // (with current fragment validation first)
            R.id.favorite_button -> if (currentDestination.id == R.id.detailFragment){
                if (fragment is DetailFragment) {
                    fragment.onClickFavorite()
                }
            }

            else -> {
                navController.popBackStack()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    // this function used by detail fragment to set favorite button (on/of)
    fun setFavorite(value: Boolean){
        favoriteButton?.icon = if(value) favOn else favOff
    }

    // this function used by home fragment to notify main activity that data is ready
    // and dismiss the splash screen
    fun dismissSplashScreen(){
        splashScreen.setKeepOnScreenCondition{false}
    }

    private fun setupLoadingBar() {

        loadingBar = LoadingBar(this, navController::popBackStack)

        // on back press -> pop back stack
        loadingBar.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                navController.popBackStack()
                loadingBar.state(false)
            }
            true
        }

    }

    // set Loading
    fun loading(value: Boolean){
        loadingBar.state(value)
    }

}