package com.zsoltbertalan.signalstrength.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zsoltbertalan.signalstrength.ui.theme.SignalStrengthTheme

@Composable
fun SignalRow(signal1: Int, signalName1: String, signal2: Int, signalName2: String, provider: String) {
	Row(
		Modifier
			.fillMaxWidth()
	) {
		Spacer(modifier = Modifier.weight(1f))
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Text(text = signalName1)
			SignalIcon(signal = signal1, provider = provider)
		}
		Spacer(modifier = Modifier.weight(1f))
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Text(text = signalName2)
			SignalIcon(signal = signal2, provider = provider)
		}
		Spacer(modifier = Modifier.weight(1f))
	}
}

@Preview
@Composable
fun SignalRowPreview() {
	SignalStrengthTheme {
		SignalRow(
			1,
			"Data Outdoor",
			3,
			"Data Outdoor No4G",
			provider = "EE",
		)
	}
}
