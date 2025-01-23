package com.interdata.laflamita.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import coil3.svg.SvgDecoder
import com.interdata.laflamita.R
import com.interdata.laflamita.components.Producto
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun DetalleView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authController: AuthController,
    pedidoId: String = "",
    ctx: Context
) {
    var cargando by rememberSaveable { mutableStateOf(true) }
    var pedido by rememberSaveable { mutableStateOf("{}") }

    LaunchedEffect(Unit) {
        authController.getDetalle(pedidoId) { success, response ->
            if (success) pedido = response.toString()
            cargando = false
            Log.d("Pedido", pedido)
        }
    }

    DetalleContent(
        modifier = modifier,
        pedido = JSONObject(pedido),
        cargando = cargando,
        onAtrasClick = {
            navController.popBackStack()
        },
        ctx = ctx
    )
}

@Composable
fun DetalleContent(
    modifier: Modifier = Modifier,
    onAtrasClick: () -> Unit = {},
    pedido: JSONObject? = null,
    cargando: Boolean = false,
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
                    text = "Pedido #${pedido?.optString("id", "??")}",
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
                pedido?.let {
                    Text(
                        text = "Estado",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    if (it.optString("estado", "ERROR") == "PAGADO") {
                        Text(
                            text = "Pedido pendiente de recoger",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(ctx)
                                        .data("https://laflamita.live/api/pedido/token/${it.optString("token", "")}")
                                        .decoderFactory(SvgDecoder.Factory())
                                        .size(Size.ORIGINAL)
                                        .build()
                                ),
                                contentDescription = "producto",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = it.optString("token", "ERROR"),
                                fontSize = 18.sp,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    } else if (it.optString("estado", "ERROR") == "COMPLETADO") {
                        Text(
                            text = "Pedido ya entregado",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Text(
                            text = "Pedido cancelado",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    HorizontalDivider()
                    Text(
                        text = "Resumen",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Subtotal: $${it.optString("subtotal", "00.00")} MXN",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Descuento: -$${it.optString("descuentos", "00.00")} MXN",
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "Total: $${it.optString("total", "00.00")} MXN",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    HorizontalDivider()
                    Text(
                        text = "Productos",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    it.optJSONArray("productos")?.let { productos ->
                        for (i in 0 until productos.length()) {
                            val producto = productos.getJSONObject(i)
                            Producto(
                                nombre = producto.optString("nombre", "Producto"),
                                precio = producto.optString("precio", "00.00"),
                                cantidad = producto.optString("cantidad", "0"),
                                descuento = producto.optString("descuento", "00.00"),
                            )
                        }
                    }
                } ?: run {
                    Text(
                        text = "No se encontraron datos",
                        color = Color.Black,
                        lineHeight = 0.56.em,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun Producto(
    nombre: String,
    precio: String,
    cantidad: String,
    descuento: String,
) {
    val precioNumero = precio.toDoubleOrNull() ?: 0.0
    val descuentoPorcentaje = descuento.toDoubleOrNull() ?: 0.0
    val descuentoPesos =  (precioNumero * descuentoPorcentaje / 100)
    val precioDescuento = precioNumero - descuentoPesos
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
        modifier = Modifier.fillMaxWidth()
            .border(
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.onSecondary),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = nombre,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 19.sp,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),

            ) {
            if (descuentoPesos > 0.0) {
                Text(
                    text = "$${precioNumero} MXN",
                    textDecoration = TextDecoration.LineThrough,
                )
            }
            Text(
                text = "$${precioDescuento} MXN",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "x $cantidad",
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
@Preview(device = "id:pixel_5", showBackground = true)
fun DetallePreview() {
    val datos = JSONObject().apply {
        put("id", "1")
        put("total", "10.00")
        put("subtotal", "10.00")
        put("descuentos", "0.00")
        put("estado", "PAGADO")
        put("token", "fpodsikjjdks")
        put("productos", JSONArray().apply {
            put(JSONObject().apply {
                put("nombre", "Coca-Cola")
                put("cantidad", "1")
                put("precio", "10.00")
                put("descuento", "50.00")
            })
            put(JSONObject().apply {
                put("nombre", "Coca-Cola")
                put("cantidad", "1")
                put("precio", "10.00")
                put("descuento", "50.00")
            })
        })
    }
    LaFlamitaTheme {
        DetalleContent(
            pedido = datos,
            ctx = LocalContext.current
        )
    }
}