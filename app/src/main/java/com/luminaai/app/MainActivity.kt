package com.luminaai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luminaai.app.ui.screens.*
import com.luminaai.app.ui.theme.LuminaAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuminaAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LuminaNavigation()
                }
            }
        }
    }
}

@Composable
fun LuminaNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        } else if (isLoggedIn == false) {
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn == true) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                }
            )
        }
    }
}
