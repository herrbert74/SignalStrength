package com.zsoltbertalan.signalstrength.data.network.dto

import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability

@Suppress("PropertyName")
data class MobileAvailabilityDto(
	val Availability: List<AvailabilityDto>? = null,
	val DBName: String? = null,
	val PostCode: String? = null
) {

	data class AvailabilityDto(
		val AddressShortDescription: String? = null,
		val UPRN: Long? = null,
		val PostCode: String? = null,
		val EEDataIndoor: Int = 1,
		val EEDataIndoorNo4g: Int = 1,
		val EEDataOutdoor: Int = 1,
		val EEDataOutdoorNo4g: Int = 1,
		val EEVoiceIndoor: Int = 1,
		val EEVoiceIndoorNo4g: Int = 1,
		val EEVoiceOutdoor: Int = 1,
		val EEVoiceOutdoorNo4g: Int = 1,
		val H3DataIndoor: Int = 1,
		val H3DataIndoorNo4g: Int = 1,
		val H3DataOutdoor: Int = 1,
		val H3DataOutdoorNo4g: Int = 1,
		val H3VoiceIndoor: Int = 1,
		val H3VoiceIndoorNo4g: Int = 1,
		val H3VoiceOutdoor: Int = 1,
		val H3VoiceOutdoorNo4g: Int = 1,
		val TFDataIndoor: Int = 1,
		val TFDataIndoorNo4g: Int = 1,
		val TFDataOutdoor: Int = 1,
		val TFDataOutdoorNo4g: Int = 1,
		val TFVoiceIndoor: Int = 1,
		val TFVoiceIndoorNo4g: Int = 1,
		val TFVoiceOutdoor: Int = 1,
		val TFVoiceOutdoorNo4g: Int = 1,
		val VODataIndoor: Int = 1,
		val VODataIndoorNo4g: Int = 1,
		val VODataOutdoor: Int = 1,
		val VODataOutdoorNo4g: Int = 1,
		val VOVoiceIndoor: Int = 1,
		val VOVoiceIndoorNo4g: Int = 1,
		val VOVoiceOutdoor: Int = 1,
		val VOVoiceOutdoorNo4g: Int = 1
	)
}

fun MobileAvailabilityDto.toMobileAvailability() = MobileAvailability(
	provision = this.Availability?.associate { (it.AddressShortDescription ?: "") to it.toProvision() } ?: emptyMap(),
	postcode = this.PostCode
)

fun MobileAvailabilityDto.AvailabilityDto.toProvision() = MobileAvailability.Provision(
	this.UPRN,
	this.PostCode,
	mapOf(
		"EE" to MobileAvailability.ProviderData(
			this.EEDataIndoor, this.EEDataIndoorNo4g, this.EEDataOutdoor, this.EEDataOutdoorNo4g,
			this.EEVoiceIndoor, this.EEVoiceIndoorNo4g, this.EEVoiceOutdoor, this.EEVoiceOutdoorNo4g
		),
		"Three" to MobileAvailability.ProviderData(
			this.H3DataIndoor, this.H3DataIndoorNo4g, this.H3DataOutdoor, this.H3DataOutdoorNo4g,
			this.H3VoiceIndoor, this.H3VoiceIndoorNo4g, this.H3VoiceOutdoor, this.H3VoiceOutdoorNo4g
		),
		"O2" to MobileAvailability.ProviderData(
			this.TFDataIndoor, this.TFDataIndoorNo4g, this.TFDataOutdoor, this.TFDataOutdoorNo4g,
			this.TFVoiceIndoor, this.TFVoiceIndoorNo4g, this.TFVoiceOutdoor, this.TFVoiceOutdoorNo4g
		),
		"Vodafone" to MobileAvailability.ProviderData(
			this.VODataIndoor, this.VODataIndoorNo4g, this.VODataOutdoor, this.VODataOutdoorNo4g,
			this.VOVoiceIndoor, this.VOVoiceIndoorNo4g, this.VOVoiceOutdoor, this.VOVoiceOutdoorNo4g
		)
	)
)
