package com.example.pushnotifylillydemo.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(urlToLoad: String?) {
    // Initialize the bottom sheet state
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Show the bottom sheet only if a valid URL is provided
    if (urlToLoad != null) {
        LaunchedEffect(urlToLoad) {
            scope.launch {
                bottomSheetState.show()  // Show the bottom sheet when the URL is available
            }
        }

        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                scope.launch { bottomSheetState.hide() }  // Handle dismissal
            }
        ) {
            WebViewScreen(urlToLoad)
        }
    }

    // Default support screen content
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center  // Center the content
    ) {
        Text(
            text = "Support Screen Content",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun WebViewScreen(url: String?) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            WebView(context).apply {
                webViewClient = WebViewClient()
                url?.let { loadUrl(it) }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}