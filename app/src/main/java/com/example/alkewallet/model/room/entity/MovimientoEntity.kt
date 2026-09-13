package com.example.alkewallet.model.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movimientos",
    foreignKeys = [
        ForeignKey(
            entity = CuentaEntity::class,
            parentColumns = ["id"],
            childColumns = ["cuentaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["cuentaId"]),
        Index(value = ["senderRemoteId"]),
        Index(value = ["receiverRemoteId"])
    ]
)
data class MovimientoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val remoteId: Int?,

    val cuentaId: Int,

    val senderRemoteId: Int?,

    val receiverRemoteId: Int?,

    val tipo: String,

    val monto: Double,

    val fecha: String,

    val descripcion: String
)