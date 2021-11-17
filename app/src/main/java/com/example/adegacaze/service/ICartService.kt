package com.example.adegacaze.service

import com.example.adegacaze.model.AddCart
import com.example.adegacaze.model.AddCartResp
import com.example.adegacaze.model.Cart
import com.example.adegacaze.model.RemoveCart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ICartService {

    @POST("api/cart/add")
    fun addCart(@Body objAdd: AddCart): Call<AddCartResp>

    @POST("api/cart/remove")
    fun removeCart(@Body objRemove: RemoveCart): Call<AddCartResp>

    @POST("api/cart/remove/product")
    fun removeProductCart(@Body objRemove: RemoveCart): Call<AddCartResp>

    @GET("api/cart")
    fun getCart(): Call<List<Cart>>

}