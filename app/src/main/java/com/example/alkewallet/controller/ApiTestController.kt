package com.example.alkewallet.controller

import android.content.Context
import com.example.alkewallet.model.room.UserRepository

class ApiTestController(context: Context) {

    private val repository =
        UserRepository(context)

    suspend fun buscarUsuarioPorCorreo(
        correo: String
    ) = repository.buscarUsuarioRemotoPorCorreo(correo)

    suspend fun probarTransferencia(
        emisorAlke: String,
        destinatarioAlke: String,
        monto: Double
    ) = repository.probarTransferenciaRemota(
        emisorAlke,
        destinatarioAlke,
        monto
    )
}