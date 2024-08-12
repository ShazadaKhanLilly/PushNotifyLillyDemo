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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Main activity class
 */
class MainActivity : ComponentActivity() {

    // Initialize the database instance
    private lateinit var db: PushNotifyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = (application as PushNotifyApp).db
        fetchToken() // Fetch the FCM token for this device

        // Extract intent data for navigation and URL loading
        val (shouldNavigateToSupport, urlToLoad) = extractIntentData(intent)

        Log.d("MainActivity", "Should navigate to support: $shouldNavigateToSupport")
        Log.d("MainActivity", "URL to load: $urlToLoad")

        setContent {
            val navController = rememberNavController()

            // Set up navigation with the extracted intent data
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, db) }
                composable("register") { RegistrationScreen(navController, db) }
                composable("tabs/{drug}/{email}") { backStackEntry ->
                    val drug = backStackEntry.arguments?.getString("drug") ?: ""
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    val savedDrug = PreferencesHelper.getUserDrug(this@MainActivity)

                    // Subscribe to topic if the drug is saved
                    if (!savedDrug.isNullOrEmpty()) {
                        subscribeToTopic(savedDrug)
                    }

                    // Set the initial tab based on whether we should navigate to support
                    val initialTabIndex = if (shouldNavigateToSupport) 1 else 0
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

            // Auto-login and navigate to the tabs screen if user is already logged in
            LaunchedEffect(Unit) {
                val userDrug = PreferencesHelper.getUserDrug(this@MainActivity)
                val userEmail = PreferencesHelper.getUserEmail(this@MainActivity)
                if (userDrug != null && userEmail != null) {
                    navController.navigate("tabs/$userDrug/$userEmail")
                }
            }
        }
    }

    // Subscribe the user to a topic for FCM
    private fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MainActivity", "Subscribed to topic: $topic")
                PreferencesHelper.saveSubscribedTopic(this, topic)
            } else {
                Log.e("MainActivity", "Failed to subscribe to topic: $topic")
            }
        }
    }

    // Fetch the FCM token for this device
    private fun fetchToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("MainActivity", "Current Token is: ($token)")
            // You can send this token to your server here if needed
        })
    }

    // Extracts intent data and provides fallback for missing values
    private fun extractIntentData(intent: android.content.Intent): Pair<Boolean, String> {
        // Primary keys to check
        var shouldNavigateToSupport = intent.getBooleanExtra("navigate_to_support", false)
        var urlToLoad = intent.getStringExtra("url_to_load") ?: ""

        // Fallback keys in case the primary ones are not found
        if (!shouldNavigateToSupport) {
            shouldNavigateToSupport = intent.getBooleanExtra("navigate", false)
        }
        if (urlToLoad.isEmpty()) {
            urlToLoad = intent.getStringExtra("url") ?: ""
            if (urlToLoad.isNotEmpty()) {
                shouldNavigateToSupport = true
            }
        }

        return Pair(shouldNavigateToSupport, urlToLoad)
    }
}