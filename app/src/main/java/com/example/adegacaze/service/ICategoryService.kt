package com.example.adegacaze.service

import com.example.adegacaze.model.Category
import retrofit2.Call
import retrofit2.http.GET

interface ICategoryService {
    @GET("api/category")
    fun listar(): Call<List<Category>>
}