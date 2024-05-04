package com.zsoltbertalan.signalstrength.ui.signal

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.mvikotlin.core.store.Store
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.Intent
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.SideEffect
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.State
import kotlinx.parcelize.Parcelize

interface SignalStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class PostcodeChanged(val postcode: String) : Intent()
		data object GetAvailabilityClicked : Intent()
	}

	@Parcelize
	data class State(
		val postcode: String = "",
		val signal: MobileAvailability? = null,
		val canContinue: Boolean = false,
		val error: Throwable? = null
	) : Parcelable

	sealed class SideEffect {
		data object Initial : SideEffect()
		data object ShowToast : SideEffect()
	}

}
