package com.zsoltbertalan.signalstrength.data

import com.zsoltbertalan.signalstrength.data.network.SignalStrengthService
import com.zsoltbertalan.signalstrength.data.network.dto.toMobileAvailability
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.ext.ApiResult
import com.zsoltbertalan.signalstrength.ext.apiRunCatching
import javax.inject.Singleton

@Singleton
class SignalStrengthAccessor(
	private val signalStrengthService: SignalStrengthService,
) : SignalStrengthRepository {

	override suspend fun getSignalStrength(apiKey: String, postcode: String): ApiResult<MobileAvailability> {
		return apiRunCatching { signalStrengthService.getSignal(apiKey, postcode).toMobileAvailability() }
	}

}
