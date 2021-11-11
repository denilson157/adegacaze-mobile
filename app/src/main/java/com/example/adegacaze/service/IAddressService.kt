package com.example.adegacaze.service

import com.example.adegacaze.model.Address
import com.example.adegacaze.model.Login
import com.example.adegacaze.model.RespAddress
import com.example.adegacaze.model.UsuarioLogin
import retrofit2.Call
import retrofit2.http.*

interface IAddressService {

    @GET("api/address")
    fun listar(): Call<List<Address>>

    @GET("api/address/{id}")
    fun pesquisarPorId(@Path("id") id: Int): Call<Address>

    @POST("api/address")
    fun criarEndereco(@Body endereco: Address): Call<RespAddress>

    @PUT("api/address/{id}")
    fun salvarEndereco(@Path("id") id: Int, @Body endereco: Address): Call<RespAddress>

}