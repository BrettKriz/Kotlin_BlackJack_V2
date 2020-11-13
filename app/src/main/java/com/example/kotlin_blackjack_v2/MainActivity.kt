package com.example.kotlin_blackjack_v2

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Switch
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_blackjack_v2.ui.home.*
import com.example.kotlin_blackjack_v2.ui.dashboard.*
import com.example.kotlin_blackjack_v2.ui.notifications.*


class MainActivity : AppCompatActivity() {
    //val SharedVM: SharedViewModel = SharedViewModel()

    // Add a companion object to store shared vars
    /*companion object SHARED {
        // Forget reflection
        // COMPANIONS ARE GODLIKE!

        // vars
        var Cash = 400
        var MinBet = 100
        var UseCash = true
        var PName = "Player"
        val CashScaler = 4

        // funcs
    }
// */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Start Code Area
        //SharedVM = ViewModelProvider(this).get(SharedViewModel::class.java)


    }

}