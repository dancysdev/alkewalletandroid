package com.example.alkewallet.api

import com.example.alkewallet.api.dto.UsuarioDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun obtenerUsuarios(): List<UsuarioDto>

    @GET("users")
    suspend fun buscarUsuarioPorCorreo(
        @Query("correo") correo: String
    ): List<UsuarioDto>
}