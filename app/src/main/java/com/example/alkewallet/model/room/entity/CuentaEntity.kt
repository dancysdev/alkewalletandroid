package com.example.alkewallet.model.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cuentas",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["usuarioId"], unique = true)
    ]
)
data class CuentaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val remoteId: Int?,

    val usuarioId: Int,

    val numero: String,

    val saldo: Double
)