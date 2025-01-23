package com.interdata.laflamita.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.interdata.laflamita.R
import com.interdata.laflamita.controller.CarritoController
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import org.json.JSONArray
import org.json.JSONObject


@Composable
fun CarritoView(
    modifier: Modifier = Modifier,
    navController: NavController,
    carritoController: CarritoController,
    ctx: Context
) {
    var carrito by rememberSaveable { mutableStateOf("{}") }
    var isCargando by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        carritoController.getItems { success, response ->
            isCargando = false
            if (!success) Toast.makeText(ctx, response.optString("message", "Error al cargar el carrito"), Toast.LENGTH_SHORT).show()
            else carrito = response.toString()
            Log.d("Carrito", response.toString())
        }
    }

    CarritoContent(
        modifier = modifier,
        items = JSONObject(carrito).optJSONArray("items"),
        resumen = JSONObject(carrito).optJSONObject("resumen"),
        isCargando = isCargando,
        onRemoveClick = { id: String ->
            if (isCargando) return@CarritoContent
            isCargando = true
            carritoController.removeItem(id) { success, response ->
                isCargando = false
                if (!success) Toast.makeText(ctx, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                else carrito = response.toString()
                Log.d("Carrito", response.toString())
            }
        },
        onPagarClick = {
            if (isCargando) return@CarritoContent
            isCargando = true
            carritoController.procesarPedido() { success, response ->
                isCargando = false
                Log.d("Carrito", response.toString())
                if (!success) Toast.makeText(ctx, response.optString("message", "Error al procesar el pedido"), Toast.LENGTH_SHORT).show()
                else {
                    val url = response.optString("url", "ERROR")
                    if (url == "ERROR") {
                        Toast.makeText(ctx, "Error al procesar el pedido", Toast.LENGTH_SHORT).show()
                        return@procesarPedido
                    }

                    val i = Intent(Intent.ACTION_VIEW)
                    i.setData(Uri.parse(url))
                    startActivity(ctx, i, null)
                }
            }
        }
    )
}

@Composable
fun CarritoContent(
    modifier: Modifier = Modifier,
    items: JSONArray? = null,
    resumen: JSONObject? = null,
    isCargando: Boolean = false,
    onRemoveClick: (String) -> Unit = {},
    onPagarClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(all = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 50.dp)
                .padding(
                    horizontal = 5.dp,
                    vertical = 5.dp
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.la_flamita),
                contentDescription = "la-flamita",
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = 10.dp,
                        vertical = 1.dp
                    )
            )
            Text(
                text = "Carrito",
                color = Color.Black,
                lineHeight = 0.56.em,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(
                    fontSize = 36.sp,
                    letterSpacing = 1.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(1f)
                .weight(weight = 1f)
                .padding(all = 10.dp)
        ) {

            items?.let {
                for (i in 0 until it.length()) {
                    val item = it.getJSONObject(i)
                    DetalleCarrito(
                        nombre = item.optJSONObject("producto")?.optString("nombre", "Producto") ?: "Producto",
                        cantidad = item.optString("cantidad", "0"),
                        precio = item.optString("precio", "00.00"),
                        onRemoveClick = { onRemoveClick(item.optString("id", ""))  }
                    )
                }

                if (it.length() == 0){
                    Text("No hay productos en el carrito.")
                }
            } ?: run {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Subtotal",
                    color = Color.Black,
                    lineHeight = 2.08.em,
                    style = TextStyle(
                        fontSize = 12.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
                Text(
                    text = "$${resumen?.optString("subtotal", "00.00") ?: "00.00"}",
                    color = Color.Black,
                    lineHeight = 2.08.em,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Descuento",
                    color = Color.Black,
                    lineHeight = 2.08.em,
                    style = TextStyle(
                        fontSize = 12.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
                Text(
                    text = "-$${resumen?.optString("descuento", "00.00") ?: "00.00"}",
                    color = Color.Black,
                    lineHeight = 2.08.em,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp,
                        vertical = 15.dp
                    )
            ) {
                Text(
                    text = "Total",
                    color = Color.Black,
                    lineHeight = 1.56.em,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.1.sp,
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
                Text(
                    text = "$${resumen?.optString("total", "00.00") ?: "00.00"}",
                    color = Color.Black,
                    lineHeight = 1.25.em,
                    style = TextStyle(
                        fontSize = 20.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
            Button(
                onClick = { onPagarClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    if (isCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(height = 18.dp, width = 18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.receipt),
                        contentDescription = "Pay",
                        modifier = Modifier
                            .requiredWidth(width = 16.dp)
                            .requiredHeight(height = 20.dp)
                    )
                    Text(
                        text = "Pagar",
                        color = Color.White,
                        lineHeight = 1.92.em,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.1.sp
                        ),
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleCarrito(
    modifier: Modifier = Modifier,
    nombre: String = "Producto",
    cantidad: String = "0",
    precio: String = "00.00",
    onRemoveClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        Button(
            onClick = { onRemoveClick() },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.requiredWidth(50.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = "icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                    modifier = Modifier.requiredSize(size = 18.dp)
                )
            }
        }
        Text(
            text = nombre,
            color = Color.Black,
            lineHeight = 1.56.em,
            style = TextStyle(
                fontSize = 16.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(weight = 0.1f)
        )
        Text(
            text = "$cantidad x",
            color = Color.Black,
            lineHeight = 1.79.em,
            style = TextStyle(
                fontSize = 14.sp,
                letterSpacing = 0.1.sp
            ),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "$${precio}",
            color = Color.Black,
            lineHeight = 1.79.em,
            style = TextStyle(
                fontSize = 14.sp,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically))
    }
}

@Preview
@Composable
private fun CarritoPreview() {
    val items = JSONArray().apply {
        put(JSONObject().apply {
            put("nombre", "Producto 1")
            put("cantidad", "10")
            put("precio", "10.00")
        })
    }
    val resumen = JSONObject().apply {
        put("subtotal", "100.00")
        put("descuento", "10.00")
        put("total", "90.00")
    }
    LaFlamitaTheme {
        CarritoContent(
            modifier = Modifier,
            items = items,
            resumen = resumen
        )
    }
}