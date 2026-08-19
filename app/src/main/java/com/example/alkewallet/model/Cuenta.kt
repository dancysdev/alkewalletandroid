package com.example.alkewallet.model

data class Cuenta(
    val numero:String,
    var saldo:Double,
    val movimientos:MutableList<Movimiento>
)