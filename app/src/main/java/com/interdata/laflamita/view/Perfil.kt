package com.interdata.laflamita.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.interdata.laflamita.R
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.navigation.AppScreens
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import org.json.JSONObject

@Composable
fun PerfilView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authController: AuthController,
    ctx: Context
) {
    var perfil by rememberSaveable { mutableStateOf("{}") }
    var imageUrl by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authController.getPerfil { success, response ->
            if (!success) Toast.makeText(ctx, response.getString("message"), Toast.LENGTH_SHORT).show()
            perfil = response.toString()
            Log.d("Perfil", perfil)
        }
    }

    var isCargando by rememberSaveable { mutableStateOf(false) }
    imageUrl = JSONObject(perfil).optString("avatar", "")

    val avatar = rememberAsyncImagePainter(
        model = ImageRequest.Builder(ctx)
            .data(imageUrl)
            .decoderFactory(SvgDecoder.Factory())
            .build()
    )

    PerfilContent(
        modifier = modifier,
        avatar = avatar,
        nombre = JSONObject(perfil).optString("nombre", "Desconocido"),
        estado = JSONObject(perfil).optBoolean("estado"),
        correo = JSONObject(perfil).optString("correo", "Desconocido"),
        verificado = JSONObject(perfil).optBoolean("verificado"),
        redSocial = JSONObject(perfil).optBoolean("google"),
        onLogoutClick = {
            isCargando = true
            authController.logout { success, _ ->
                isCargando = false
                if (!success) return@logout
                navController.popBackStack()
                navController.navigate(AppScreens.Login.route)
            }
        },
        onPedidosClick = {
            navController.navigate(AppScreens.Pedidos.route)
        },
        ctx = ctx
    )
}

@Composable
fun PerfilContent(
    modifier: Modifier = Modifier,
    avatar: AsyncImagePainter?,
    nombre: String,
    estado: Boolean,
    correo: String,
    verificado: Boolean,
    redSocial: Boolean,
    onLogoutClick: () -> Unit = {},
    onPedidosClick: () -> Unit = {},
    isCargando: Boolean = false,
    ctx: Context
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 390.dp)
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 50.dp)
                    .padding(top = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.la_flamita),
                    contentDescription = "la-flamita",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                )
                Text(
                    text = "Perfil",
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(10)
                    )
            ) {
                avatar?.let {
                    Image(
                        painter = avatar,
                        contentDescription = "perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .clip(CircleShape)
                            .size(80.dp)
                    )
                }
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 15.dp)
                )
            }

            PerfilData(
                label = "Estado",
                value = if (estado) "Activo" else "Inactivo",
                iconInt = R.drawable.user
            )
            PerfilData(
                label = "Correo electrónico",
                value = correo,
                icon = Icons.Outlined.Email
            )
            PerfilData(
                label = "Correo estado",
                value = if (verificado) "Verificado" else "No verificado",
                icon = Icons.Outlined.Email
            )
            PerfilData(
                label = "Red social",
                value = if (redSocial) "Vinculada" else "No vinculada",
                iconInt = R.drawable.google,
                color = false
            )

            Button(
                onClick = { if (!isCargando) onPedidosClick() },
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
                    if (isCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(height = 18.dp, width = 18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
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

            Button(
                onClick = { if (!isCargando) onLogoutClick() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
                modifier = Modifier.fillMaxWidth().requiredHeight(height = 40.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (isCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(height = 18.dp, width = 18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                    Image(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "icon",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                        modifier = Modifier
                            .requiredSize(size = 18.dp)
                    )
                    Text(
                        text = "Cerrar sesión",
                        color = MaterialTheme.colorScheme.onTertiary,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.43.em,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
fun PerfilData(label: String, value: String, icon: ImageVector? = null, iconInt: Int? = null, color: Boolean = true ) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 20.dp)
    ){
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
        }

        iconInt?.let {
            Image(
                painter = painterResource(it),
                contentDescription = "icon",
                colorFilter = if (color) ColorFilter.tint(MaterialTheme.colorScheme.secondary) else null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun PerfilPreview() {
    LaFlamitaTheme {
    PerfilContent(
        avatar = null,
        nombre = "Ismael Cortés Gutiérrez",
        estado = true,
        correo = "isma@gmail.com",
        verificado = true,
        redSocial = false,
        ctx = LocalContext.current
    )
    }
}