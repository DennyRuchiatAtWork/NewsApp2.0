package com.example.newsapp20

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.await

class NewsViewModel : ViewModel() {
    private val _headlineNewsState = mutableStateOf<NewsResponse?>(null)
    val headlineNewsState: State<NewsResponse?> = _headlineNewsState

    private val _latestNewsState = mutableStateOf<NewsResponse?>(null)
    val latestNewsState: State<NewsResponse?> = _latestNewsState

    init {
        fetchHeadlineNews()
        fetchLatestNews()
    }

    private fun fetchHeadlineNews() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTopHeadlines(apiKey = "0b4fcd0098a64138a47a5100d4936072").await()
                _headlineNewsState.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchLatestNews() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLatestNews(
                    query = "bitcoin",
                    apiKey = "0b4fcd0098a64138a47a5100d4936072"
                ).await()
                _latestNewsState.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
