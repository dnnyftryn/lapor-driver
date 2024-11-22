package com.example.lapordriver.api

import android.telecom.Call
import com.example.lapordriver.api.responses.DefaultResponse
import com.example.lapordriver.api.responses.ListAllVehicleResponse
import com.example.lapordriver.api.responses.ListAllVehicleResponseItem
import com.example.lapordriver.api.responses.ListVehicleResponse
import com.example.lapordriver.api.responses.ListVehicleResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RequestAPI {
    @GET("list_vehicle")
    suspend fun listVehicle() : List<ListVehicleResponseItem?>?

    @GET("read_all_laporan")
    suspend fun listAllVehicle(
        @Query("userId") id : String
    ) : List<ListAllVehicleResponseItem?>?

    @Multipart
    @POST("add_laporan")
    suspend fun addLaporan(
        @Part("vehicleId") vehicleId: RequestBody,
        @Part("note") note: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part photo: MultipartBody.Part,
    ) : DefaultResponse

}