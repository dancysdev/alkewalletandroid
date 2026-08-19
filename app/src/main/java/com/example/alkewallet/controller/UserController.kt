package com.example.alkewallet.controller

import com.example.alkewallet.model.FakeDatabase
import com.example.alkewallet.model.Movimiento
import com.example.alkewallet.model.Tarjeta
import com.example.alkewallet.model.Usuario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class UserController {

    fun crearUsuario(usuario: Usuario) {

        // Generar ALKE aleatorio y único
        var alkeNumero: String

        do {
            alkeNumero = "ALKE${(100..999).random()}"
        } while (
            FakeDatabase.usuarios.any {
                it.alkeNumero.equals(alkeNumero, ignoreCase = true)
            }
        )

        usuario.alkeNumero = alkeNumero

        FakeDatabase.usuarios.add(usuario)
    }

    fun buscarUsuario(correo: String): Usuario? {
        return FakeDatabase.usuarios.find { it.correo == correo }
    }

    fun editarPerfil(usuarioActualizado: Usuario): Boolean {

        val indice = FakeDatabase.usuarios.indexOfFirst {
            it.id == usuarioActualizado.id
        }

        if (indice != -1) {
            FakeDatabase.usuarios[indice] = usuarioActualizado
            return true
        }

        return false
    }

    fun eliminarUsuario(id: Int): Boolean {
        return FakeDatabase.usuarios.removeIf { it.id == id }
    }


    // =====================================================
    // INGRESAR DINERO DESDE TARJETA
    // =====================================================

    fun ingresarDinero(
        usuario: Usuario,
        monto: Double,
        tarjeta: Tarjeta,
        nota: String?
    ): Boolean {

        if (monto <= 0) {
            return false
        }

        usuario.cuenta.saldo += monto

        val formatoFecha = SimpleDateFormat(
            "dd MMM, HH:mm",
            Locale("es", "CL")
        )

        formatoFecha.timeZone =
            TimeZone.getTimeZone("America/Santiago")

        val descripcionMovimiento =
            if (!nota.isNullOrBlank()) {
                nota
            } else {
                "${tarjeta.nombre} •••• ${tarjeta.numero.takeLast(4)}"
            }

        val movimiento = Movimiento(
            tipo = "ingreso",
            monto = monto,
            fecha = formatoFecha.format(Date()),
            descripcion = descripcionMovimiento,
            usuarioRelacionado = null
        )

        usuario.cuenta.movimientos.add(movimiento)

        return true
    }


    // =====================================================
    // TRANSFERIR DINERO
    // =====================================================

    fun transferirDinero(
        emisor: Usuario,
        destinatario: Usuario,
        monto: Double,
        nota: String?
    ): Boolean {

        if (monto <= 0) {
            return false
        }

        if (emisor.id == destinatario.id) {
            return false
        }

        if (monto > emisor.cuenta.saldo) {
            return false
        }

        val formatoFecha = SimpleDateFormat(
            "dd MMM, HH:mm",
            Locale("es", "CL")
        )

        formatoFecha.timeZone =
            TimeZone.getTimeZone("America/Santiago")

        val fechaActual = formatoFecha.format(Date())

        val descripcionMovimiento =
            if (!nota.isNullOrBlank()) {
                nota
            } else {
                "Transferencia"
            }

        emisor.cuenta.saldo -= monto
        destinatario.cuenta.saldo += monto

        val movimientoEnviado = Movimiento(
            tipo = "transferencia enviada",
            monto = monto,
            fecha = fechaActual,
            descripcion = descripcionMovimiento,
            usuarioRelacionado =
                "${destinatario.nombre} ${destinatario.apellido}"
        )

        val movimientoRecibido = Movimiento(
            tipo = "transferencia recibida",
            monto = monto,
            fecha = fechaActual,
            descripcion = descripcionMovimiento,
            usuarioRelacionado =
                "${emisor.nombre} ${emisor.apellido}"
        )

        emisor.cuenta.movimientos.add(
            movimientoEnviado
        )

        destinatario.cuenta.movimientos.add(
            movimientoRecibido
        )

        return true
    }
}