package com.interdata.laflamita.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.credentials.Credential
import androidx.navigation.NavController
import com.interdata.laflamita.R
import com.interdata.laflamita.components.GoogleButton
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.navigation.AppScreens
import com.interdata.laflamita.view.theme.LaFlamitaTheme

@Composable
fun SingupView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authController: AuthController,
    ctx: Context
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmarClave by rememberSaveable { mutableStateOf("") }
    var isCargando by rememberSaveable { mutableStateOf(false) }

    SingupContent(
        modifier = modifier,
        nombre = nombre,
        apellido = apellido,
        correo = correo,
        clave = clave,
        confirmarClave = confirmarClave,
        isCargando = isCargando,
        onNombreChange = { nombre = it },
        onApellidoChange = { apellido = it },
        onCorreoChange = { correo = it },
        onClaveChange = { clave = it },
        onConfirmarClaveChange = { confirmarClave = it },
        onRegisterClick = {
            isCargando = true
            authController.singup(nombre, apellido, correo, clave, confirmarClave) { success, messageData ->
                isCargando = false
                if (!success) Toast.makeText(ctx, messageData.getString("message"), Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(ctx, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("menu") { popUpTo(AppScreens.Login.route) { inclusive = true } }
                }
            } },
        onGoogleClick = { credential: Credential ->
            isCargando = true
            authController.loginWithGoogle(credential) { success ->
                isCargando = false
                if (!success) Toast.makeText(ctx, "Error al crear cuenta con Google", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(ctx, "Cuenta creada con exito", Toast.LENGTH_SHORT).show()
                    navController.navigate("menu") { popUpTo(AppScreens.Login.route) { inclusive = true } }
                }
            }
        },
        onLoginClick = { navController.popBackStack() },
        ctx = ctx
    )
}

@Composable
fun SingupContent(
    modifier: Modifier = Modifier,
    nombre: String,
    apellido: String,
    correo: String,
    clave: String,
    confirmarClave: String,
    isCargando: Boolean,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onConfirmarClaveChange: (String) -> Unit,
    onRegisterClick: () -> Unit = {},
    onGoogleClick: (Credential) -> Unit = {},
    onLoginClick: () -> Unit = {},
    ctx: Context
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 10.dp, horizontal = 30.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(35.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .widthIn(max = 360.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear una cuenta",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                CustomOutlinedInput(
                    valor = nombre,
                    onValorChange = { onNombreChange(it) },
                    label = "Nombre",
                )
                CustomOutlinedInput(
                    valor = apellido,
                    onValorChange = { onApellidoChange(it) },
                    label = "Apellido",
                )
                CustomOutlinedInput(
                    valor = correo,
                    onValorChange = { onCorreoChange(it) },
                    label = "Correo electronico",
                )
                CustomOutlinedInput(
                    valor = clave,
                    onValorChange = { onClaveChange(it) },
                    label = "Contraseña",
                    isPassword = true
                )
                CustomOutlinedInput(
                    valor = confirmarClave,
                    onValorChange = { onConfirmarClaveChange(it) },
                    label = "Confirmar contraseña",
                    isPassword = true,
                    onSend = { if (!isCargando) onRegisterClick() },
                    isLast = true
                )
                Button(
                    onClick = { if (!isCargando) onRegisterClick() },
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
                            painter = painterResource(id = R.drawable.pencil),
                            contentDescription = "icon",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .requiredSize(size = 18.dp)
                        )
                        Text(
                            text = "Crear cuenta",
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.43.em,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                GoogleButton(
                    isCargando = isCargando,
                    ctx = ctx,
                    onGetCredentialResponse = {credential -> onGoogleClick(credential) },
                )
                OutlinedButton(
                    onClick = { if (!isCargando) onLoginClick() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
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
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "icon",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                            modifier = Modifier
                                .fillMaxHeight(0.8f))
                        Text(
                            text = "Iniciar sesión",
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.43.em,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically))
                    }

                }
            }
        }
    }
}

@Composable
fun CustomOutlinedInput(
    modifier: Modifier = Modifier,
    valor: String,
    onValorChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    onSend: () -> Unit = {},
    isLast: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { onValorChange(it) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLast) androidx.compose.ui.text.input.ImeAction.Send else androidx.compose.ui.text.input.ImeAction.Next,
        ),
        keyboardActions = if (isLast) KeyboardActions( onSend = { onSend() } ) else KeyboardActions.Default,
        label = {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                lineHeight = 1.5.em,
                style = MaterialTheme.typography.bodyLarge)
        },
        modifier = modifier.fillMaxWidth()
    )
}


@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SingupPreview() {
    LaFlamitaTheme {
        SingupContent(
            modifier = Modifier,
            nombre = "",
            apellido = "",
            correo = "",
            clave = "",
            confirmarClave = "",
            isCargando = false,
            onNombreChange = {},
            onApellidoChange = {},
            onCorreoChange = {},
            onClaveChange = {},
            onConfirmarClaveChange = {},
            onRegisterClick = {},
            onGoogleClick = {},
            onLoginClick = {},
            ctx = LocalContext.current
        )
    }
}
