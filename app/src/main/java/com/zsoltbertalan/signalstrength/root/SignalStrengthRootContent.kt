package com.zsoltbertalan.signalstrength.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.zsoltbertalan.signalstrength.design.SignalStrengthTheme
import com.zsoltbertalan.signalstrength.ui.signal.SignalScreen

@Composable
fun SignalStrengthRootContent(component: SignalStrengthRootComp) {

	val stack = component.childStackValue

	SignalStrengthTheme {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is SignalStrengthChild.Signal -> SignalScreen(child.component)
			}
		}
	}

}
