package com.zsoltbertalan.signalstrength

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.di.IoDispatcher
import com.zsoltbertalan.signalstrength.di.MainDispatcher
import com.zsoltbertalan.signalstrength.root.SignalStrengthRootComponent
import com.zsoltbertalan.signalstrength.root.SignalStrengthRootContent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule
	val composeTestRule = createComposeRule()

	@Inject
	lateinit var signalStrengthRepository: SignalStrengthRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	@Before
	fun setUp() {

		hiltAndroidRule.inject()

		CoroutineScope(mainContext).launch {
			val signalStrengthRootComponent = SignalStrengthRootComponent(
				DefaultComponentContext(lifecycle = LifecycleRegistry()),
				mainContext,
				ioContext,
				signalStrengthRepository,
			) {}
			composeTestRule.setContent {
				SignalStrengthRootContent(signalStrengthRootComponent)
			}
		}

	}

	@Test
	fun showSignal() {

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("SignalHeader"), 1000L)

		composeTestRule.onNodeWithText("Affenpinscher", ignoreCase = true).assertExists()

	}

	@Test
	fun showImages() {

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("SignalHeader"), 3000L)

		composeTestRule.onNodeWithText("Affenpinscher", ignoreCase = true).performClick()

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("Row"), 3000L)

		composeTestRule.onAllNodesWithTag("Row").assertAny(hasTestTag("Row"))

	}

}