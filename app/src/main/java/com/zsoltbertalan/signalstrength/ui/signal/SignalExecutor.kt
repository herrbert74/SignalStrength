package com.zsoltbertalan.signalstrength.ui.signal

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.signalstrength.API_KEY
import com.zsoltbertalan.signalstrength.UK_POSTCODE_REGEX
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.Intent
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.SideEffect
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SignalExecutor(
	private val signalStrengthRepository: SignalStrengthRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowSignal -> {

			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.PostcodeChanged -> {
				val regex = Regex(UK_POSTCODE_REGEX)
				val canContinue = regex.containsMatchIn(intent.postcode)
				dispatch(Message.PostcodeChanged(postcode = intent.postcode, canContinue))
			}

			Intent.GetAvailabilityClicked -> getAvailability(getState().postcode)
		}
	}

	private fun getAvailability(postcode: String) {
		scope.launch(ioContext) {
			signalStrengthRepository.getSignalStrength(API_KEY, postcode)
				.onSuccess {
					withContext(mainContext) { dispatch(Message.ShowAvailability( it)) }
				}.onFailure {
					withContext(mainContext) { dispatch(Message.ShowError(it)) }
					//publish(SideEffect.ShowToast)
				}
		}
	}

}