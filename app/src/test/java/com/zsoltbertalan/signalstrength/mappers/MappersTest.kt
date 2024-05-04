package com.zsoltbertalan.signalstrength.mappers

import com.zsoltbertalan.signalstrength.testhelper.DtoMother
import com.zsoltbertalan.signalstrength.data.network.dto.Dto
import com.zsoltbertalan.signalstrength.model.
import com.zsoltbertalan.signalstrength.data.network.dto.toSignalList
import com.zsoltbertalan.signalstrength.data.network.dto.Status
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class MappersTest {

	private var mappedResponse :List<> = emptyList()

	@Before
	fun setup() {
		val responseDto = DtoMother.createDtoList()
		mappedResponse = responseDto.toList()
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse[0].name shouldBe "Walter White"
	}

	@Test
	fun `when there is a response then occupation is mapped`() {
		mappedResponse[0].occupation shouldBe "High School Chemistry Teacher, Meth King Pin"
	}

	@Test
	fun `when there is a response then status is mapped`() {
		mappedResponse[0].status shouldBe Status.Unknown
	}

}
