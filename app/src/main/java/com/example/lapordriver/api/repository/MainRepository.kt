package com.example.lapordriver.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.lapordriver.api.RequestAPI
import com.example.lapordriver.api.responses.DefaultResponse
import com.example.lapordriver.api.responses.ListAllVehicleResponse
import com.example.lapordriver.api.responses.ListAllVehicleResponseItem
import com.example.lapordriver.api.responses.ListVehicleResponse
import com.example.lapordriver.api.responses.ListVehicleResponseItem
import com.example.lapordriver.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainRepository(private val requestAPI: RequestAPI) {

    fun getListVehichle() : LiveData<Result<List<ListVehicleResponseItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = requestAPI.listVehicle()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListAllVehicle() : LiveData<Result<List<ListAllVehicleResponseItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val userId = "8UjCTNZlRoLvxcd1gZ0kuWqMpcumCL"
            val response = requestAPI.listAllVehicle(userId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postLaporan(
        vehicleId: RequestBody,
        note: RequestBody,
        photo: MultipartBody.Part
    ) : LiveData<Result<DefaultResponse>> = liveData {
        emit(Result.Loading)
        try {
            val id = "8UjCTNZlRoLvxcd1gZ0kuWqMpcumCL"
            val userId = id.toRequestBody("text/plain".toMediaType())
            val response = requestAPI.addLaporan(vehicleId, note, userId, photo)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}