package com.zsoltbertalan.signalstrength.ui

import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthRepository
import com.zsoltbertalan.signalstrength.testhelper.Mother
import com.zsoltbertalan.signalstrength.ui..Executor
import com.zsoltbertalan.signalstrength.ui..Store
import com.zsoltbertalan.signalstrength.ui..StoreFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class ExecutorTest {

	private val signalStrengthRepository = mockk<SignalStrengthRepository>(relaxed = true)

	private lateinit var Executor: Executor

	private lateinit var Store: Store

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { signalStrengthRepository.getAll() } answers { Mother.createList() }

Executor = Executor(
			signalStrengthRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

Store =
StoreFactory(DefaultStoreFactory(), Executor).create(stateKeeper = StateKeeperDispatcher())
	}

	@Test
	fun `when started then get is called and returns correct list`() {
		val states = Store.states.test()

		coVerify(exactly = 1) { signalStrengthRepository.getAll() }
		states.first(). shouldBe Mother.createList()
	}

	@Test
	fun `when sort button is pressed then get returned in reverse order`() {
		val states = Store.states.test()

Store.accept(Store.Intent.SortClicked)

		states.last(). shouldBe Mother.createList().reversed()
	}

}


