package com.example.alkewallet.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.alkewallet.model.room.entity.CuentaEntity

@Dao
interface CuentaDao {

    @Insert
    suspend fun insertar(cuenta: CuentaEntity): Long

    @Query("SELECT * FROM cuentas WHERE id = :id")
    suspend fun buscarPorId(id: Int): CuentaEntity?

    @Query("SELECT * FROM cuentas WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarPorUsuarioId(usuarioId: Int): CuentaEntity?

    @Update
    suspend fun actualizar(cuenta: CuentaEntity)

    @Delete
    suspend fun eliminar(cuenta: CuentaEntity)
}