package com.interdata.laflamita.model

import android.content.Context
import com.android.volley.Request
import org.json.JSONObject

class ProductoModel(ctx: Context) : ApiModel(ctx) {
    fun getCategorias(categoria: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/categoria/$categoria"
        request(Request.Method.GET, url, null, null, callback, true)
    }

    fun getProductos(categoriaId: String, callback: (Boolean, JSONObject) -> Unit) {
        val url = "$serverURL/api/producto/categoria/$categoriaId"
        request(Request.Method.GET, url, null, null, callback, true)
    }
}