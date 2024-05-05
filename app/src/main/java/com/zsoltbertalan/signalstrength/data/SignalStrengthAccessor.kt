package com.zsoltbertalan.signalstrength.data

import com.google.gson.Gson
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthService
import com.zsoltbertalan.signalstrength.data.network.dto.SignalStrengthError
import com.zsoltbertalan.signalstrength.data.network.dto.toMobileAvailability
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.ext.ApiResult
import com.zsoltbertalan.signalstrength.ext.apiRunCatching
import java.lang.Exception
import javax.inject.Singleton

@Singleton
class SignalStrengthAccessor(
	private val signalStrengthService: SignalStrengthService,
) : SignalStrengthRepository {

	override suspend fun getSignalStrength(apiKey: String, postcode: String): ApiResult<MobileAvailability> {
		return apiRunCatching {
			val response = signalStrengthService.getSignal(apiKey, postcode)
			if (response.body() != null) {
				response.body()?.toMobileAvailability()!!
			} else {
				val error = Gson().fromJson(response.errorBody()?.string(), SignalStrengthError::class.java)
				throw Exception(error.Error)
			}

		}
	}

}
