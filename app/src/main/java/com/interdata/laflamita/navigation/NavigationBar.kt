package com.interdata.laflamita.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("menu", Icons.Default.Home, "Inicio")
    data object Cart : BottomNavItem("cart", Icons.Default.ShoppingCart, "Carrito")
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "Perfil")
}

val bottomNavRoutes = arrayOf(
    BottomNavItem.Home.route,
    BottomNavItem.Cart.route,
    BottomNavItem.Profile.route
)

@Composable
fun CustomNavigationBar(navController: NavController) {

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (currentRoute !in bottomNavRoutes) return

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.onSurface
    ) {
        // looping over each tab to generate the views and navigation for each item
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Cart,
            BottomNavItem.Profile
        )

        items.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface),
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(tabBarItem.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = tabBarItem.icon,
                        contentDescription = tabBarItem.label
                    )
                },
                label = { Text(tabBarItem.label) }
            )
        }
    }
}
