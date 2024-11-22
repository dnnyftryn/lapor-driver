package com.example.lapordriver.ui

import androidx.lifecycle.ViewModel
import com.example.lapordriver.api.repository.MainRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getAllVehicle() = mainRepository.getListAllVehicle()

    fun getVehicle() = mainRepository.getListVehichle()

    fun addLaporan(
        vehicleId: RequestBody,
        notes: RequestBody,
        photo: MultipartBody.Part,
    ) = mainRepository.postLaporan(
        vehicleId,
        notes,
        photo
    )
}