package com.example.newsapp20

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.await

class NewsViewModel : ViewModel() {
    private val _newsState = mutableStateOf<NewsResponse?>(null)
    val newsState: State<NewsResponse?> = _newsState

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTopHeadlines(apiKey = "0b4fcd0098a64138a47a5100d4936072").await()
                _newsState.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
