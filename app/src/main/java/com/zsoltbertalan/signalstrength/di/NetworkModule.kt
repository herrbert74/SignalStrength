package com.zsoltbertalan.signalstrength.di

import com.zsoltbertalan.signalstrength.BASE_URL
import com.zsoltbertalan.signalstrength.data.SignalStrengthAccessor
import com.zsoltbertalan.signalstrength.data.network.SignalStrengthService
import com.zsoltbertalan.signalstrength.domain.api.SignalStrengthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class NetworkModule {

	@Provides
	@Singleton
	internal fun provideSignalStrengthRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()

		httpClient.addInterceptor(logging)
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	internal fun provideSignalStrengthService(retroFit: Retrofit): SignalStrengthService {
		return retroFit.create(SignalStrengthService::class.java)
	}

	@Provides
	@Singleton
	fun provideSignalStrengthRepository(
		signalStrengthService: SignalStrengthService,
	): SignalStrengthRepository {
		return SignalStrengthAccessor(signalStrengthService)
	}

}
