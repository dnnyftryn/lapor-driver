package com.example.lapordriver.utils

import com.example.lapordriver.api.RetrofitClient
import com.example.lapordriver.api.repository.MainRepository

object Injection {
    fun provideRepository() : MainRepository {
        val apiService = RetrofitClient.instance
        return MainRepository(apiService)
    }
}