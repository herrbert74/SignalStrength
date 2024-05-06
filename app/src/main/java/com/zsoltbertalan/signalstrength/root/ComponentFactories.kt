package com.zsoltbertalan.signalstrength.root

import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.ui.signal.SignalComponent
import com.zsoltbertalan.signalstrength.ui.signal.SignalExecutor
import kotlinx.coroutines.CoroutineDispatcher

/**
* These are higher order functions with common parameters used in the RootComponent,
* that return functions, that create the Decompose feature components from feature specific parameters.
*/

internal fun createSignalFactory(
	signalStrengthRepository: SignalStrengthRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateSignalComp = { childContext, finishHandler, output ->
	SignalComponent(
		componentContext = childContext,
		signalExecutor = SignalExecutor(signalStrengthRepository, mainContext, ioContext),
		output = output,
		mainContext = mainContext,
		finishHandler = finishHandler,
	)
}
