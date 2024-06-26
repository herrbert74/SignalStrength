package com.zsoltbertalan.signalstrength.ext

import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.rx.Disposable

typealias ValueObserver<T> = (T) -> Unit

fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
	object : Value<T>() {
		override val value: T get() = state
		private var disposables = emptyMap<ValueObserver<T>, Disposable>()

		override fun subscribe(observer: ValueObserver<T>) {
			val disposable = states(com.arkivanov.mvikotlin.rx.observer(onNext = observer))
			this.disposables += observer to disposable
		}

		override fun unsubscribe(observer: ValueObserver<T>) {
			val disposable = disposables[observer] ?: return
			this.disposables -= observer
			disposable.dispose()
		}
	}
