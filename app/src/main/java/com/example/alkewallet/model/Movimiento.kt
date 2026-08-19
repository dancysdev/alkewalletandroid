package com.example.alkewallet.model

data class Movimiento(

    val tipo:String,

    val monto:Double,

    val fecha:String,

    val descripcion:String,

    val usuarioRelacionado:String?

)