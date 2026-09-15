package com.example.alkewallet.api

import com.example.alkewallet.api.dto.UsuarioDto
import com.example.alkewallet.api.dto.TransferenciaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun obtenerUsuarios(): List<UsuarioDto>

    @GET("users")
    suspend fun buscarUsuarioPorCorreo(
        @Query("correo") correo: String
    ): List<UsuarioDto>

    @POST("users")
    suspend fun crearUsuario(
        @Body usuario: UsuarioDto
    ): UsuarioDto

    @POST("transactions")
    suspend fun crearTransferencia(
        @Body transferencia: TransferenciaDto
    ): TransferenciaDto

    @PATCH("users/{id}")
    suspend fun actualizarSaldoUsuario(
        @Path("id") id: String,
        @Body datos: Map<String, Double>
    ): UsuarioDto
}