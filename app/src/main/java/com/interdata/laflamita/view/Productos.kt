package com.interdata.laflamita.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.interdata.laflamita.components.Producto
import com.interdata.laflamita.controller.CarritoController
import com.interdata.laflamita.controller.ProductoController
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import org.json.JSONObject

@Composable
fun ProductosView(
    modifier: Modifier = Modifier,
    navController: NavController,
    productoController: ProductoController,
    carritoController: CarritoController,
    categoriaId: String = "",
    categoriaValor: String = "",
    ctx: Context
) {
    var cargando by rememberSaveable { mutableStateOf(true) }
    var waiting by rememberSaveable { mutableStateOf(true) }
    var productos by rememberSaveable { mutableStateOf("{}") }

    LaunchedEffect(categoriaId) {
        productoController.getProductos(categoriaId) { success, response ->
            if (success) productos = response.toString()
            cargando = false
            waiting = false
            Log.d("Productos", productos)
        }
    }

    ProductosContent(
        modifier = modifier,
        categoria = categoriaValor,
        productos = JSONObject(productos),
        onAtrasClick = { navController.popBackStack() },
        cargando = cargando,
        ctx = ctx,
        onAgregarClick = { productoId: String ->
            if (waiting) return@ProductosContent
            waiting = true
            carritoController.addItem(productoId) { success ->
                waiting = false
                if (!success) Toast.makeText(ctx, "Error al agregar el producto", Toast.LENGTH_SHORT).show()
                else Toast.makeText(ctx, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
            }
        }
    )

}


@Composable
fun ProductosContent(
    modifier: Modifier = Modifier,
    categoria: String,
    onAtrasClick: () -> Unit = {},
    onAgregarClick: (String) -> Unit = {},
    productos: JSONObject = JSONObject("{}"),
    cargando: Boolean = true,
    ctx: Context
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 390.dp)
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 50.dp)
                    .padding(top = 5.dp)
            ) {
                Button(
                    onClick = { onAtrasClick() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier.requiredWidth(50.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "icon",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            modifier = Modifier.requiredSize(size = 18.dp)
                        )
                    }
                }
                Text(
                    text = categoria,
                    color = Color.Black,
                    lineHeight = 0.56.em,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        fontSize = 36.sp,
                        letterSpacing = 1.sp),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                productos.keys().forEach { key ->
                    val producto = productos.optJSONObject(key) ?: JSONObject("{}")
                    Producto(
                        nombre = producto.optString("nombre", "Producto"),
                        descripcion = producto.optString("descripcion", "Descripción del producto"),
                        precio = producto.optString("precio", "00.00"),
                        imagen = producto.opt("foto")?.toString(),
                        ctx = ctx,
                        onAddToCartClick = { onAgregarClick(producto.optString("id", "0")) }
                    )
                }

                if (productos.length() == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay productos disponibles",
                            color = Color.Black,
                            lineHeight = 0.56.em,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

        }
    }
}


@Composable
@Preview(showBackground = true, device = "id:pixel_5")
fun ProductosPreview() {
    val productos = JSONObject().apply {
        put("1", JSONObject().apply {
            put("nombre", "Coca-Cola")
            put("descripcion", "Refresco Coca-Cola en botella de vidrio de 600 ml.")
            put("precio", "25.00")
        })
        put("2", JSONObject().apply {
            put("nombre", "Horchata")
            put("descripcion", "Agua sabor horchata servida en un vaso de vidrio con canela en polvo.")
            put("precio", "25.00")
        })
    }

    LaFlamitaTheme {
        ProductosContent(
            categoria = "Bebidas",
            ctx = LocalContext.current,
            productos = productos,
            cargando = false
        )
    }
}