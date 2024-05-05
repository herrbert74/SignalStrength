package com.zsoltbertalan.signalstrength.data.network

import com.zsoltbertalan.signalstrength.data.network.dto.MobileAvailabilityDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SignalStrengthService {
	@GET("mobile/coverage/{postcode}")
	suspend fun getSignal(
		@Header("Ocp-Apim-Subscription-Key") apiKey: String,
		@Path("postcode") postcode: String,
	): Response<MobileAvailabilityDto>
}