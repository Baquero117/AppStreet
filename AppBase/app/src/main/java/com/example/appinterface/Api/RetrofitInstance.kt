package com.example.appinterface.Api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://dog.ceo/api/"
    private const val BASE_URL_APIKOTLIN = "http://10.0.2.2:8080/"

    // 🔹 Dog API (sin JWT)
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // 🔐 Backend con JWT
    fun api2kotlin(context: Context): ApiServicesKotlin {

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicesKotlin::class.java)
    }
}
