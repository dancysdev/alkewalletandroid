package com.example.alkewallet.model.room

import android.content.Context
import androidx.room.withTransaction
import com.example.alkewallet.api.RetrofitClient
import com.example.alkewallet.api.dto.TransferenciaDto
import com.example.alkewallet.api.dto.UsuarioDto
import com.example.alkewallet.model.Cuenta
import com.example.alkewallet.model.Movimiento
import com.example.alkewallet.model.Tarjeta
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.model.room.entity.ContactoEntity
import com.example.alkewallet.model.room.entity.CuentaEntity
import com.example.alkewallet.model.room.entity.MovimientoEntity
import com.example.alkewallet.model.room.entity.TarjetaEntity
import com.example.alkewallet.model.room.entity.UsuarioEntity


class UserRepository(context: Context) {

    private val database =
        DatabaseProvider.getDatabase(context)

    private val apiService =
        RetrofitClient.apiService

    private val usuarioDao =
        database.usuarioDao()

    private val cuentaDao =
        database.cuentaDao()

    private val tarjetaDao =
        database.tarjetaDao()

    private val movimientoDao =
        database.movimientoDao()

    private val contactoDao =
        database.contactoDao()


    // =========================================================
    // USUARIOS LOCALES
    // =========================================================

    suspend fun crearUsuario(
        usuario: Usuario
    ): Int {

        val usuarioEntity =
            UsuarioEntity(
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                correo = usuario.correo,
                password = usuario.password,
                imagenPerfil = usuario.imagenPerfil,
                alkeNumero = usuario.alkeNumero,
                remoteId = null
            )

        val usuarioId =
            usuarioDao.insertar(
                usuarioEntity
            ).toInt()

        val cuentaEntity =
            CuentaEntity(
                usuarioId = usuarioId,
                numero = usuario.cuenta.numero,
                saldo = usuario.cuenta.saldo,
                remoteId = null
            )

        cuentaDao.insertar(
            cuentaEntity
        )

        return usuarioId
    }


    suspend fun existeAlkeNumero(
        alkeNumero: String
    ): Boolean {

        return usuarioDao.buscarPorAlkeNumero(
            alkeNumero
        ) != null
    }


    suspend fun buscarPorCorreo(
        correo: String
    ): Usuario? {

        val usuarioEntity =
            usuarioDao.buscarPorCorreo(
                correo
            )
                ?: return null

        return construirUsuario(
            usuarioEntity
        )
    }


    suspend fun buscarPorAlkeNumero(
        alkeNumero: String
    ): Usuario? {

        val usuarioEntity =
            usuarioDao.buscarPorAlkeNumero(
                alkeNumero
            )
                ?: return null

        return construirUsuario(
            usuarioEntity
        )
    }


    // =========================================================
    // USUARIOS REMOTOS / API
    // =========================================================

    suspend fun obtenerUsuariosRemotos(): List<UsuarioDto> {

        return apiService.obtenerUsuarios()
    }


    suspend fun buscarUsuarioRemotoPorCorreo(
        correo: String
    ): UsuarioDto? {

        return apiService
            .buscarUsuarioPorCorreo(correo)
            .firstOrNull()
    }


    suspend fun buscarUsuarioRemotoPorAlke(
        alkeNumero: String
    ): UsuarioDto? {

        return apiService
            .obtenerUsuarios()
            .firstOrNull {
                it.alkeNumero == alkeNumero
            }
    }


    suspend fun crearUsuarioRemoto(
        usuario: UsuarioDto
    ): UsuarioDto {

        return apiService.crearUsuario(
            usuario
        )
    }
    suspend fun actualizarSaldoRemoto(
        usuarioIdRemoto: String,
        nuevoSaldo: Double
    ): UsuarioDto {

        val datos =
            mapOf(
                "saldo" to nuevoSaldo
            )

        return apiService.actualizarSaldoUsuario(
            id = usuarioIdRemoto,
            datos = datos
        )
    }


    // =========================================================
    // TRANSFERENCIAS REMOTAS / API
    // =========================================================

    suspend fun crearTransferenciaRemota(
        transferencia: TransferenciaDto
    ): TransferenciaDto {

        return apiService.crearTransferencia(
            transferencia
        )
    }


    suspend fun probarTransferenciaRemota(
        emisorAlke: String,
        destinatarioAlke: String,
        monto: Double
    ): TransferenciaDto? {

        val emisor =
            buscarUsuarioRemotoPorAlke(
                emisorAlke
            )
                ?: return null

        val destinatario =
            buscarUsuarioRemotoPorAlke(
                destinatarioAlke
            )
                ?: return null

        val transferencia =
            TransferenciaDto(
                senderId = emisor.id,
                receiverId = destinatario.id,
                senderAlkeNumero = emisor.alkeNumero,
                receiverAlkeNumero = destinatario.alkeNumero,
                amount = monto,
                type = "TRANSFER"
            )

        return crearTransferenciaRemota(
            transferencia
        )
    }


    // =========================================================
    // CONTACTOS LOCALES
    // =========================================================

