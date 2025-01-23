package com.interdata.laflamita.controller

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

open class Controller(ctx: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences = ctx.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    protected fun getToken(): String? {
        return sharedPreferences.getString("cliente_token", "") // Devuelve null si no existe
    }

    protected fun setAccessToken(token: String) {
        sharedPreferences.edit().putString("cliente_token", token).apply()
    }

    protected fun removeAccessToken() {
        sharedPreferences.edit().remove("cliente_token").apply()
    }

}