package com.interdata.laflamita.model

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

// Este modelo se encarga de abstraer las llamadas a API en otros modelos
// No se usa directamente, solo se hereda en otros modelos
open class ApiModel(ctx: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(ctx)
    protected val serverURL = "https://laflamita.live"

    protected fun clearCache(key: String) {
        requestQueue.cache.remove(key)
    }

    protected fun request(
        method: Int,
        url: String,
        token: String?,
        body: JSONObject?,
        callback: (Boolean, JSONObject) -> Unit,
        cache: Boolean = false
    ) {
        val jsonRequest = object : JsonObjectRequest(
            method,
            url,
            body,
            { response ->
                callback(true, response)
            },
            { error: VolleyError ->
                // Manejar el error
                error.networkResponse?.let { response ->
                    // Error dado por laravel
                    Log.d("ApiModel", "Error: ${response.statusCode}")
                    val jsonString = String(response.data, Charsets.UTF_8)
                    val jsonResponse = JSONObject(jsonString)
                    Log.d("ApiModel", "Response: $jsonString")
                    callback(false, jsonResponse)
                } ?: run {
                    // Si no hay respuesta, se trata de un error de conexión
                    Log.d("ApiModel", "Error: ${error.message.orEmpty()}")
                    callback(false, JSONObject().apply { put("message", "Error de conexión") })
                }
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                if (token != null) headers["Authorization"] = "Bearer $token"
                return headers
            }

        }.apply {
            setShouldCache(cache)
        }

        // Añadir la solicitud a la cola
        requestQueue.add(jsonRequest)
    }


    protected fun emptyRequest(
        method: Int,
        url: String,
        token: String?,
        body: JSONObject?,
        callback: (Boolean) -> Unit,
        cache: Boolean = false
    ) {
        val request = object : StringRequest(
            method,
            url,
            { _ ->
                callback(true)
            },
            { error: VolleyError ->
                // Manejar el error
                error.networkResponse?.let { response ->
                    // Error dado por laravel
                    Log.d("ApiModel", "Error: ${response.statusCode}")
                    callback(false)
                } ?: run {
                    // Si no hay respuesta, se trata de un error de conexión
                    Log.d("ApiModel", "Error: ${error.message.orEmpty()}")
                    callback(false)
                }
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                if (token != null) headers["Authorization"] = "Bearer $token"
                return headers
            }

            override fun getBody(): ByteArray {
                return body.toString().toByteArray(Charsets.UTF_8)
            }

        }.apply {
            setShouldCache(cache)
        }

        // Añadir la solicitud a la cola
        requestQueue.add(request)
    }
}