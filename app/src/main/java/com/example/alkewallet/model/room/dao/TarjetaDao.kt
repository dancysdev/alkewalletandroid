package com.example.alkewallet.model.room.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.alkewallet.model.room.entity.TarjetaEntity

@Dao
interface TarjetaDao {

    @Insert
    suspend fun insertar(tarjeta: TarjetaEntity): Long

    @Query("SELECT * FROM tarjetas WHERE id = :id")
    suspend fun buscarPorId(id: Int): TarjetaEntity?

    @Query("SELECT * FROM tarjetas WHERE usuarioId = :usuarioId")
    suspend fun listarPorUsuario(usuarioId: Int): List<TarjetaEntity>

    @Query("SELECT * FROM tarjetas WHERE numero = :numero LIMIT 1")
    suspend fun buscarPorNumero(numero: String): TarjetaEntity?

    @Query("""
        SELECT * FROM tarjetas
        WHERE usuarioId = :usuarioId
        AND numero = :numero
        LIMIT 1
    """)
    suspend fun buscarPorUsuarioYNumero(
        usuarioId: Int,
        numero: String
    ): TarjetaEntity?

    @Update
    suspend fun actualizar(tarjeta: TarjetaEntity)

    @Delete
    suspend fun eliminar(tarjeta: TarjetaEntity)
}

