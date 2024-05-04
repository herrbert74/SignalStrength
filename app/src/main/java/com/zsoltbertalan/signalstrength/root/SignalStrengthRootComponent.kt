package com.zsoltbertalan.signalstrength.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.zsoltbertalan.signalstrength.di.IoDispatcher
import com.zsoltbertalan.signalstrength.di.MainDispatcher
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.ui.signal.SignalComp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector

typealias CreateSignalComp = (ComponentContext, () -> Unit, FlowCollector<SignalComp.Output>) -> SignalComp
interface SignalStrengthRootComp {
	val childStackValue: Value<ChildStack<*, SignalStrengthChild>>
}

class SignalStrengthRootComponent internal constructor(
	componentContext: ComponentContext,
	private val finishHandler: () -> Unit,
	private val createSignalComp: CreateSignalComp,
) : SignalStrengthRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@IoDispatcher ioContext: CoroutineDispatcher,
		signalStrengthRepository: SignalStrengthRepository,
		finishHandler: () -> Unit,
	) : this(
		componentContext = componentContext,
		finishHandler,
		createSignalComp = createSignalFactory(signalStrengthRepository, mainContext, ioContext),
	)

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.Signal) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): SignalStrengthChild =
		when (configuration) {

			is Configuration.Signal -> SignalStrengthChild.Signal(
				createSignalComp(
					componentContext.childContext(key = "SignalComponent"),
					finishHandler,
					FlowCollector(::onSignalOutput)
				)
			)
		}

}
