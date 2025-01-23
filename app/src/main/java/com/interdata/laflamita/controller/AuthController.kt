package com.interdata.laflamita.controller

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.interdata.laflamita.model.ClienteModel
import org.json.JSONObject

open class AuthController(ctx: Context) : Controller(ctx) {
    private val model = ClienteModel(ctx)

    fun login(correo: String, clave: String, callback: (Boolean, JSONObject) -> Unit) {
        model.login(correo, clave) { success, response ->
            if (success) setAccessToken(response.getString("token"))
            callback(success, response)
        }
    }

    fun singup(nombre: String, apellido: String, correo: String, clave: String, confirmarClave: String, callback: (Boolean, JSONObject) -> Unit) {
        if (clave != confirmarClave) {
            callback(false, JSONObject("{\"message\":\"Ambas contraseñas deben ser iguales.\"}"))
            return
        }

        model.singup(nombre, apellido, correo, clave) { success, response ->
            if (success) setAccessToken(response.getString("token"))
            callback(success, response)
        }
    }

    fun logout(callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.logout(it) { success, response ->
                if (success) removeAccessToken()
                callback(success, response)
            }
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión no está iniciada.\"}"))
        }
    }



    fun isLoggedIn(callback: (Boolean) -> Unit) {
        getToken()?.let {
            model.isLoggedIn(it) { success, _ ->
                callback(success)
            }
        } ?: run {
            callback(false)
        }
    }

    fun getPerfil(callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.getProfile(it) { success, response ->
                callback(success, response)
            }
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión no está iniciada.\"}"))
        }
    }

    fun getPedidos(callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.getPedidos(it, callback)
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión no está iniciada.\"}"))
        }
    }

    fun getDetalle(pedidoId: String, callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.getDetalle(pedidoId, it, callback)
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión no está iniciada.\"}"))
        }
    }

    fun loginWithGoogle(credential: Credential, callback: (Boolean) -> Unit) {
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        model.loginWithGoogle(googleIdTokenCredential.idToken) { success, response ->
                            if (success) setAccessToken(response.getString("token"))
                            callback(success)
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("GoogleAuth", "Received an invalid google id token response", e)
                        callback(false)
                    }
                } else {
                    // Catch any unrecognized credential type here.
                    Log.e("GoogleAuth", "Unexpected type of credential")
                    callback(false)
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("GoogleAuth", "Unexpected type of credential")
                callback(false)
            }
        }
    }
}