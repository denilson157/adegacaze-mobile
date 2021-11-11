package com.example.adegacaze.service

import com.example.adegacaze.model.*
import retrofit2.Call
import retrofit2.http.*

interface IUserService {
    @POST("api/user")
    fun registrar(@Body login: UsuarioRegistro): Call<UsuarioLogin>

    @GET("api/user/{id}")
    fun pesquisarPorId(@Path("id") id: Int): Call<User>

    @PUT("api/user/{id}")
    fun salvarUsuario(@Path("id") id: Int, @Body usuario: User): Call<RespUser>
}