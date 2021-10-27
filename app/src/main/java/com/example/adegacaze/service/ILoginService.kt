package com.example.adegacaze.service

import com.example.adegacaze.model.Login
import com.example.adegacaze.model.UsuarioLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ILoginService {

    @POST("api/login")
    fun login(@Body login: Login): Call<UsuarioLogin>
}