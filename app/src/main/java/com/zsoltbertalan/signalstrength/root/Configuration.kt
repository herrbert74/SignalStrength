package com.zsoltbertalan.signalstrength.root

import kotlinx.serialization.Serializable

@Serializable
sealed class Configuration {

	@Serializable
	data object Signal : Configuration()

}
