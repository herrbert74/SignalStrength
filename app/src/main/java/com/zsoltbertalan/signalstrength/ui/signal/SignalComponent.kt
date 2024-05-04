package com.zsoltbertalan.signalstrength.ui.signal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.zsoltbertalan.signalstrength.ext.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface SignalComp {

	fun postcodeChanged(postcode: String)

	fun onGetAvailabilityClicked()

	fun onBackClicked()

	val state: Value<SignalStore.State>

	val sideEffects: Flow<SignalStore.SideEffect>

	sealed class Output {
		data class Selected(val x: String) : Output()
	}

}

class SignalComponent(
	componentContext: ComponentContext,
	val signalExecutor: SignalExecutor,
	private val output: FlowCollector<SignalComp.Output>,
	private val finishHandler: () -> Unit,
) : SignalComp, ComponentContext by componentContext {

	private var signalStore: SignalStore =
		SignalStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), signalExecutor).create(stateKeeper)

	override fun postcodeChanged(postcode: String) {
		signalStore.accept(SignalStore.Intent.PostcodeChanged(postcode))
	}

	override fun onGetAvailabilityClicked() {
		signalStore.accept(SignalStore.Intent.GetAvailabilityClicked)
	}

	override fun onBackClicked() {
		CoroutineScope(signalExecutor.mainContext).launch {
			finishHandler.invoke()
			signalStore.dispose()
		}
	}

	override val state: Value<SignalStore.State>
		get() = signalStore.asValue()

	override val sideEffects: Flow<SignalStore.SideEffect>
		get() = signalStore.labels

}
