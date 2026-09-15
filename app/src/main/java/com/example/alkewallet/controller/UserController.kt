package com.example.alkewallet.controller

import android.content.Context
import com.example.alkewallet.api.dto.UsuarioDto
import com.example.alkewallet.model.Movimiento
import com.example.alkewallet.model.Tarjeta
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.model.room.UserRepository
import com.example.alkewallet.model.room.entity.ContactoEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class UserController(context: Context) {

    private val userRepository =
        UserRepository(context)

    // =====================================================
    // CREAR USUARIO
    // =====================================================

    suspend fun crearUsuario(usuario: Usuario): Int {

        var alkeNumero: String

        do {
            alkeNumero = "ALKE${(100..999).random()}"
        } while (
            userRepository.existeAlkeNumero(alkeNumero)
        )

        usuario.alkeNumero = alkeNumero

        val usuarioId =
            userRepository.crearUsuario(usuario)

        val usuarioDto =
            UsuarioDto(
                id = "",
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                correo = usuario.correo,
                password = usuario.password,
                imagenPerfil = usuario.imagenPerfil,
                alkeNumero = usuario.alkeNumero,
                cuentaNumero = usuario.cuenta.numero,
                saldo = usuario.cuenta.saldo
            )

        userRepository.crearUsuarioRemoto(
            usuarioDto
        )

        return usuarioId
    }


    // =====================================================
    // BUSCAR USUARIO POR CORREO
    // =====================================================

    suspend fun buscarUsuario(correo: String): Usuario? {

        return userRepository.buscarPorCorreo(
            correo
        )
    }


    // =====================================================
    // BUSCAR USUARIO POR ALKE
    // =====================================================

    suspend fun buscarUsuarioPorAlke(
        alkeNumero: String
    ): Usuario? {

        return userRepository.buscarPorAlkeNumero(
            alkeNumero
        )
    }


    // =====================================================
    // CONTACTOS
    // =====================================================

    suspend fun buscarUsuarioRemotoPorAlke(
        alkeNumero: String
    ): UsuarioDto? {

        return userRepository.buscarUsuarioRemotoPorAlke(
            alkeNumero
        )
    }


    suspend fun listarContactos(
        usuarioId: Int
    ): List<ContactoEntity> {

        return userRepository.listarContactos(
            usuarioId
        )
    }


    suspend fun agregarContacto(
        usuarioId: Int,
        alkeNumero: String
    ): Boolean {

        val usuarioRemoto =
            userRepository.buscarUsuarioRemotoPorAlke(
                alkeNumero
            )
                ?: return false

        return userRepository.agregarContacto(
            usuarioId = usuarioId,
            usuarioRemoto = usuarioRemoto
        )
    }


    suspend fun eliminarContacto(
        usuarioId: Int,
        alkeNumero: String
    ): Boolean {

        return userRepository.eliminarContacto(
            usuarioId = usuarioId,
            alkeNumero = alkeNumero
        )
    }


    // =====================================================
    // EDITAR PERFIL
    // =====================================================

    suspend fun editarPerfil(
        usuarioActualizado: Usuario
    ): Boolean {

        return userRepository.actualizarUsuario(
            usuarioActualizado
        )
    }


    // =====================================================
    // ELIMINAR USUARIO
    // =====================================================

    suspend fun eliminarUsuario(
        id: Int
    ): Boolean {

        return userRepository.eliminarUsuario(
            id
        )
    }


    // =====================================================
    // AGREGAR TARJETA
    // =====================================================

    suspend fun agregarTarjeta(
        usuario: Usuario,
        tarjeta: Tarjeta
    ): Boolean {

        return userRepository.agregarTarjeta(
            usuarioId = usuario.id,
            tarjeta = tarjeta
        )
    }


    // =====================================================
    // ELIMINAR TARJETA
    // =====================================================

    suspend fun eliminarTarjeta(
        usuario: Usuario,
        tarjeta: Tarjeta
    ): Boolean {

        return userRepository.eliminarTarjeta(
            usuarioId = usuario.id,
            numero = tarjeta.numero
        )
    }


    // =====================================================
    // INGRESAR DINERO DESDE TARJETA
    // =====================================================

    suspend fun ingresarDinero(
        usuario: Usuario,
        monto: Double,
        tarjeta: Tarjeta,
        nota: String?
    ): Boolean {

        if (monto <= 0) {
            return false
        }

        val descripcionMovimiento =
            if (!nota.isNullOrBlank()) {
                nota
            } else {
                "${tarjeta.nombre} •••• ${tarjeta.numero.takeLast(4)}"
            }

        val formatoFecha = SimpleDateFormat(
            "dd MMM, HH:mm",
            Locale("es", "CL")
        )

        formatoFecha.timeZone =
            TimeZone.getTimeZone("America/Santiago")

        val fechaActual =
            formatoFecha.format(Date())

        // =========================================================
        // ACTUALIZAR SALDO REMOTO
        // =========================================================

        val usuarioRemoto =
            try {
                userRepository.buscarUsuarioRemotoPorAlke(
                    usuario.alkeNumero
                )
            } catch (e: Exception) {
                return false
            }

        if (usuarioRemoto == null) {
            return false
        }

        val nuevoSaldoRemoto =
            usuarioRemoto.saldo + monto

        try {
            userRepository.actualizarSaldoRemoto(
                usuarioIdRemoto = usuarioRemoto.id,
                nuevoSaldo = nuevoSaldoRemoto
            )
        } catch (e: Exception) {
            return false
        }

        // =========================================================
        // ACTUALIZAR PERSISTENCIA LOCAL
        // =========================================================

        val ingresoRealizado =
            userRepository.ingresarDinero(
                usuarioId = usuario.id,
                tarjetaNumero = tarjeta.numero,
                monto = monto,
                fecha = fechaActual,
                descripcion = descripcionMovimiento
            )

        if (!ingresoRealizado) {
            return false
        }

        // =========================================================
        // ACTUALIZAR OBJETO EN MEMORIA
        // =========================================================

        usuario.cuenta.saldo += monto
        tarjeta.saldo -= monto

        usuario.cuenta.movimientos.add(
            Movimiento(
                tipo = "ingreso",
                monto = monto,
                fecha = fechaActual,
                descripcion = descripcionMovimiento,
                usuarioRelacionado = null
            )
        )

        return true
    }


    // =====================================================
    // TRANSFERIR DINERO
    // =====================================================

    suspend fun transferirDinero(
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

        val descripcionMovimiento =
            if (!nota.isNullOrBlank()) {
                nota
            } else {
                "Transferencia"
            }

        val formatoFecha = SimpleDateFormat(
            "dd MMM, HH:mm",
            Locale("es", "CL")
        )

        formatoFecha.timeZone =
            TimeZone.getTimeZone("America/Santiago")

        val fechaActual =
            formatoFecha.format(Date())

        // =========================================================
        // BUSCAR USUARIOS EN EL BACKEND
        // =========================================================

        val emisorRemoto =
            try {
                userRepository.buscarUsuarioRemotoPorAlke(
                    emisor.alkeNumero
                )
            } catch (e: Exception) {
                return false
            }

        val destinatarioRemoto =
            try {
                userRepository.buscarUsuarioRemotoPorAlke(
                    destinatario.alkeNumero
                )
            } catch (e: Exception) {
                return false
            }

        if (emisorRemoto == null || destinatarioRemoto == null) {
            return false
        }

        // =========================================================
        // VALIDAR SALDO REMOTO
        // =========================================================

        if (monto > emisorRemoto.saldo) {
            return false
        }

        val nuevoSaldoEmisor =
            emisorRemoto.saldo - monto

        val nuevoSaldoDestinatario =
            destinatarioRemoto.saldo + monto

        // =========================================================
        // ACTUALIZAR SALDO DEL EMISOR EN BACKEND
        // =========================================================

        try {
            userRepository.actualizarSaldoRemoto(
                usuarioIdRemoto = emisorRemoto.id,
                nuevoSaldo = nuevoSaldoEmisor
            )
        } catch (e: Exception) {
            return false
        }

        // =========================================================
        // ACTUALIZAR SALDO DEL DESTINATARIO EN BACKEND
        // =========================================================

        try {
            userRepository.actualizarSaldoRemoto(
                usuarioIdRemoto = destinatarioRemoto.id,
                nuevoSaldo = nuevoSaldoDestinatario
            )
        } catch (e: Exception) {
            return false
        }

        // =========================================================
        // REGISTRAR TRANSFERENCIA EN BACKEND
        // =========================================================

        val transferenciaRemota =
            try {
                userRepository.probarTransferenciaRemota(
                    emisorAlke = emisor.alkeNumero,
                    destinatarioAlke = destinatario.alkeNumero,
                    monto = monto
                )
            } catch (e: Exception) {
                return false
            }

        if (transferenciaRemota == null) {
            return false
        }

        // =========================================================
        // ACTUALIZAR ROOM
        // =========================================================

        val transferenciaRealizada =
            userRepository.transferirDinero(
                emisorId = emisor.id,
                destinatarioId = destinatario.id,
                monto = monto,
                fecha = fechaActual,
                descripcion = descripcionMovimiento
            )

        if (!transferenciaRealizada) {
            return false
        }

        // =========================================================
        // ACTUALIZAR OBJETOS EN MEMORIA
        // =========================================================

        emisor.cuenta.saldo -= monto

        emisor.cuenta.movimientos.add(
            Movimiento(
                tipo = "transferencia enviada",
                monto = monto,
                fecha = fechaActual,
                descripcion = descripcionMovimiento,
                usuarioRelacionado =
                    "${destinatario.nombre} ${destinatario.apellido}"
            )
        )

        destinatario.cuenta.saldo += monto

        destinatario.cuenta.movimientos.add(
            Movimiento(
                tipo = "transferencia recibida",
                monto = monto,
                fecha = fechaActual,
                descripcion = descripcionMovimiento,
                usuarioRelacionado =
                    "${emisor.nombre} ${emisor.apellido}"
            )
        )

        return true
    }
}