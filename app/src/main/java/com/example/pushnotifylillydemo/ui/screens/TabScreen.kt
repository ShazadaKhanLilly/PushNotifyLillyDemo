package com.example.pushnotifylillydemo.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pushnotifylillydemo.data.database.PushNotifyDatabase

/**
 * Tab screen composable function
 * @param userDrug: String
 * @param userEmail: String
 * @param navController: NavHostController
 * @param db: PushNotifyDatabase
 */

@Composable
fun TabScreen(
    userDrug: String,
    userEmail: String,
    navController: NavHostController,
    db: PushNotifyDatabase,
    initialTabIndex: Int = 0,
    urlToLoad: String? = null  // Pass the URL to the Support screen
) {
    val tabs = listOf("Home", "Support", "Profile")
    var selectedTab by remember { mutableStateOf(initialTabIndex) }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedTab) {
                    0 -> HomeScreen(userDrug)
                    1 -> SupportScreen(urlToLoad)  // Pass the URL to SupportScreen
                    2 -> ProfileScreen(navController = navController, db = db, userEmail = userEmail)
                }
            }
        }
    )
}

@Composable
fun HomeScreen(drug: String) {
    BackHandler {
        // Do nothing on back press
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "This is a $drug user!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
    }
}