    suspend fun listarContactos(
        usuarioId: Int
    ): List<ContactoEntity> {

        return contactoDao.listarPorUsuario(
            usuarioId
        )
    }


    suspend fun buscarContactoPorAlke(
        usuarioId: Int,
        alkeNumero: String
    ): ContactoEntity? {

        return contactoDao.buscarPorAlkeNumero(
            usuarioId = usuarioId,
            alkeNumero = alkeNumero
        )
    }


    suspend fun agregarContacto(
        usuarioId: Int,
        usuarioRemoto: UsuarioDto
    ): Boolean {

        val contactoExistente =
            buscarContactoPorAlke(
                usuarioId = usuarioId,
                alkeNumero = usuarioRemoto.alkeNumero
            )

        if (contactoExistente != null) {
            return false
        }

        val contacto =
            ContactoEntity(
                usuarioId = usuarioId,

                // El modelo Room actual usa Int,
                // mientras JSON Server usa String.
                // Mantenemos el esquema actual sin migración.
                contactoRemoteId =
                    usuarioRemoto.id.hashCode(),

                alkeNumero = usuarioRemoto.alkeNumero,
                nombre = usuarioRemoto.nombre,
                apellido = usuarioRemoto.apellido,
                imagenPerfil = usuarioRemoto.imagenPerfil
            )

        contactoDao.insertar(
            contacto
        )

        return true
    }


    suspend fun eliminarContacto(
        usuarioId: Int,
        alkeNumero: String
    ): Boolean {

        val contacto =
            buscarContactoPorAlke(
                usuarioId = usuarioId,
                alkeNumero = alkeNumero
            )
                ?: return false

        contactoDao.eliminar(
            contacto
        )

        return true
    }


    // =========================================================
    // CONSTRUCCIÓN DE USUARIO DESDE ROOM
    // =========================================================

    private suspend fun construirUsuario(
        usuarioEntity: UsuarioEntity
    ): Usuario {

        val cuentaEntity =
            cuentaDao.buscarPorUsuarioId(
                usuarioEntity.id
            )
                ?: return Usuario(
                    id = usuarioEntity.id,
                    nombre = usuarioEntity.nombre,
                    apellido = usuarioEntity.apellido,
                    correo = usuarioEntity.correo,
                    password = usuarioEntity.password,
                    imagenPerfil = usuarioEntity.imagenPerfil,
                    alkeNumero = usuarioEntity.alkeNumero,
                    cuenta = Cuenta(
                        numero = "",
                        saldo = 0.0,
                        movimientos = mutableListOf()
                    )
                )

        val tarjetasEntity =
            tarjetaDao.listarPorUsuario(
                usuarioEntity.id
            )

        val movimientosEntity =
            movimientoDao.listarPorCuenta(
                cuentaEntity.id
            )

        tarjetasEntity.forEach {

            android.util.Log.d(
                "ROOM_TARJETAS",
                "Tarjeta id=${it.id}, usuarioId=${it.usuarioId}, numero=${it.numero}"
            )
        }


        val movimientos =
            movimientosEntity.map {

                Movimiento(
                    tipo = it.tipo,
                    monto = it.monto,
                    fecha = it.fecha,
                    descripcion = it.descripcion,
                    usuarioRelacionado = null
                )

            }.toMutableList()


        val tarjetas =
            tarjetasEntity.map {

                Tarjeta(
                    nombre = it.nombre,
                    numero = it.numero,
                    saldo = it.saldo
                )

            }.toMutableList()


        return Usuario(
            id = usuarioEntity.id,
            nombre = usuarioEntity.nombre,
            apellido = usuarioEntity.apellido,
            correo = usuarioEntity.correo,
            password = usuarioEntity.password,
            imagenPerfil = usuarioEntity.imagenPerfil,
            alkeNumero = usuarioEntity.alkeNumero,

            cuenta = Cuenta(
                numero = cuentaEntity.numero,
                saldo = cuentaEntity.saldo,
                movimientos = movimientos
            ),

            tarjetas = tarjetas
        )
    }


    // =========================================================
    // PERFIL
    // =========================================================

    suspend fun actualizarUsuario(
        usuario: Usuario
    ): Boolean {

        val usuarioEntity =
            usuarioDao.buscarPorId(
                usuario.id
            )
                ?: return false

        val usuarioActualizado =
            usuarioEntity.copy(
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                correo = usuario.correo,
                password = usuario.password,
                imagenPerfil = usuario.imagenPerfil,
                alkeNumero = usuario.alkeNumero
            )

        usuarioDao.actualizar(
            usuarioActualizado
        )

        return true
    }


    suspend fun eliminarUsuario(
        id: Int
    ): Boolean {

        val usuarioEntity =
            usuarioDao.buscarPorId(
                id
            )
                ?: return false

        usuarioDao.eliminar(
            usuarioEntity
        )

        return true
    }


    // =========================================================
    // CUENTA
    // =========================================================

