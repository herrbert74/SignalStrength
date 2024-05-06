package com.zsoltbertalan.signalstrength.ui.signal

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.signalstrength.UK_POSTCODE_REGEX
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.Intent
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignalExecutor(
	private val signalStrengthRepository: SignalStrengthRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, Nothing, State, Message, Nothing>(mainContext) {

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.PostcodeChanged -> {
				val regex = Regex(UK_POSTCODE_REGEX)
				val canContinue = regex.containsMatchIn(intent.postcode)
				val normalizedPostcode = intent.postcode.replace(" ", "").uppercase()
				dispatch(Message.PostcodeChanged(postcode = normalizedPostcode, canContinue))
			}

			Intent.GetAvailabilityClicked -> getAvailability(getState().postcode)
		}
	}

	private fun getAvailability(postcode: String) {
		scope.launch {
			signalStrengthRepository.getSignalStrength(postcode)
				.onSuccess {
					dispatch(Message.ShowAvailability(it))
				}.onFailure {
					dispatch(Message.ShowError(it))
				}
		}
	}

}