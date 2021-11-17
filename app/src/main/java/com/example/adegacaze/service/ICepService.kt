package com.example.adegacaze.service


import com.example.adegacaze.model.CEP
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ICepService {

    @GET("ws/{cep}/json/")
    fun buscarCep(@Path("cep") cep: String): Call<CEP>

}

