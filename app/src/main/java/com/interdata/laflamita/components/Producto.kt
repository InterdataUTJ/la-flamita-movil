package com.interdata.laflamita.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.interdata.laflamita.R
import com.interdata.laflamita.view.theme.LaFlamitaTheme


@Composable
fun Producto(
    modifier: Modifier = Modifier,
    nombre: String = "Nombre del producto",
    descripcion: String = "Descripción del producto",
    precio: String = "00.00",
    imagen: String? = null,
    onAddToCartClick: () -> Unit = {},
    ctx: Context
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        modifier = modifier
            .fillMaxWidth()
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            imagen?.let {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(ctx)
                            .data(it)
                            .decoderFactory(SvgDecoder.Factory())
                            .build()
                    ),
                    contentDescription = "producto",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "producto",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 30.sp
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp
                )
                Text(
                    text = "$$precio MXN",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp
                )
            }
        }
        Button(
            onClick = { onAddToCartClick() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 40.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shopping_cart_plus),
                    contentDescription = "icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier
                        .requiredSize(size = 18.dp)
                )
                Text(
                    text = "Añadir al carrito",
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.43.em,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
        }
    }
}


@Composable
@Preview(device = "id:pixel_8a", showBackground = true)
fun ProductoPreview() {
    val ctx = LocalContext.current
    LaFlamitaTheme {
        Box(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Top),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Producto(
                    nombre = "Coca-Cola",
                    descripcion = "Refresco Coca-Cola en botella de vidrio de 600 ml.",
                    precio = "25.00",
                    ctx = ctx
                )
                Producto(
                    nombre = "Horchata",
                    descripcion = "Agua sabor horchata servida en un vaso de vidrio con canela en polvo.",
                    precio = "25.00",
                    ctx = ctx
                )
            }
        }

    }
}