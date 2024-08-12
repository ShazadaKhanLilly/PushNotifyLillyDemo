package com.example.pushnotifylillydemo.ui.screens

import android.util.Log
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch

/**
 * Support screen composable function
 * @param urlToLoad: String?
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(urlToLoad: String?) {
    BackHandler {
        // Do nothing on back press
    }

    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Log.d("SupportScreen", "URL to load: $urlToLoad")

    // Show the bottom sheet only if a valid URL is provided
    if (urlToLoad != null) {
        LaunchedEffect(urlToLoad) {
            scope.launch {
                bottomSheetState.show()
            }
        }

        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                scope.launch {
                    bottomSheetState.hide() // Handle dismissal
                }
            }
        ) {
            WebViewScreen(urlToLoad)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
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
                settings.javaScriptEnabled = true

                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = true

                // Ensure the web content is resized correctly when the WebView is smaller
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true

                url?.let { loadUrl(it) }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}