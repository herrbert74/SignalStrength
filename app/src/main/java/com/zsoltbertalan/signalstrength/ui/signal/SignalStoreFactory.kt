package com.zsoltbertalan.signalstrength.ui.signal

import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.Intent
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.SideEffect
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.State

class SignalStoreFactory(
	private val storeFactory: StoreFactory,
	private val signalExecutor: SignalExecutor,
) {

	fun create(stateKeeper: StateKeeper): SignalStore =
		object : SignalStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "SignalStore",
			initialState = stateKeeper.consume(key = "SignalStore") ?: State(),
			bootstrapper = SignalBootstrapper(),
			executorFactory = { signalExecutor },
			reducer = SignalReducer
		) {
		}.also {
			stateKeeper.register(key = "SignalStore") {
				it.state.copy()
			}
		}

	private class SignalBootstrapper : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.ShowSignal)
		}
	}

	private object SignalReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.PostcodeChanged -> copy(postcode = msg.postcode, canContinue = msg.canBeShown, error = null)
				is Message.ShowAvailability -> copy(signal = msg.signal)
				is Message.ShowError -> copy(signal = null, error = msg.throwable)
			}
	}

}

sealed class Message {
	data class PostcodeChanged(val postcode: String, val canBeShown: Boolean) : Message()
	data class ShowAvailability(val signal: MobileAvailability) : Message()
	data class ShowError(val throwable: Throwable) : Message()
}

sealed class BootstrapIntent {
	data object ShowSignal : BootstrapIntent()
}
