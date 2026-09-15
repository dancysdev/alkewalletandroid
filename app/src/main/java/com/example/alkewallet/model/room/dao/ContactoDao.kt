package com.example.alkewallet.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.alkewallet.model.room.entity.ContactoEntity

@Dao
interface ContactoDao {

    @Insert
    suspend fun insertar(
        contacto: ContactoEntity
    ): Long

    @Query("""
        SELECT * FROM contactos
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun buscarPorId(
        id: Int
    ): ContactoEntity?

    @Query("""
        SELECT * FROM contactos
        WHERE usuarioId = :usuarioId
    """)
    suspend fun listarPorUsuario(
        usuarioId: Int
    ): List<ContactoEntity>

    @Query("""
        SELECT * FROM contactos
        WHERE usuarioId = :usuarioId
        AND alkeNumero = :alkeNumero
        LIMIT 1
    """)
    suspend fun buscarPorAlkeNumero(
        usuarioId: Int,
        alkeNumero: String
    ): ContactoEntity?

    @Delete
    suspend fun eliminar(
        contacto: ContactoEntity
    )
}

