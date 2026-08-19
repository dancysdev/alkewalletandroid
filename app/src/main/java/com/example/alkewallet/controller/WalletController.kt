package com.example.alkewallet.controller

import com.example.alkewallet.model.FakeDatabase
import com.example.alkewallet.model.Movimiento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WalletController {


    fun consultarSaldo(numeroCuenta:String): Double? {

        val usuario = FakeDatabase.usuarios.find {
            it.cuenta.numero == numeroCuenta
        }

        return usuario?.cuenta?.saldo
    }


    fun depositar(
        numeroCuenta:String,
        monto:Double,
        descripcion:String
    ):Boolean {

        val usuario = FakeDatabase.usuarios.find {
            it.cuenta.numero == numeroCuenta
        }


        if(usuario != null){

            usuario.cuenta.saldo += monto


            usuario.cuenta.movimientos.add(
                Movimiento(
                    tipo = "Ingreso",
                    monto = monto,
                    fecha = obtenerFecha(),
                    descripcion = descripcion,
                    usuarioRelacionado = null
                )
            )


            return true
        }


        return false
    }


    fun retirar(
        numeroCuenta:String,
        monto:Double,
        descripcion:String
    ):Boolean {

        val usuario = FakeDatabase.usuarios.find {
            it.cuenta.numero == numeroCuenta
        }


        if(usuario != null &&
            usuario.cuenta.saldo >= monto){


            usuario.cuenta.saldo -= monto


            usuario.cuenta.movimientos.add(
                Movimiento(
                    tipo = "Retiro",
                    monto = -monto,
                    fecha = obtenerFecha(),
                    descripcion = descripcion,
                    usuarioRelacionado = null
                )
            )


            return true
        }


        return false
    }


    private fun obtenerFecha():String{

        return SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

    }

}