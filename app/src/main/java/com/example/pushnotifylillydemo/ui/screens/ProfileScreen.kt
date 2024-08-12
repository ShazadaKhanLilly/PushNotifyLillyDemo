package com.example.pushnotifylillydemo.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pushnotifylillydemo.data.User
import com.example.pushnotifylillydemo.data.database.PushNotifyDatabase
import com.example.pushnotifylillydemo.utility.PreferencesHelper
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

/**
 * Profile screen composable function
 * @param navController: NavHostController
 * @param db: PushNotifyDatabase
 * @param userEmail: String
 */

@Composable
fun ProfileScreen(navController: NavHostController, db: PushNotifyDatabase, userEmail: String) {
    BackHandler {
        // Do nothing on back press
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // State to hold the fetched user
    var user by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch user data using the email inside a LaunchedEffect
    LaunchedEffect(userEmail) {
        try {
            val fetchedUser = db.userDao().getUserByEmail(userEmail)
            if (fetchedUser != null) {
                user = fetchedUser
            } else {
                errorMessage = "User not found."
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred while fetching user data."
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Display error message if there's an error
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Display user's name and logout button if user is found
        user?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center),  // Align column in the center
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "User: ${it.email}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(it.drug)
                        Log.d("ProfileScreen", "Unsubscribed from ${it.drug}")
                        // Clear user data and navigate to the login screen
                        scope.launch {
                            PreferencesHelper.clearUserData(context)
                            PreferencesHelper.removeSubscribedTopic(context,it.drug)
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true  // Clear the backstack up to the start destination (login)
                                }
                                launchSingleTop = true  // Ensure that the same instance of login is not recreated
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }

        // Show a loading message while fetching user data
        if (user == null && errorMessage == null) {
            Text(
                "Loading user data...",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}