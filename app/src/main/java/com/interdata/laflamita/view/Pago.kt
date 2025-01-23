package com.interdata.laflamita.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.interdata.laflamita.navigation.AppScreens
import com.interdata.laflamita.view.theme.LaFlamitaTheme

@Composable
fun PagoView(
    modifier: Modifier = Modifier,
    navController: NavController,
    estado: Boolean = false,
    mensaje: String = "Pedido cancelado.",
) {
    PagoContent(
        modifier = modifier,
        estado = estado,
        mensaje = mensaje,
        onAtrasClick = {
            navController.navigate(AppScreens.Menu.route) {
                popUpTo(AppScreens.Pago.route) {
                    inclusive = true
                }
            }
        },
        onPedidosClick = {
            navController.navigate(AppScreens.Pedidos.route)
        },
    )
}

@Composable
fun PagoContent(
    modifier: Modifier = Modifier,
    estado: Boolean = true,
    mensaje: String = "3",
    onAtrasClick: () -> Unit = {},
    onPedidosClick: () -> Unit = {},
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
                    text = "Resumen",
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

            if (estado) {
                Text(
                    text = "Pago realizado con exito.",
                    color = Color.Black,
                    lineHeight = 0.56.em,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp)
                )
                Text(
                    text = "Puedes consultar tu pedido en la sección de \"Mis pedidos\"",
                    color = Color.Black,
                    lineHeight = 1.5.em,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp)
                )

                Button(
                    onClick = { onPedidosClick() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
                    modifier = Modifier.fillMaxWidth().requiredHeight(height = 40.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "icon",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                            modifier = Modifier
                                .requiredSize(size = 18.dp)
                        )
                        Text(
                            text = "Mis pedidos",
                            color = MaterialTheme.colorScheme.onTertiary,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.43.em,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                }

            } else {
                Text(
                    text = mensaje,
                    color = Color.Black,
                    lineHeight = 0.56.em,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp)
                )
            }
        }
    }
}

@Composable
@Preview(device = "id:pixel_5", showBackground = true)
fun PagoPreview() {
    LaFlamitaTheme {
        PagoContent()
    }
}