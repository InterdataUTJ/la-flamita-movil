package com.interdata.laflamita.controller

import android.content.Context
import android.util.Log
import com.interdata.laflamita.model.CarritoModel
import org.json.JSONObject

class CarritoController(ctx: Context) : Controller(ctx) {
    private val model = CarritoModel(ctx)

    fun getItems(callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.getItems(it) { success, response ->
                callback(success, response)
            }
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión no está iniciada.\"}"))
        }
    }

    fun addItem(productoId: String, callback: (Boolean) -> Unit) {
        getToken()?.let {
            model.addItem(productoId, it) { success ->
                callback(success)
            }
        } ?: run {
            callback(false)
        }
    }

    fun removeItem(itemId: String, callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.removeItem(itemId, it) { success, response ->
                callback(success, response)
            }
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión no está iniciada.\"}"))
        }
    }

    fun procesarPedido(callback: (Boolean, JSONObject) -> Unit) {
        getToken()?.let {
            model.procesarPedido(it) { success, response ->
                callback(success, response)
            }
        } ?: run {
            callback(false, JSONObject("{\"message\":\"La sesión ha expirado.\"}"))
        }
    }
}