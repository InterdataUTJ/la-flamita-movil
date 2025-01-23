package com.interdata.laflamita.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.controller.CarritoController
import com.interdata.laflamita.controller.ProductoController
import com.interdata.laflamita.view.LoginView
import com.interdata.laflamita.view.SingupView
import com.interdata.laflamita.view.MenuView
import com.interdata.laflamita.view.CarritoView
import com.interdata.laflamita.view.DetalleView
import com.interdata.laflamita.view.PagoView
import com.interdata.laflamita.view.PedidosView
import com.interdata.laflamita.view.PerfilView
import com.interdata.laflamita.view.ProductosView

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authController: AuthController,
    isLogged: Boolean,
    fromUrl: Boolean = false,
    estado: String = "",
    mensaje: String = ""
) {
    val ctx = LocalContext.current
    val productoController = ProductoController(ctx)
    val carritoController = CarritoController(ctx)

    val startDestination = if (fromUrl && isLogged) AppScreens.Pago.route
        else if (isLogged) AppScreens.Menu.route
        else AppScreens.Login.route

    Log.d("AppNavigation", "isLogged: $isLogged, fromUrl: $fromUrl, startDestination: $startDestination")

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = ::slideInToRight,
        exitTransition = ::slideOutToRight,
        popEnterTransition = ::slideInToLeft,
        popExitTransition = ::slideOutToLeft
    ) {
        composable(AppScreens.Login.route) { LoginView(modifier, navController, authController, ctx) }
        composable(AppScreens.Singup.route) { SingupView(modifier, navController, authController, ctx) }
        composable(AppScreens.Menu.route) { MenuView(modifier, navController, productoController) }
        composable(AppScreens.Cart.route) { CarritoView(modifier, navController, carritoController, ctx ) }
        composable(AppScreens.Profile.route) { PerfilView(modifier, navController, authController, ctx) }
        composable(AppScreens.Pedidos.route) { PedidosView(modifier, navController, authController) }
        composable("${AppScreens.Productos.route}/{categoriaValor}/{categoriaId}") { backStackEntry ->
            if (backStackEntry.arguments == null || backStackEntry.arguments?.getString("categoriaId") == null || backStackEntry.arguments?.getString("categoriaValor") == null) {
                navController.popBackStack()
                Toast.makeText(ctx, "Error al cargar la categoría", Toast.LENGTH_SHORT).show()
            }

            val categoriaValor = backStackEntry.arguments?.getString("categoriaValor") ?: ""
            val categoriaId = backStackEntry.arguments?.getString("categoriaId") ?: ""

            ProductosView(modifier, navController, productoController, carritoController, categoriaId, categoriaValor, ctx)
        }
        composable("${AppScreens.Detalle.route}/{pedidoId}") { backStackEntry ->
            if (backStackEntry.arguments == null || backStackEntry.arguments?.getString("pedidoId") == null) {
                navController.popBackStack()
                Toast.makeText(ctx, "Error al cargar el pedido", Toast.LENGTH_SHORT).show()
            }

            val pedidoId = backStackEntry.arguments?.getString("pedidoId") ?: ""
            DetalleView(modifier, navController, authController, pedidoId, ctx)
        }
        composable(AppScreens.Pago.route) {
            PagoView(modifier, navController, estado == "aprobada", mensaje)
        }
    }
}

fun slideInToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(250)
    )
}

fun slideInToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(250)
    )
}

fun slideOutToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(250)
    )
}

fun slideOutToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(250)
    )
}