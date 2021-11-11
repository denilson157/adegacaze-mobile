package com.example.adegacaze.service

import android.content.Context
import com.example.adegacaze.getTokenUser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class API(val context: Context) {
    private val baseUrl = "http://192.168.15.64:8080/";

    private var tokenAuthorization: String? = null;

    private val retrofit: Retrofit
        get() {
            val token = getTokenUser(context);

            val client = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer $token"
                        )
                        .build()
                    chain.proceed(newRequest)
                }.build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    val produto: IProductService
        get() {
            return retrofit.create(IProductService::class.java)
        }
    val pedido: IOrderService
        get() {
            return retrofit.create(IOrderService::class.java)
        }
    val endereco: IAddressService
        get() {
            return retrofit.create(IAddressService::class.java)
        }
    val login: ILoginService
        get() {
            return retrofit.create(ILoginService::class.java)
        }
    val user: IUserService
        get() {
            return retrofit.create(IUserService::class.java)
        }
}

