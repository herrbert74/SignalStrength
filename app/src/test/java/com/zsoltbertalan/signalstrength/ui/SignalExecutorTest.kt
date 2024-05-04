package com.zsoltbertalan.signalstrength.ui

import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.testhelper.Mother
import com.zsoltbertalan.signalstrength.ui.signal.SignalExecutor
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore
import com.zsoltbertalan.signalstrength.ui.signal.SignalStoreFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class SignalExecutorTest {

	private val signalStrengthRepository = mockk<SignalStrengthRepository>(relaxed = true)

	private lateinit var signalExecutor: SignalExecutor

	private lateinit var signalStore: SignalStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { signalStrengthRepository.getAllSignal() } answers { Mother.createList() }

		signalExecutor = SignalExecutor(
			signalStrengthRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		signalStore =
			SignalStoreFactory(DefaultStoreFactory(), signalExecutor).create(stateKeeper = StateKeeperDispatcher())
	}

	@Test
	fun `when started then getSignal is called and returns correct list`() {
		val states = signalStore.states.test()

		coVerify(exactly = 1) { signalStrengthRepository.getAllSignal() }
		states.first().signal shouldBe Mother.createList()
	}

	@Test
	fun `when sort button is pressed then getSignal returned in reverse order`() {
		val states = signalStore.states.test()

		signalStore.accept(SignalStore.Intent.SortSignalClicked)

		states.last().signal shouldBe Mother.createList().reversed()
	}

}


