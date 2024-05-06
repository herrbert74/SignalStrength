package com.zsoltbertalan.signalstrength.data

import com.google.gson.Gson
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthService
import com.zsoltbertalan.signalstrength.data.network.dto.SignalStrengthGeneralErrorBody
import com.zsoltbertalan.signalstrength.data.network.dto.SignalStrengthPostcodeErrorBody
import com.zsoltbertalan.signalstrength.data.network.dto.toMobileAvailability
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.domain.model.throwable.GeneralException
import com.zsoltbertalan.signalstrength.domain.model.throwable.PostcodeException
import com.zsoltbertalan.signalstrength.ext.ApiResult
import com.zsoltbertalan.signalstrength.ext.apiRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class SignalStrengthAccessor(
	private val signalStrengthService: SignalStrengthService,
	private val ioContext: CoroutineDispatcher,
) : SignalStrengthRepository {

	override suspend fun getSignalStrength(postcode: String): ApiResult<MobileAvailability> {
		return apiRunCatching {
			withContext(ioContext) {

				val response = signalStrengthService.getSignal(postcode)
				if (response.body() != null) {
					response.body()?.toMobileAvailability()!!
				} else {
					val errorBody = response.errorBody()?.string()
					val postcodeErrorBody = Gson().fromJson(errorBody, SignalStrengthPostcodeErrorBody::class.java)
					val generalErrorBody = Gson().fromJson(errorBody, SignalStrengthGeneralErrorBody::class.java)
					throw if (postcodeErrorBody.Error != null) {
						PostcodeException(postcodeErrorBody.Error)
					} else if (generalErrorBody.message.isNotEmpty()) {
						GeneralException(generalErrorBody.message)
					} else {
						GeneralException("Something went wrong")
					}
				}

			}
		}
	}

}
