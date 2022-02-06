package ru.michaelismur.androiddevtest.web

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiProvider {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://developerslife.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}