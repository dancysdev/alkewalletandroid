package com.example.alkewallet.model.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tarjetas",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["usuarioId"])
    ]
)
data class TarjetaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val remoteId: Int?,

    val usuarioId: Int,

    val nombre: String,

    val numero: String,

    val saldo: Double
)