package com.example.pushnotifylillydemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pushnotifylillydemo.data.database.PushNotifyDatabase
import com.example.pushnotifylillydemo.ui.screens.LoginScreen
import com.example.pushnotifylillydemo.ui.screens.RegistrationScreen
import com.example.pushnotifylillydemo.ui.screens.TabScreen

/**
 * App navigation composable function
 * @param navController: NavHostController
 * @param db: PushNotifyDatabase
 */

@Composable
fun AppNavigation(navController: NavHostController, db: PushNotifyDatabase) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, db) }
        composable("register") { RegistrationScreen(navController, db) }
        composable("tabs/{drug}/{email}") { backStackEntry ->
            val drug = backStackEntry.arguments?.getString("drug") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            TabScreen(userDrug = drug, userEmail = email, navController = navController, db = db)
        }
    }
}