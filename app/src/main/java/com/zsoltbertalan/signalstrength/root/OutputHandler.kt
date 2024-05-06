package com.zsoltbertalan.signalstrength.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.zsoltbertalan.signalstrength.ui.signal.SignalComp

val navigation = StackNavigation<Configuration>()

internal fun onSignalOutput(output: SignalComp.Output): Unit = when (output) {

	is SignalComp.Output.Selected -> {
		//no-op in this project
	}

}
