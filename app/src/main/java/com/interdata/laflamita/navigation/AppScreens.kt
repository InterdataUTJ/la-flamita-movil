package com.interdata.laflamita.navigation

sealed class AppScreens(val route: String) {
    data object Login : AppScreens("login")
    data object Singup : AppScreens("singup")

    data object Menu : AppScreens("menu")
    data object Cart : AppScreens("cart")
    data object Profile : AppScreens("profile")

    data object Productos : AppScreens("productos")
    data object Pedidos : AppScreens("pedidos")
    data object Pago : AppScreens("pago")
    data object Detalle : AppScreens("detalle")
}