    suspend fun actualizarCuenta(
        usuarioId: Int,
        saldo: Double
    ): Boolean {

        val cuenta =
            cuentaDao.buscarPorUsuarioId(
                usuarioId
            )
                ?: return false

        cuentaDao.actualizar(
            cuenta.copy(
                saldo = saldo
            )
        )

        return true
    }


    // =========================================================
    // TARJETAS
    // =========================================================

    suspend fun agregarTarjeta(
        usuarioId: Int,
        tarjeta: Tarjeta
    ): Boolean {

        val tarjetaExistente =
            tarjetaDao.buscarPorUsuarioYNumero(
                usuarioId = usuarioId,
                numero = tarjeta.numero
            )

        if (tarjetaExistente != null) {
            return false
        }

        val tarjetaEntity =
            TarjetaEntity(
                remoteId = null,
                usuarioId = usuarioId,
                nombre = tarjeta.nombre,
                numero = tarjeta.numero,
                saldo = tarjeta.saldo
            )

        tarjetaDao.insertar(
            tarjetaEntity
        )

        return true
    }


    suspend fun eliminarTarjeta(
        usuarioId: Int,
        numero: String
    ): Boolean {

        val tarjeta =
            tarjetaDao.buscarPorUsuarioYNumero(
                usuarioId = usuarioId,
                numero = numero
            )
                ?: return false

        tarjetaDao.eliminar(
            tarjeta
        )

        return true
    }


    // =========================================================
    // INGRESAR DINERO
    // =========================================================

    suspend fun ingresarDinero(
        usuarioId: Int,
        tarjetaNumero: String,
        monto: Double,
        fecha: String,
        descripcion: String
    ): Boolean {

        return database.withTransaction {

            val cuenta =
                cuentaDao.buscarPorUsuarioId(
                    usuarioId
                )
                    ?: return@withTransaction false

            val tarjeta =
                tarjetaDao.buscarPorUsuarioYNumero(
                    usuarioId = usuarioId,
                    numero = tarjetaNumero
                )
                    ?: return@withTransaction false

            if (monto <= 0) {
                return@withTransaction false
            }

            if (monto > tarjeta.saldo) {
                return@withTransaction false
            }


            tarjetaDao.actualizar(
                tarjeta.copy(
                    saldo = tarjeta.saldo - monto
                )
            )


            cuentaDao.actualizar(
                cuenta.copy(
                    saldo = cuenta.saldo + monto
                )
            )


            val usuarioEntity =
                usuarioDao.buscarPorId(
                    usuarioId
                )


            val movimiento =
                MovimientoEntity(
                    remoteId = null,
                    cuentaId = cuenta.id,
                    senderRemoteId = null,
                    receiverRemoteId =
                        usuarioEntity?.remoteId,
                    tipo = "ingreso",
                    monto = monto,
                    fecha = fecha,
                    descripcion = descripcion
                )


            movimientoDao.insertar(
                movimiento
            )

            true
        }
    }


    // =========================================================
    // TRANSFERENCIA LOCAL
    // =========================================================

    suspend fun transferirDinero(
        emisorId: Int,
        destinatarioId: Int,
        monto: Double,
        fecha: String,
        descripcion: String
    ): Boolean {

        return database.withTransaction {

            val cuentaEmisor =
                cuentaDao.buscarPorUsuarioId(
                    emisorId
                )
                    ?: return@withTransaction false

            val cuentaDestinatario =
                cuentaDao.buscarPorUsuarioId(
                    destinatarioId
                )
                    ?: return@withTransaction false


            if (monto <= 0) {
                return@withTransaction false
            }

            if (emisorId == destinatarioId) {
                return@withTransaction false
            }

            if (monto > cuentaEmisor.saldo) {
                return@withTransaction false
            }


            val emisor =
                usuarioDao.buscarPorId(
                    emisorId
                )
                    ?: return@withTransaction false

            val destinatario =
                usuarioDao.buscarPorId(
                    destinatarioId
                )
                    ?: return@withTransaction false


            cuentaDao.actualizar(
                cuentaEmisor.copy(
                    saldo =
                        cuentaEmisor.saldo - monto
                )
            )


            cuentaDao.actualizar(
                cuentaDestinatario.copy(
                    saldo =
                        cuentaDestinatario.saldo + monto
                )
            )


            movimientoDao.insertar(
                MovimientoEntity(
                    remoteId = null,
                    cuentaId = cuentaEmisor.id,
                    senderRemoteId =
                        emisor.remoteId,
                    receiverRemoteId =
                        destinatario.remoteId,
                    tipo = "transferencia enviada",
                    monto = monto,
                    fecha = fecha,
                    descripcion = descripcion
                )
            )


            movimientoDao.insertar(
                MovimientoEntity(
                    remoteId = null,
                    cuentaId = cuentaDestinatario.id,
                    senderRemoteId =
                        emisor.remoteId,
                    receiverRemoteId =
                        destinatario.remoteId,
                    tipo = "transferencia recibida",
                    monto = monto,
                    fecha = fecha,
                    descripcion = descripcion
                )
            )


            true
        }
    }
}