package com.example.adegacaze.service

import com.example.adegacaze.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IProductService {

    @GET("api/product")
    fun listar(): Call<List<Product>>

    @GET("api/product/{id}")
    fun pesquisarPorId(@Path("id")id: Int): Call<Product>

}