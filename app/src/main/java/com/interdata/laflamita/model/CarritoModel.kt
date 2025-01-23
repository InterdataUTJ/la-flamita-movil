package com.interdata.laflamita.model

import android.content.Context
import com.android.volley.Request
import org.json.JSONObject

class CarritoModel(ctx: Context) : ApiModel(ctx) {
    fun getItems(token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/carrito"
        request(Request.Method.GET, url, token, null, callback)
    }

    fun addItem(productoId: String, token: String, callback: (Boolean) -> Unit) {
        val url = "$serverURL/api/carrito/add"
        val body = JSONObject()
        body.put("id", productoId)
        emptyRequest(Request.Method.POST, url, token, body, callback)
    }

    fun removeItem(itemId: String, token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/carrito/eliminar"
        val body = JSONObject()
        body.put("id", itemId)
        request(Request.Method.POST, url, token, body, callback)
    }

    fun procesarPedido(token: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/venta/crear"
        request(Request.Method.POST, url, token, null, callback)
    }
}