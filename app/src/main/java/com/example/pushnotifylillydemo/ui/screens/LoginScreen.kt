package com.example.pushnotifylillydemo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pushnotifylillydemo.data.database.PushNotifyDatabase
import com.example.pushnotifylillydemo.utility.PreferencesHelper
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

/**
 * Login screen composable function
 * @param navController: NavHostController
 * @param db: PushNotifyDatabase
 */

@Composable
fun LoginScreen(navController: NavHostController, db: PushNotifyDatabase) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current  // Get the context here

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Push Notify Lilly \n   Demo Login",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email/Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val user = db.userDao().getUser(email, password)
                    if (user != null) {
                        PreferencesHelper.saveUserDrugAndEmail(
                            context = context,
                            drug = user.drug,
                            email = user.email
                        )
                        // Subscribe to the user's drug as a topic
                        FirebaseMessaging.getInstance().subscribeToTopic(user.drug)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("LoginScreen", "Subscribed to ${user.drug}")
                                } else {
                                    Log.d("LoginScreen", "Failed to subscribe to ${user.drug}")
                                }
                            }
                        navController.navigate("tabs/${user.drug}/${user.email}")
                    } else {
                        errorMessage = "Invalid email or password"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Register")
        }

        // Display error message if login fails
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
    }
}