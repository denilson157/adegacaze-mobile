package com.example.adegacaze.service

import com.example.adegacaze.model.InsertOrder
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.RespInsertOrder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IOrderService {
    @GET("api/order")
    fun listar(): Call<List<Order>>

    @GET("api/order/{id}")
    fun pesquisarPorId(@Path("id")id: Int): Call<Order>

    @POST("api/order")
    fun inserir(@Body pedido: InsertOrder): Call<RespInsertOrder>
}