package com.interdata.laflamita.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.interdata.laflamita.R
import com.interdata.laflamita.controller.ProductoController
import com.interdata.laflamita.navigation.AppScreens
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import org.json.JSONObject

@Composable
fun MenuView(
    modifier: Modifier = Modifier,
    navController: NavController,
    productoController: ProductoController
) {
    var categorias by rememberSaveable { mutableStateOf("{}") }

    LaunchedEffect(Unit) {
        productoController.getCategorias { success, response ->
            if (!success) return@getCategorias
            categorias = response.toString()
        }
    }

    MenuContent(
        modifier = modifier,
        categorias = JSONObject(categorias),
        onCategoriaClick = { categoriaId, categoriaValor ->
            navController.navigate("${AppScreens.Productos.route}/$categoriaValor/$categoriaId")
        }
    )
}

@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    categorias: JSONObject,
    onCategoriaClick: (String, String) -> Unit = { _, _ -> }
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .fillMaxWidth()
                        .requiredHeight(height = 50.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.la_flamita),
                        contentDescription = "la-flamita",
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 10.dp)
                    )
                    Text(
                        text = "Menú",
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
            }


            categorias.keys().forEach { key ->
                item() {
                    Button(
                        onClick = { onCategoriaClick(key, categorias.optString(key)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
                        modifier = Modifier.fillMaxWidth().requiredHeight(height = 120.dp)
                    ) {
                        Text(
                            text = categorias.optString(key),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuViewPreview() {
    LaFlamitaTheme {
        MenuContent(categorias = JSONObject().apply {
            put("1", "Tacos")
            put("2", "Ordenes")
            put("3", "Bebidas")
        })
    }
}