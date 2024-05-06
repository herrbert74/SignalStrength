package com.zsoltbertalan.signalstrength.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MobileAvailability(
	val provision: Map<String, Provision> = emptyMap(),
	val postcode: String? = null
) {

	@Serializable
	data class Provision(
		val uprn: Long? = null,
		val postcode: String? = null,
		val providers: Map<String, ProviderData> = emptyMap(),
	)

	@Serializable
	data class ProviderData(
		val dataIndoor: Int,
		val dataIndoorNo4Group: Int,
		val dataOutdoor: Int,
		val dataOutdoorNo4Group: Int,
		val voiceIndoor: Int,
		val voiceIndoorNo4Group: Int,
		val voiceOutdoor: Int,
		val voiceOutdoorNo4Group: Int,
	)

}
