package ru.michaelismur.androiddevtest.web

import retrofit2.http.GET
import ru.michaelismur.androiddevtest.model.PostInfo

interface ApiService {
    @GET("random?json=true")
    suspend fun getRandomPost(): PostInfo
}