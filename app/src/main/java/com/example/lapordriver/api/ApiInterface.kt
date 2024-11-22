package com.example.lapordriver.api

import com.example.lapordriver.api.responses.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("add_laporan") // Replace with your endpoint
    fun addLaporanV1(
        @Part("vehicleId") vehicleId: RequestBody,
        @Part("note") note: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<DefaultResponse> // Replace DefaultResponse with your response model
}
