package com.example.alkewallet.api.dto

data class UsuarioDto(
    val id: String,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val password: String,
    val imagenPerfil: String?,
    val alkeNumero: String,
    val cuentaNumero: String,
    val saldo: Double
)