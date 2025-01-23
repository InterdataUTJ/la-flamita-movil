package com.interdata.laflamita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.navigation.AppNavigation
import com.interdata.laflamita.navigation.CustomNavigationBar
import com.interdata.laflamita.view.theme.LaFlamitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var fromUrl = false
        var estado = ""
        var mensaje = ""

        intent?.data?.let { uri ->
            if (uri.host != "venta") return@let
            val datos = uri.pathSegments
            if (datos.size != 2) return@let
            estado = datos[0]
            mensaje = datos[1]
            fromUrl = true
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.White.toArgb(),
                Color.Black.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.White.toArgb(),
                Color.Black.toArgb()
            )
        )
        setContent {

            Log.d("Created", "Hola")
            val navController = rememberNavController()
            val authController = AuthController(this)
            var isLoading by rememberSaveable { mutableStateOf(true) }
            var isLogged by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                authController.isLoggedIn { isLoggedIn ->
                    isLogged = isLoggedIn
                    isLoading = false
                }
            }

            splashScreen.setKeepOnScreenCondition { isLoading }


            LaFlamitaTheme {
                Scaffold(
                    bottomBar = { CustomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    if (!isLoading) {
                        AppNavigation(
                            Modifier.padding(innerPadding),
                            navController,
                            authController,
                            isLogged,
                            fromUrl,
                            estado,
                            mensaje
                        )
                    }
                }
            }
        }
    }
}