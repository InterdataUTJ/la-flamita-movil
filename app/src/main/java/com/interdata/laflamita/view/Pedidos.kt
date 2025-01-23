package com.interdata.laflamita.view

import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.interdata.laflamita.components.Pedido
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.navigation.AppScreens
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import org.json.JSONObject

@Composable
fun PedidosView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authController: AuthController
) {
    var cargando by rememberSaveable { mutableStateOf(true) }
    var pedidos by rememberSaveable { mutableStateOf("{}") }

    LaunchedEffect(Unit) {
        authController.getPedidos() { success, response ->
            if (success) pedidos = response.toString()
            cargando = false
            Log.d("Pedidos", pedidos)
        }
    }

    PedidosContent(
        modifier = modifier,
        pedidos = JSONObject(pedidos),
        cargando = cargando,
        onAtrasClick = {
            navController.popBackStack()
        },
        onPedidoClick = { pedidoId ->
            Log.d("Pedido", pedidoId)
            navController.navigate("${AppScreens.Detalle.route}/$pedidoId")
        }
    )
}

@Composable
fun PedidosContent(
    modifier: Modifier = Modifier,
    onAtrasClick: () -> Unit = {},
    pedidos: JSONObject? = null,
    cargando: Boolean = false,
    onPedidoClick: (String) -> Unit = {}
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
                    onClick = { if(!cargando) onAtrasClick() },
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
                    text = "Pedidos",
                    color = Color.Black,
                    lineHeight = 0.56.em,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        fontSize = 36.sp,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                pedidos?.let {
                    if (pedidos.length() <= 0) Text("No hay pedidos.", modifier = Modifier.align(Alignment.CenterHorizontally))
                    PedidoRecursivo(it, it.keys(), onPedidoClick = onPedidoClick)
                } ?: run {
                    Text("No hay pedidos.", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Composable
fun PedidoRecursivo(productos: JSONObject, iterador: Iterator<String>, onPedidoClick: (String) -> Unit = {}) {
    if (!iterador.hasNext()) return
    val key = iterador.next()

    Pedido(
        id = key,
        fecha = productos.getJSONObject(key).optString("fecha", "00/00/0000"),
        total = productos.getJSONObject(key).optString("total", "00.00"),
        onPedidoClick = onPedidoClick
    )
    PedidoRecursivo(productos, iterador, onPedidoClick)
}

@Composable
@Preview(device = "id:pixel_5", showBackground = true)
fun PedidosPreview() {
    val pedidos = JSONObject().apply {
        put("1", JSONObject().apply {
            put("fecha", "11/11/2024")
            put("total", "10.59")
        })
        put("2", JSONObject().apply {
            put("fecha", "12/11/2024")
            put("total", "99.50")
        })
    }

    LaFlamitaTheme {
        PedidosContent(pedidos = pedidos)
    }
}