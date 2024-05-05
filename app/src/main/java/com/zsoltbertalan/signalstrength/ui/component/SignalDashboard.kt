package com.zsoltbertalan.signalstrength.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zsoltbertalan.signalstrength.design.Colors
import com.zsoltbertalan.signalstrength.design.Dimens
import com.zsoltbertalan.signalstrength.domain.model.MobileAvailability
import com.zsoltbertalan.signalstrength.ui.theme.SignalStrengthTheme

@Composable
fun SignalDashboard(name:String, providerData: MobileAvailability.ProviderData) {
	Column(
		Modifier
			.fillMaxWidth()
			.padding(bottom = Dimens.marginNormal)
			.border(width = Dimens.marginSmallest, color = Colors.primary)
			.padding(Dimens.marginNormal)
	) {
		SignalRow(
			signal1 = providerData.dataIndoor,
			signalName1 = "Data Indoor",
			signal2 = providerData.dataIndoorNo4Group,
			signalName2 = "Data Indoor No 4G",
			provider = name
		)
		SignalRow(
			signal1 = providerData.dataOutdoor,
			signalName1 = "Data Outdoor",
			signal2 = providerData.dataOutdoorNo4Group,
			signalName2 = "Data Outdoor No 4G",
			provider = name
		)
		SignalRow(
			signal1 = providerData.voiceIndoor,
			signalName1 = "Voice Indoor",
			signal2 = providerData.voiceOtdoorNo4Group,
			signalName2 = "Voice Indoor No 4G",
			provider = name
		)
		SignalRow(
			signal1 = providerData.dataOutdoor,
			signalName1 = "Voice Outdoor",
			signal2 = providerData.dataOutdoorNo4Group,
			signalName2 = "Voice Outdoor No 4G",
			provider = name
		)
	}
}

@Preview
@Composable
fun SignalDashboardPreview() {
	SignalStrengthTheme {
		SignalDashboard(
			name="EE",
			providerData = MobileAvailability.ProviderData(
				1,
				2,
				3,
				4,
				1,
				2,
				3,
				4,
			)
		)
	}
}
