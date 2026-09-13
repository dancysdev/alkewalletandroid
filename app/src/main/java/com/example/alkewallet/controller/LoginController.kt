package com.example.alkewallet.controller

import android.content.Context
import com.example.alkewallet.model.Usuario

class LoginController(context: Context) {

    private val userController =
        UserController(context)

    suspend fun login(
        correo: String,
        password: String
    ): Usuario? {

        val usuario =
            userController.buscarUsuario(correo)

        return if (
            usuario != null &&
            usuario.password == password
        ) {
            usuario
        } else {
            null
        }
    }
}