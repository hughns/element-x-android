/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package io.element.android.features.lockscreen.impl.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.element.android.features.lockscreen.impl.R
import io.element.android.features.lockscreen.impl.create.model.PinDigit
import io.element.android.features.lockscreen.impl.create.model.PinEntry
import io.element.android.features.lockscreen.impl.create.validation.CreatePinFailure
import io.element.android.libraries.designsystem.atomic.molecules.IconTitleSubtitleMolecule
import io.element.android.libraries.designsystem.atomic.pages.HeaderFooterPage
import io.element.android.libraries.designsystem.components.button.BackButton
import io.element.android.libraries.designsystem.components.dialogs.ErrorDialog
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.theme.components.Scaffold
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.designsystem.theme.components.TopAppBar
import io.element.android.libraries.designsystem.theme.pinDigitBg
import io.element.android.libraries.theme.ElementTheme

@Composable
fun CreatePinView(
    state: CreatePinState,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton(onClick = onBackClicked)
                },
                title = {}
            )
        },
        content = { padding ->
            HeaderFooterPage(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding),
                header = { CreatePinHeader(state.isConfirmationStep, state.pinSize) },
                content = { CreatePinContent(state) }
            )
        }
    )
}

@Composable
private fun CreatePinHeader(
    isValidationStep: Boolean,
    pinSize: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconTitleSubtitleMolecule(
            modifier = Modifier.padding(top = 60.dp, bottom = 12.dp),
            title = if (isValidationStep) {
                stringResource(id = R.string.screen_app_lock_setup_confirm_pin)
            } else {
                stringResource(id = R.string.screen_app_lock_setup_choose_pin, pinSize)
            },
            subTitle = stringResource(id = R.string.screen_app_lock_setup_pin_context),
            iconImageVector = Icons.Filled.Lock,
        )
        Text(
            text = stringResource(id = R.string.screen_app_lock_setup_pin_context_warning),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            style = ElementTheme.typography.fontBodyMdRegular,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun CreatePinContent(
    state: CreatePinState,
    modifier: Modifier = Modifier,
) {
    PinEntryTextField(
        state.activePinEntry,
        onValueChange = {
            state.eventSink(CreatePinEvents.OnPinEntryChanged(it))
        },
        modifier = modifier
            .padding(top = 36.dp)
            .fillMaxWidth()
    )
    if (state.createPinFailure != null) {
        ErrorDialog(
            modifier = modifier,
            title = state.createPinFailure.title(),
            content = state.createPinFailure.content(),
            onDismiss = {
                state.eventSink(CreatePinEvents.ClearFailure)
            }
        )
    }
}

@Composable
private fun CreatePinFailure.content(): String {
    return when (this) {
        CreatePinFailure.PinBlacklisted -> stringResource(id = R.string.screen_app_lock_setup_pin_blacklisted_dialog_content)
        CreatePinFailure.PinsDontMatch -> stringResource(id = R.string.screen_app_lock_setup_pin_mismatch_dialog_content)
    }
}

@Composable
private fun CreatePinFailure.title(): String {
    return when (this) {
        CreatePinFailure.PinBlacklisted -> stringResource(id = R.string.screen_app_lock_setup_pin_blacklisted_dialog_title)
        CreatePinFailure.PinsDontMatch -> stringResource(id = R.string.screen_app_lock_setup_pin_mismatch_dialog_title)
    }
}

@Composable
fun PinEntryTextField(
    pinEntry: PinEntry,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(pinEntry.toText()),
        onValueChange = {
            onValueChange(it.text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            PinEntryRow(pinEntry = pinEntry)
        }
    )
}

@Composable
private fun PinEntryRow(
    pinEntry: PinEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (digit in pinEntry.digits) {
            PinDigitView(digit = digit)
        }
    }
}

@Composable
private fun PinDigitView(
    digit: PinDigit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    val appearanceModifier = when (digit) {
        PinDigit.Empty -> {
            Modifier.border(1.dp, ElementTheme.colors.iconPrimary, shape)
        }
        is PinDigit.Filled -> {
            Modifier.background(ElementTheme.colors.pinDigitBg, shape)
        }
    }
    Box(
        modifier = modifier
            .size(40.dp, 50.dp)
            .then(appearanceModifier),
        contentAlignment = Alignment.Center,

        ) {
        if (digit is PinDigit.Filled) {
            Text(
                text = digit.toText(),
                style = ElementTheme.typography.fontHeadingMdBold
            )
        }

    }
}

@Composable
@PreviewsDayNight
internal fun CreatePinViewPreview(@PreviewParameter(CreatePinStateProvider::class) state: CreatePinState) {
    ElementPreview {
        CreatePinView(
            state = state,
            onBackClicked = {},
        )
    }
}
