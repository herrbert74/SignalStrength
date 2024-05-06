package com.zsoltbertalan.signalstrength.ui.signal

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.zsoltbertalan.signalstrength.design.Colors
import com.zsoltbertalan.signalstrength.design.Dimens
import com.zsoltbertalan.signalstrength.design.SignalStrengthTheme
import com.zsoltbertalan.signalstrength.design.SignalStrengthTypography
import com.zsoltbertalan.signalstrength.domain.model.throwable.PostcodeException
import com.zsoltbertalan.signalstrength.ui.component.SignalDashboard
import com.zsoltbertalan.signalstrength.ui.defaultSignalExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignalScreen(component: SignalComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	SignalScaffold(component, model)

}

@Composable
private fun SignalScaffold(
	component: SignalComp,
	model: SignalStore.State,
) {

	val reusableModifier = Modifier.padding(bottom = Dimens.marginLarge)
	var isAddressExpanded by rememberSaveable { mutableStateOf(false) }
	var isProviderExpanded by rememberSaveable { mutableStateOf(false) }

	var selectedAddress by rememberSaveable { mutableStateOf("") }
	var selectedProvider by rememberSaveable { mutableStateOf("EE") }

	val scope = rememberCoroutineScope()
	val snackbarHostState = remember { SnackbarHostState() }
	val focusManager = LocalFocusManager.current

	val addressIcon = if (isAddressExpanded)
		Icons.Filled.KeyboardArrowUp
	else
		Icons.Filled.KeyboardArrowDown

	val providerIcon = if (isProviderExpanded)
		Icons.Filled.KeyboardArrowUp
	else
		Icons.Filled.KeyboardArrowDown

	LaunchedEffect(model.error) {
		if (model.error != null && model.error !is PostcodeException) {
			scope.launch {
				snackbarHostState.showSnackbar(model.error.message ?: " Something happened")
			}
		}
	}

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
		},
		snackbarHost = {
			SnackbarHost(hostState = snackbarHostState)
		},
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
				.padding(horizontal = Dimens.marginLarge)
				.fillMaxSize()
		) {

			var postcode by rememberSaveable { mutableStateOf(model.postcode) }

			Text(
				text = "Please enter your postcode",
				modifier = Modifier
					.padding(top = Dimens.marginLarge, bottom = Dimens.marginNormal),
				style = SignalStrengthTypography.labelLarge
			)
			TextField(
				modifier = reusableModifier,
				value = postcode,
				onValueChange = {
					postcode = it
					selectedAddress = ""
					component.postcodeChanged(it)
				},
				label = { Text("Postcode") },
				supportingText = {
					if (model.error != null && model.error is PostcodeException) {
						Text(text = model.error.message)
					} else Text(text = "")
				},
				trailingIcon = {
					if (model.error != null && model.error is PostcodeException)
						Icon(Icons.Filled.Warning, "error", tint = Colors.error)
				},
				isError = model.error != null && model.error is PostcodeException,
			)
			Button(
				modifier = reusableModifier,
				enabled = model.canContinue,
				onClick = {
					focusManager.clearFocus()
					component.onGetAvailabilityClicked()
				}
			) {
				Text("Check Signal Strength")
			}

			if (model.canContinue && model.signal != null) {
				ExposedDropdownMenuBox(
					expanded = isAddressExpanded,
					onExpandedChange = { isAddressExpanded = it },
				) {
					OutlinedTextField(
						modifier = reusableModifier
							.fillMaxWidth()
							.menuAnchor(),
						value = selectedAddress,
						onValueChange = { selectedAddress = it },
						singleLine = true,
						readOnly = true,
						label = { Text("Choose your address") },
						trailingIcon = {
							Icon(
								addressIcon,
								"Address dropdown icon",
							)
						}
					)

					ExposedDropdownMenu(
						expanded = isAddressExpanded,
						onDismissRequest = { isAddressExpanded = false },
						modifier = Modifier
							.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginLarge)
					) {
						model.signal.provision.keys.forEach { address ->
							DropdownMenuItem(
								text = { Text(address) },
								onClick = {
									selectedAddress = address
									isAddressExpanded = false
								}
							)
						}
					}
				}

				if (selectedAddress.isNotEmpty()) {
					ExposedDropdownMenuBox(
						expanded = isProviderExpanded,
						onExpandedChange = { isProviderExpanded = it },
					) {
						OutlinedTextField(
							value = selectedProvider,
							onValueChange = { selectedProvider = it },
							singleLine = true,
							readOnly = true,
							modifier = reusableModifier
								.fillMaxWidth()
								.menuAnchor(),
							label = { Text("Choose your provider") },
							trailingIcon = {
								Icon(
									providerIcon,
									"Provider dropdown icon",
								)
							}
						)

						ExposedDropdownMenu(
							expanded = isProviderExpanded,
							onDismissRequest = { isProviderExpanded = false },
							modifier = reusableModifier
						) {
							model.signal.provision[selectedAddress]?.providers?.keys?.forEach { provider ->
								DropdownMenuItem(
									text = { Text(provider) },
									onClick = {
										selectedProvider = provider
										isProviderExpanded = false
									}
								)
							}
						}
					}
				}
			}
			if (selectedAddress.isNotEmpty()) {
				model.signal?.provision?.get(selectedAddress)?.providers?.get(selectedProvider)?.let {
					SignalDashboard(
						selectedProvider, it,
					)
				}
			}
			Spacer(modifier = Modifier.weight(1f))
		}
	}
}

@Preview("Signal Screen Preview")
@Composable
fun SignalScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	SignalScaffold(
		SignalComponent(componentContext, defaultSignalExecutor(), {}, {}, Dispatchers.Main),
		SignalStore.State(signal = null),
	)
}
