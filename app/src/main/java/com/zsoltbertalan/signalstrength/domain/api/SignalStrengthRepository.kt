package com.zsoltbertalan.signalstrength.domain.api

import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.ext.ApiResult

interface SignalStrengthRepository {
	suspend fun getSignalStrength(apiKey: String, postcode: String): ApiResult<MobileAvailability>
}
