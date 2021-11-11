package com.example.adegacaze.service

import com.example.adegacaze.model.Product
import com.example.adegacaze.model.ProductSearch
import retrofit2.Call
import retrofit2.http.*

interface IProductService {

    @GET("api/product")
    fun listar(): Call<List<Product>>

    @GET("api/product/{id}")
    fun pesquisarPorId(@Path("id") id: Int): Call<Product>

    @GET("api/product/category/{id}")
    fun pesquisarPorCategoria(@Path("id") id: Int): Call<List<Product>>

    @POST("api/product/search/category")
    fun pesquisarProdutoNomePorCategoria(@Body objPesquisa: ProductSearch): Call<List<Product>>
}