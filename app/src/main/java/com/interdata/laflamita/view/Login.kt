package com.interdata.laflamita.view

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.interdata.laflamita.R
import com.interdata.laflamita.components.GoogleButton
import com.interdata.laflamita.controller.AuthController
import com.interdata.laflamita.navigation.AppScreens
import com.interdata.laflamita.view.theme.LaFlamitaTheme
import kotlinx.coroutines.launch


@Composable
fun LoginView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authController: AuthController,
    ctx: Context
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var isCargando by rememberSaveable { mutableStateOf(false) }


    LoginViewContent(
        correo = correo,
        onCorreoChange = { correo = it },
        clave = clave,
        onClaveChange = { clave = it },
        isCargando = isCargando,
        onLoginClick = {
            isCargando = true
            authController.login(correo, clave) { success, messageData ->
                isCargando = false
                if (!success) Toast.makeText(ctx, messageData.getString("message"), Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(ctx, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("menu") { popUpTo(AppScreens.Login.route) { inclusive = true } }
                }
            }
        },
        onRegisterClick = { navController.navigate("singup") },
        onGoogleClick = { credential: Credential ->
            isCargando = true
            authController.loginWithGoogle(credential) { success ->
                isCargando = false
                if (!success) Toast.makeText(ctx, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(ctx, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("menu") { popUpTo(AppScreens.Login.route) { inclusive = true } }
                }
            }
        },
        modifier = modifier,
        ctx = ctx
    )
}

@Composable
fun LoginViewContent(
    modifier: Modifier = Modifier,
    correo: String,
    clave: String,
    isCargando: Boolean,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: (Credential) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    ctx: Context
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(35.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .widthIn(max = 390.dp)
                .padding(vertical = 10.dp, horizontal = 30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.la_flamita),
                contentDescription = "la-flamita logo",
                modifier = Modifier.fillMaxWidth(0.4f)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Iniciar sesión",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { onCorreoChange(it) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    ),
                    label = {
                        Text(
                            text = "Correo electrónico",
                            textAlign = TextAlign.Center,
                            lineHeight = 1.5.em,
                            style = MaterialTheme.typography.bodyLarge)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = clave,
                    onValueChange = { onClaveChange(it) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions( onSend = { if (!isCargando) onLoginClick() } ),
                    label = {
                        Text(
                            text = "Contraseña",
                            textAlign = TextAlign.Center,
                            lineHeight = 1.5.em,
                            style = MaterialTheme.typography.bodyLarge)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { if (!isCargando) onLoginClick() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                        if (isCargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(height = 18.dp, width = 18.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "icon",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .requiredSize(size = 18.dp)
                        )
                        Text(
                            text = "Iniciar sesión",
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
                    onClick = { if (!isCargando) onRegisterClick() },
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
                                painter = painterResource(id = R.drawable.pencil),
                                contentDescription = "icon",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                                modifier = Modifier
                                    .fillMaxHeight(0.8f))
                            Text(
                                text = "Crear una cuenta",
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


@Preview(device = "id:pixel_8a")
@Composable
private fun LoginMobilePreview() {
    LaFlamitaTheme {
        LoginViewContent(
            correo = "",
            clave = "",
            isCargando = false,
            onCorreoChange = {},
            onClaveChange = {},
            onLoginClick = {},
            onGoogleClick = {},
            onRegisterClick = {},
            ctx = LocalContext.current
        )
    }
}
