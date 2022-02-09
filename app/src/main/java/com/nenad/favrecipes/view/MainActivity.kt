package com.nenad.favrecipes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nenad.favrecipes.R
import com.nenad.favrecipes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mNavController: NavController
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)



        mNavController = findNavController(R.id.fragment_container)
        val appBarConfiguration = AppBarConfiguration(
           setOf(R.id.searchFragment, R.id.favFragment)
        )
        mBinding.bottomNavigationView.setupWithNavController(mNavController)

        setupActionBarWithNavController(mNavController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }
}