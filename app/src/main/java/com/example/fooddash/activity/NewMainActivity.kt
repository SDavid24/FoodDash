package com.example.fooddash.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddash.R
import com.example.fooddash.databinding.ActivityNewMainBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.fragments.intro.LoginFragment
import com.example.fooddash.fragments.intro.SplashFragment
import com.example.fooddash.fragments.operations.CartListFragment
import com.example.fooddash.fragments.operations.HomeFragment

class NewMainActivity : AppCompatActivity() {
    private var homeAlreadyClicked = false
    private var cartAlreadyClicked = false
    private var profileAlreadyClicked = false
    private var supportAlreadyClicked = false
    private var settingsAlreadyClicked = false
    private lateinit var binding : ActivityNewMainBinding

    private val homeFragment = HomeFragment()

    private val loginFragment = LoginFragment()
    private val splashFragment = SplashFragment()
    private val cartListFragment = CartListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationViewFragment.background = null

        splashActivityConfiguration()
        bottomItemsOnClickListener()
    }

        /**Method that makes the bottom nav bar invisible*/
       fun hideAppBar() {
            binding.bottomContainerFragment.visibility = View.GONE
        }

        /**Method that makes the bottom nav bar invisible*/
        fun showAppBar() {
            binding.bottomContainerFragment.visibility = View.VISIBLE
        }

        fun bottomItemsOnClickListener() {

            //Onclick listener for the fab cart button that takes it to the cartList fragment
            binding.fragmentFabCartBtn.setOnClickListener {
                if (!cartAlreadyClicked) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, cartListFragment)
                        addToBackStack(null)
                        commit()
                    }

                    cartAlreadyClicked = true
                    homeAlreadyClicked = false
                    profileAlreadyClicked = false
                    supportAlreadyClicked = false
                    settingsAlreadyClicked = false
                }
            }

            //Onclick listener for the home button that takes it to the home fragment
            binding.bottomNavigationViewFragment.setOnItemSelectedListener {
                when (it.itemId) {

                    //Onclick listener for the Home button that takes it to the home fragment
                    R.id.menuHome -> {
                        if (!homeAlreadyClicked) {
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, homeFragment)
                                addToBackStack(null)
                                commit()

                            }
                            homeAlreadyClicked = true
                            cartAlreadyClicked = false
                            profileAlreadyClicked = false
                            supportAlreadyClicked = false
                            settingsAlreadyClicked = false
                        }
                    }

                    //Onclick listener for the profile button that takes it to the profile fragment
                    R.id.menuProfile -> {
                        if (!cartAlreadyClicked) {
                            profileAlreadyClicked = true
                            supportAlreadyClicked = false
                            settingsAlreadyClicked = false
                            homeAlreadyClicked = false
                            cartAlreadyClicked = false
                        }
                    }

                    //Onclick listener for the support button that takes it to the support fragment
                    R.id.menuSupport -> {
                        if (!cartAlreadyClicked) {
                            supportAlreadyClicked = true
                            settingsAlreadyClicked = false
                            homeAlreadyClicked = false
                            cartAlreadyClicked = false
                            profileAlreadyClicked = false
                        }
                    }

                    //Onclick listener for the settings button that takes it to the settings fragment
                    R.id.menuSettings -> {
                        if (!cartAlreadyClicked) {
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, loginFragment)
                                commit()

                                hideAppBar()  //Makes the bottom nav bar invisible in the splash activity
                            }

                            settingsAlreadyClicked = true
                            homeAlreadyClicked = false
                            cartAlreadyClicked = false
                            profileAlreadyClicked = false
                            supportAlreadyClicked = false
                        }
                    }
                }
                true
            }
        }



        /**Method that configures the actions that take place in the Splash Activity*/
        private fun splashActivityConfiguration(){

            //Sets the activity view to Splash fragment by default
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, splashFragment)
                commit()

                hideAppBar()  //Makes the bottom nav bar invisible in the splash activity
            }

            //Switches the fragment to the home fragment after the stipulated time
            Handler().postDelayed({
                val currentUserId = FirestoreClass().getCurrentUserId()

                //Checks if there is a logged in user already. If yes, it goes straight
                // to the home page and if not it goes to te login page
                if (currentUserId.isNotEmpty()) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, homeFragment)
                        commit()

                        showAppBar()
                    }
                } else {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, loginFragment)
                        commit()

                        hideAppBar()  //Makes the bottom nav bar invisible in the splash activity
                    }
                }

            }, 2500)
        }

}


