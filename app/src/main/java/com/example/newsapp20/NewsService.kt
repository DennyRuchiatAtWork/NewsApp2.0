package com.example.newsapp20

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String = "us"
    ): Call<NewsResponse>

    @GET("v2/everything")
    fun getLatestNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}