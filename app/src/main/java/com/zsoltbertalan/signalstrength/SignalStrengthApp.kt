package com.zsoltbertalan.signalstrength

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class SignalStrengthApp : Application() {

	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) {
			plant(Timber.DebugTree())
		}
	}

}