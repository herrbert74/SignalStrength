package com.zsoltbertalan.signalstrength.domain.model

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MobileAvailability(
	val provision: Map<String, Provision> = emptyMap(),
	val postcode: String? = null
) : Parcelable {

	@Parcelize
	data class Provision(
		val uprn: Long? = null,
		val postcode: String? = null,
		val providers: Map<String, ProviderData> = emptyMap(),
//		val ee: ProviderData = ProviderData("EE", 1, 1, 1, 1, 1, 1, 1, 1),
//		val h3: ProviderData = ProviderData("Three", 1, 1, 1, 1, 1, 1, 1, 1),
//		val tf: ProviderData = ProviderData("O2", 1, 1, 1, 1, 1, 1, 1, 1),
//		val vo: ProviderData = ProviderData("Vodafone", 1, 1, 1, 1, 1, 1, 1, 1),
	) : Parcelable

	@Parcelize
	data class ProviderData(
		//val providerName: String,
		val dataIndoor: Int,
		val dataIndoorNo4Group: Int,
		val dataOutdoor: Int,
		val dataOutdoorNo4Group: Int,
		val voiceIndoor: Int,
		val voiceIndoorNo4Group: Int,
		val voiceOutdoor: Int,
		val voiceOtdoorNo4Group: Int,
	) : Parcelable
}
