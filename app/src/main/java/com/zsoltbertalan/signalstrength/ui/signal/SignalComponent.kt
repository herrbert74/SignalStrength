package com.zsoltbertalan.signalstrength.ui.signal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.zsoltbertalan.signalstrength.POSTCODE_DEBOUNCE_DURATION
import com.zsoltbertalan.signalstrength.ext.asValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

interface SignalComp {

	fun postcodeChanged(postcode: String)

	fun onGetAvailabilityClicked()

	fun onBackClicked()

	val state: Value<SignalStore.State>

	sealed class Output {
		data class Selected(val x: String) : Output()
	}

}

class SignalComponent(
	componentContext: ComponentContext,
	val signalExecutor: SignalExecutor,
	@Suppress("unused") private val output: FlowCollector<SignalComp.Output>,
	private val finishHandler: () -> Unit,
	private val mainContext: CoroutineDispatcher,
) : SignalComp, ComponentContext by componentContext {

	//A flow to debounce quick changes in the postcode
	private val mutablePostcodeFlow = MutableStateFlow("")
	private val postcodeFlow = mutablePostcodeFlow.debounce(POSTCODE_DEBOUNCE_DURATION).distinctUntilChanged()

	init {
		CoroutineScope(mainContext).launch {
			postcodeFlow.collect {
				signalStore.accept(SignalStore.Intent.PostcodeChanged(it))
			}
		}
	}

	private var signalStore: SignalStore =
		SignalStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), signalExecutor).create(stateKeeper)

	override fun postcodeChanged(postcode: String) {
		CoroutineScope(mainContext).launch {
			mutablePostcodeFlow.emit(postcode)
		}
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

}
