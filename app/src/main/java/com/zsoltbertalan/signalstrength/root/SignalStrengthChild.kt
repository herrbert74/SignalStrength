package com.zsoltbertalan.signalstrength.root

import com.zsoltbertalan.signalstrength.ui.signal.SignalComp

sealed class SignalStrengthChild {
    data class Signal(val component: SignalComp) : SignalStrengthChild()
}
