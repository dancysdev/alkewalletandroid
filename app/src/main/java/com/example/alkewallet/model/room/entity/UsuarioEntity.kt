package com.example.alkewallet.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val remoteId: Int?,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val password: String,
    val imagenPerfil: String?,
    val alkeNumero: String
)