package com.example.alkewallet.controller

import com.example.alkewallet.model.Usuario

class LoginController {

    private val userController = UserController()

    fun login(correo: String, password: String): Usuario? {

        val usuario = userController.buscarUsuario(correo)

        return if (usuario != null && usuario.password == password) {
            usuario
        } else {
            null
        }
    }
}