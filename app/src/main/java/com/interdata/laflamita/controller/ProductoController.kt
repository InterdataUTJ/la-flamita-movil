package com.interdata.laflamita.controller

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.interdata.laflamita.model.ProductoModel
import org.json.JSONObject


class ProductoController(ctx: Context) : Controller(ctx) {
    private val model = ProductoModel(ctx)
    private val categoria = "Tipo"

    fun getCategorias(callback: (Boolean, JSONObject) -> Unit) {
        model.getCategorias(this.categoria) { success, response ->
            callback(success, response)
        }
    }

    fun getProductos(categoriaId: String, callback: (Boolean, JSONObject) -> Unit) {
        model.getProductos(categoriaId) { success, response ->
            callback(success, response)
        }
    }
}