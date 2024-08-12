package com.example.pushnotifylillydemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pushnotifylillydemo.data.database.PushNotifyDatabase
import com.example.pushnotifylillydemo.ui.screens.LoginScreen
import com.example.pushnotifylillydemo.ui.screens.RegistrationScreen
import com.example.pushnotifylillydemo.ui.screens.TabScreen
import com.example.pushnotifylillydemo.utility.PreferencesHelper

/**
 * Main activity class
 */

class MainActivity : ComponentActivity() {
    private lateinit var db: PushNotifyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = (application as PushNotifyApp).db

        // Check if the intent contains the extra to navigate to Support and the URL
        val shouldNavigateToSupport = intent.getBooleanExtra("navigate_to_support", false)
        val urlToLoad = intent.getStringExtra("url_to_load")

        Log.d("MainActivity", "shouldNavigateToSupport: $shouldNavigateToSupport")
        Log.d("MainActivity", "urlToLoad: $urlToLoad")
        setContent {
            val navController = rememberNavController()

            // Navigation setup
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, db) }
                composable("register") { RegistrationScreen(navController, db) }
                composable("tabs/{drug}/{email}") { backStackEntry ->
                    val drug = backStackEntry.arguments?.getString("drug") ?: ""
                    val email = backStackEntry.arguments?.getString("email") ?: ""

                    val initialTabIndex = if (shouldNavigateToSupport) 1 else 0
                    Log.d("MainActivity", "initialTabIndex: $initialTabIndex")
                    TabScreen(
                        userDrug = drug,
                        userEmail = email,
                        navController = navController,
                        db = db,
                        initialTabIndex = initialTabIndex,
                        urlToLoad = if (shouldNavigateToSupport) urlToLoad else null
                    )
                }
            }

            // Check if the user is logged in and navigate accordingly
            LaunchedEffect(Unit) {
                Log.d("MainActivity", "LaunchedEffect")
                val userDrug = PreferencesHelper.getUserDrug(this@MainActivity)
                val userEmail = PreferencesHelper.getUserEmail(this@MainActivity)
                if (userDrug != null && userEmail != null) {
                    navController.navigate("tabs/$userDrug/$userEmail")
                }
            }
        }
    }
}