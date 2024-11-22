package com.example.lapordriver.api.responses

import com.google.gson.annotations.SerializedName

data class ListVehicleResponse(

	@field:SerializedName("ListVehicleResponse")
	val listVehicleResponse: List<ListVehicleResponseItem?>? = null
)

data class ListVehicleResponseItem(

	@field:SerializedName("licenseNumber")
	val licenseNumber: String? = null,

	@field:SerializedName("vehicleId")
	val vehicleId: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
