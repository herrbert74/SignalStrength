package com.zsoltbertalan.signalstrength.ui

import com.zsoltbertalan.signalstrength.data.SignalStrengthAccessor
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthService
import com.zsoltbertalan.signalstrength.data.network.dto.MobileAvailabilityDto
import com.zsoltbertalan.signalstrength.ui.signal.SignalExecutor
import kotlinx.coroutines.Dispatchers

val defaultSignalStrengthService = object : SignalStrengthService {

	override suspend fun getSignal(apiKey: String, postcode: String): MobileAvailabilityDto {
		TODO("Not yet implemented")
	}
}

fun defaultSignalExecutor() = SignalExecutor(SignalStrengthAccessor(defaultSignalStrengthService), Dispatchers.Main, Dispatchers.IO)
