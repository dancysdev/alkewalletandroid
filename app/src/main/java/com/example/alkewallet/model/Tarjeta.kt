package com.example.alkewallet.model

data class Tarjeta(
    val nombre: String,
    val numero: String,
    var saldo: Double = 100000.0
)