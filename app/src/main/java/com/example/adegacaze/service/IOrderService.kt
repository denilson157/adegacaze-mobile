package com.example.adegacaze.service

import com.example.adegacaze.model.Order
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IOrderService {
    @GET("api/order")
    fun listar(): Call<List<Order>>

    @GET("api/order/{id}")
    fun pesquisarPorId(@Path("id")id: Int): Call<Order>
}