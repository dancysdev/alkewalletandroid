package com.example.alkewallet.model

data class Usuario(
    val id: Int,
    var nombre: String,
    var apellido: String,
    var correo: String,
    var password: String,
    var imagenPerfil: String?,
    var alkeNumero: String,
    var cuenta: Cuenta,
    val tarjetas: MutableList<Tarjeta> = mutableListOf()
)

