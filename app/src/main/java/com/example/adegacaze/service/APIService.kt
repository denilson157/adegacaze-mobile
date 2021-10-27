package com.example.adegacaze.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getService(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://192.168.15.64:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}