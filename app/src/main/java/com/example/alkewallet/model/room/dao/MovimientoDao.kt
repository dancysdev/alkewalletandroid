package com.example.alkewallet.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.alkewallet.model.room.entity.MovimientoEntity

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: MovimientoEntity): Long

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun buscarPorId(id: Int): MovimientoEntity?

    @Query("SELECT * FROM movimientos WHERE cuentaId = :cuentaId ORDER BY id DESC")
    suspend fun listarPorCuenta(cuentaId: Int): List<MovimientoEntity>

    @Update
    suspend fun actualizar(movimiento: MovimientoEntity)

    @Delete
    suspend fun eliminar(movimiento: MovimientoEntity)
}