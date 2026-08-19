package com.example.alkewallet.utils


import com.example.alkewallet.model.Usuario

object SessionManager {

    var usuarioActual: Usuario? = null

    fun iniciarSesion(usuario: Usuario) {
        usuarioActual = usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}
