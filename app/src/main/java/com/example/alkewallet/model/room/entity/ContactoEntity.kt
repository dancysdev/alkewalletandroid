package com.example.alkewallet.model.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contactos",
    indices = [
        Index(
            value = ["usuarioId", "contactoRemoteId"],
            unique = true
        )
    ]
)
data class ContactoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuarioId: Int,

    val contactoRemoteId: Int,

    val alkeNumero: String,

    val nombre: String,

    val apellido: String,

    val imagenPerfil: String?
)