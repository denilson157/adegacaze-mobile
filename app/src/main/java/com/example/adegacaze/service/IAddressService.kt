package com.example.adegacaze.service

import com.example.adegacaze.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IAddressService {

    @GET("api/address")
    fun listar(): Call<List<Address>>

    @GET("api/address/{id}")
    fun pesquisarPorId(@Path("id")id: Int): Call<Address>


}