package com.zsoltbertalan.signalstrength.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.DefaultComponentContext
import com.zsoltbertalan.signalstrength.di.IoDispatcher
import com.zsoltbertalan.signalstrength.di.MainDispatcher
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.root.SignalStrengthRootComponent
import com.zsoltbertalan.signalstrength.root.SignalStrengthRootContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class SignalStrengthActivity : ComponentActivity() {

	@Inject
	lateinit var signalStrengthRepository: SignalStrengthRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	private lateinit var signalStrengthRootComponent: SignalStrengthRootComponent

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		signalStrengthRootComponent = SignalStrengthRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			mainContext,
			ioContext,
			signalStrengthRepository
		) { finish() }

		setContent {
			SignalStrengthRootContent(signalStrengthRootComponent)
		}
	}

}
