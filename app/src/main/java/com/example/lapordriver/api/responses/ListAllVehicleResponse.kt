package com.example.lapordriver.api.responses

import com.google.gson.annotations.SerializedName

data class ListAllVehicleResponse(

	@field:SerializedName("ListAllVehicleResponse")
	val listAllVehicleResponse: List<ListAllVehicleResponseItem?>? = null
)

data class ListAllVehicleResponseItem(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("vehicleName")
	val vehicleName: String? = null,

	@field:SerializedName("reportId")
	val reportId: String? = null,

	@field:SerializedName("vehicleLicenseNumber")
	val vehicleLicenseNumber: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("reportStatus")
	val reportStatus: String? = null,

	@field:SerializedName("vehicleId")
	val vehicleId: String? = null
)
