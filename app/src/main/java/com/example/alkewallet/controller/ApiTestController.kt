package com.example.alkewallet.controller

import android.content.Context
import com.example.alkewallet.model.room.UserRepository

class ApiTestController(context: Context) {

    private val repository =
        UserRepository(context)

    suspend fun buscarUsuarioPorCorreo(
        correo: String
    ) = repository.buscarUsuarioRemotoPorCorreo(correo)
}