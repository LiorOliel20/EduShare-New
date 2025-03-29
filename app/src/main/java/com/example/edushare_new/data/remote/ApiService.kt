package com.example.edushare_new.data.remote

import com.example.edushare_new.data.remote.model.ZenQuote
import retrofit2.http.GET

interface ApiService {
    @GET("random")
    suspend fun getRandomQuote(): List<ZenQuote>
}