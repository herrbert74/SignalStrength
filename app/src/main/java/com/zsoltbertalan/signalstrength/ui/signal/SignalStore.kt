package com.zsoltbertalan.signalstrength.ui.signal

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.domain.model.NotSerializable
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.Intent
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.State
import kotlinx.serialization.Serializable

interface SignalStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		data class PostcodeChanged(val postcode: String) : Intent()
		data object GetAvailabilityClicked : Intent()
	}

	@Serializable
	@Immutable
	data class State(
		val postcode: String = "",
		val signal: MobileAvailability? = null,
		val canContinue: Boolean = false,
		@Suppress("SERIALIZER_TYPE_INCOMPATIBLE") @Serializable( with = NotSerializable::class )
		val error: Throwable? = null
	)

}
