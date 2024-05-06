package com.zsoltbertalan.signalstrength.data.network

import com.zsoltbertalan.signalstrength.BuildConfig
import com.zsoltbertalan.signalstrength.data.network.dto.MobileAvailabilityDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SignalStrengthService {
	@GET("mobile/coverage/{postcode}")
	suspend fun getSignal(
		@Path("postcode") postcode: String,
		@Header("Ocp-Apim-Subscription-Key") apiKey: String = BuildConfig.ofcomAccessToken,
	): Response<MobileAvailabilityDto>
}
