package com.interdata.laflamita.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.json.JSONObject

class ClienteModel(ctx: Context) : ApiModel(ctx) {
    fun login(correo: String, clave: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/login"
        val jsonBody = JSONObject().apply {
            put("correo", correo)
            put("clave", clave)
        }

        // Crear la solicitud
        request(Request.Method.POST, url, null, jsonBody, callback)
    }

    fun loginWithGoogle(idToken: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/google"
        val jsonBody = JSONObject().apply {
            put("token", idToken)
        }

        // Crear la solicitud
        request(Request.Method.POST, url, null, jsonBody, callback)
    }

    fun singup(nombre: String, apellido: String, correo: String, clave: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/singup"
        val jsonBody = JSONObject().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("correo", correo)
            put("clave", clave)
        }

        // Crear la solicitud
        request(Request.Method.POST, url, null, jsonBody, callback)
    }

    fun logout(token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/logout"

        // Crear la solicitud
        clearCache("$serverURL/api/profile")
        request(Request.Method.POST, url, token,null, callback)
    }

    fun getProfile(token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/profile"
        request(Request.Method.GET, url, token,null, callback, true)
    }

    fun isLoggedIn(token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/validar"
        request(Request.Method.GET, url, token,null, callback)
    }

    fun getPedidos(token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/pedidos"
        request(Request.Method.GET, url, token, null, callback)
    }

    fun getDetalle(pedidoId: String, token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/pedido/$pedidoId"
        request(Request.Method.GET, url, token, null, callback)
    }
}