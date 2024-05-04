package com.zsoltbertalan.signalstrength.ui.signal

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.base.compose.design.smallDimensions
import com.zsoltbertalan.signalstrength.design.Dimens
import com.zsoltbertalan.signalstrength.design.SignalStrengthTheme
import com.zsoltbertalan.signalstrength.design.SignalStrengthTypography
import com.zsoltbertalan.signalstrength.ui.defaultSignalExecutor
import com.zsoltbertalan.signalstrength.ui.signal.SignalStore.SideEffect.Initial

@Composable
fun SignalScreen(component: SignalComp) {

	val model by component.state.subscribeAsState()

	val sideEffect by component.sideEffects.collectAsState(Initial)

	BackHandler(onBack = { component.onBackClicked() })

	SignalScaffold(component, model, sideEffect)

}

@Composable
private fun SignalScaffold(
	component: SignalComp,
	model: SignalStore.State,
	sideEffect: SignalStore.SideEffect
) {

	var isExpanded by rememberSaveable { mutableStateOf(false) }

	var selectedText by rememberSaveable { mutableStateOf("") }

	var textFieldSize by remember { mutableStateOf(Size.Zero) }

	val icon = if (isExpanded)
		Icons.Filled.KeyboardArrowUp
	else
		Icons.Filled.KeyboardArrowDown

	Scaffold(
		topBar = {
			TopAppBar(
				colors = topAppBarColors(
					containerColor = SignalStrengthTheme.colorScheme.primaryContainer,
					titleContentColor = SignalStrengthTheme.colorScheme.primary,
				),
				title = {
					Text("Signal Strength")
				},
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
		) {

			var postcode by rememberSaveable { mutableStateOf(model.postcode) }

			Text(
				text = "Please enter your postcode",
				modifier = Modifier
					.padding(Dimens.marginLarge),
				style = SignalStrengthTypography.labelLarge
			)
			TextField(
				modifier = Modifier.padding(Dimens.marginLarge),
				value = postcode,
				onValueChange = {
					postcode = it
					component.postcodeChanged(it)
				},
				label = { Text("Postcode") }
			)

			if (model.signal != null) {
				Column(Modifier.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)) {

					OutlinedTextField(
						value = selectedText,
						onValueChange = { selectedText = it },
						modifier = Modifier
							.fillMaxWidth()
							.onGloballyPositioned { coordinates ->
								textFieldSize = coordinates.size.toSize()
							},
						label = { Text("Choose your address") },
						trailingIcon = {
							Icon(
								icon,
								"contentDescription",
								Modifier.clickable { isExpanded = !isExpanded }
							)
						}
					)

					DropdownMenu(
						expanded = isExpanded,
						onDismissRequest = { isExpanded = false },
						modifier = Modifier
							.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginLarge)
							.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
					) {
						model.signal.provision?.forEach { label ->
							DropdownMenuItem(
								text = { Text(label?.addressShortDescription ?: "") },
								onClick = {
									selectedText = label?.addressShortDescription ?: ""
									isExpanded = false
								})
						}
					}
				}
			}
			if (selectedText.isNotEmpty()) {
				Text(
					model.signal?.provision?.getOrNull(0)?.addressShortDescription ?: "",
					modifier = Modifier
						.padding(
							horizontal = smallDimensions.marginLarge,
							vertical = smallDimensions.marginNormal,
						)
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Button(
				modifier = Modifier.padding(Dimens.marginLarge),
				enabled = model.canContinue,
				onClick = { component.onGetAvailabilityClicked() }
			) {
				Text("Check Signal Strength")
			}
		}
	}
}

@Preview("Signal Screen Preview")
@Composable
fun SignalScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	SignalScaffold(
		SignalComponent(componentContext, defaultSignalExecutor(), {}, {}),
		SignalStore.State(signal = null),
		Initial
	)
}
