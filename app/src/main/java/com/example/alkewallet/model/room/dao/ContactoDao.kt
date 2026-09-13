package com.example.alkewallet.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.alkewallet.model.room.entity.ContactoEntity

@Dao
interface ContactoDao {

    @Insert
    suspend fun insertar(contacto: ContactoEntity): Long

    @Query("SELECT * FROM contactos WHERE id = :id")
    suspend fun buscarPorId(id: Int): ContactoEntity?

    @Query("SELECT * FROM contactos WHERE usuarioId = :usuarioId")
    suspend fun listarPorUsuario(usuarioId: Int): List<ContactoEntity>

    @Query("""
        SELECT * FROM contactos
        WHERE usuarioId = :usuarioId
        AND contactoRemoteId = :contactoRemoteId
        LIMIT 1
    """)
    suspend fun buscarContacto(
        usuarioId: Int,
        contactoRemoteId: Int
    ): ContactoEntity?

    @Delete
    suspend fun eliminar(contacto: ContactoEntity)
}