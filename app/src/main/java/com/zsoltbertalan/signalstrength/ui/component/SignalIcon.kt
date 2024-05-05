package com.zsoltbertalan.signalstrength.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.zsoltbertalan.signalstrength.R
import com.zsoltbertalan.signalstrength.design.Colors
import com.zsoltbertalan.signalstrength.design.Dimens
import com.zsoltbertalan.signalstrength.design.SignalStrengthTypography
import com.zsoltbertalan.signalstrength.ui.theme.SignalStrengthTheme

@Composable
fun SignalIcon(signal: Int, provider: String) {
	Column(
		Modifier
			.padding(Dimens.marginNormal)
			.border(width = Dimens.marginExtraSmall, color = Colors.secondaryContainer)
			.padding(Dimens.marginSmall)
	) {
		Text(provider, style = SignalStrengthTypography.labelMedium)
		Box {
			Icon(
				painterResource(id = R.drawable.ic_signal_cellular),
				contentDescription = "Signal",
				tint = Color.LightGray
			)
			val painter = when (signal) {
				2 -> R.drawable.ic_signal_cellular_1
				3 -> R.drawable.ic_signal_cellular_2
				else -> R.drawable.ic_signal_cellular
			}
			if (signal > 1) {
				Icon(
					painterResource(id = painter),
					contentDescription = "Signal",
					tint = Color.Green
				)
			}
		}
	}
}

@Preview("No Signal")
@Composable
fun SignalNoSignalPreview() {
	SignalStrengthTheme {
		SignalIcon(signal = 1, provider = "EE")
	}
}

@Preview("Weak Signal")
@Composable
fun SignalWeakSignalPreview() {
	SignalStrengthTheme {
		SignalIcon(signal = 2, provider = "EE")
	}
}

@Preview("Good Signal")
@Composable
fun SignalGoodSignalPreview() {
	SignalStrengthTheme {
		SignalIcon(signal = 3, provider = "EE")
	}
}

@Preview("Excellent Signal")
@Composable
fun SignalExcellentSignalPreview() {
	SignalStrengthTheme {
		SignalIcon(signal = 4, provider = "EE")
	}
}
