package com.example.alkewallet.controller

import android.content.Context
import com.example.alkewallet.model.Cuenta
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.model.room.UserRepository

class LoginController(context: Context) {

    private val repository =
        UserRepository(context)

    suspend fun login(
        correo: String,
        password: String
    ): Usuario? {

        val usuarioRemoto =
            repository.buscarUsuarioRemotoPorCorreo(correo)
                ?: return null

        if (usuarioRemoto.password != password) {
            return null
        }

        val usuarioLocal =
            repository.buscarPorCorreo(correo)
                ?: return null

        return usuarioLocal.copy(
            nombre = usuarioRemoto.nombre,
            apellido = usuarioRemoto.apellido,
            correo = usuarioRemoto.correo,
            password = usuarioRemoto.password,
            alkeNumero = usuarioRemoto.alkeNumero
        )
    }
}