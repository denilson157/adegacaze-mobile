package com.example.adegacaze.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request


fun getService(): Retrofit {

    val client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 2|iPYh0Z43LDmlz5ImCY17PKxTnwpbrwVQREFJ68GX")
            .build()
        chain.proceed(newRequest)
    }.build()

    return Retrofit.Builder()
        .client(client)
        .baseUrl("http://192.168.15.64:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
