/*
 * Copyright (c) 2024 New Vector Ltd
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

package io.element.android.features.login.impl.screens.qrcode.error

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import io.element.android.compound.theme.ElementTheme
import io.element.android.features.login.impl.R
import io.element.android.features.login.impl.qrcode.QrCodeErrorScreenType
import io.element.android.libraries.designsystem.atomic.organisms.NumberedListOrganism
import io.element.android.libraries.designsystem.atomic.pages.FlowStepPage
import io.element.android.libraries.designsystem.components.BigIcon
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.theme.components.Button
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.ui.strings.CommonStrings
import kotlinx.collections.immutable.persistentListOf

@Composable
fun QrCodeErrorView(
    modifier: Modifier = Modifier,
    errorScreenType: QrCodeErrorScreenType,
    onRetry: () -> Unit,
) {
    BackHandler {
        onRetry()
    }
    FlowStepPage(
        modifier = modifier,
        iconStyle = BigIcon.Style.AlertSolid,
        title = titleText(errorScreenType),
        subTitle = subtitleText(errorScreenType),
        content = { Content(errorScreenType) },
        buttons = { Buttons(onRetry) }
    )
}

@Composable
private fun titleText(errorScreenType: QrCodeErrorScreenType) = when (errorScreenType) {
    QrCodeErrorScreenType.Cancelled -> stringResource(R.string.screen_qr_code_login_error_cancelled_title)
    QrCodeErrorScreenType.Declined -> stringResource(R.string.screen_qr_code_login_error_declined_title)
    QrCodeErrorScreenType.Expired -> stringResource(R.string.screen_qr_code_login_error_expired_title)
    QrCodeErrorScreenType.ProtocolNotSupported -> stringResource(R.string.screen_qr_code_login_error_linking_not_suported_title)
    QrCodeErrorScreenType.InsecureChannelDetected -> stringResource(id = R.string.screen_qr_code_login_connection_note_secure_state_title)
    is QrCodeErrorScreenType.UnknownError -> stringResource(CommonStrings.common_something_went_wrong)
}

@Composable
private fun subtitleText(errorScreenType: QrCodeErrorScreenType) = when (errorScreenType) {
    QrCodeErrorScreenType.Cancelled -> stringResource(R.string.screen_qr_code_login_error_cancelled_subtitle)
    QrCodeErrorScreenType.Declined -> stringResource(R.string.screen_qr_code_login_error_declined_subtitle)
    QrCodeErrorScreenType.Expired -> stringResource(R.string.screen_qr_code_login_error_expired_subtitle)
    QrCodeErrorScreenType.ProtocolNotSupported -> stringResource(R.string.screen_qr_code_login_error_linking_not_suported_subtitle)
    QrCodeErrorScreenType.InsecureChannelDetected -> stringResource(id = R.string.screen_qr_code_login_connection_note_secure_state_description)
    is QrCodeErrorScreenType.UnknownError -> stringResource(R.string.screen_qr_code_login_unknown_error_description)
}

@Composable
private fun ColumnScope.InsecureChannelDetectedError() {
    Text(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        text = stringResource(R.string.screen_qr_code_login_connection_note_secure_state_list_header),
        style = ElementTheme.typography.fontBodyLgMedium,
        textAlign = TextAlign.Center,
    )
    NumberedListOrganism(
        modifier = Modifier.fillMaxSize(),
        items = persistentListOf(
            AnnotatedString(stringResource(R.string.screen_qr_code_login_connection_note_secure_state_list_item_1)),
            AnnotatedString(stringResource(R.string.screen_qr_code_login_connection_note_secure_state_list_item_2)),
            AnnotatedString(stringResource(R.string.screen_qr_code_login_connection_note_secure_state_list_item_3)),
        )
    )
}

@Composable
private fun ErrorMessageParagraph(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = ElementTheme.typography.fontBodyMdRegular,
        color = ElementTheme.colors.textSecondary,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun Content(errorScreenType: QrCodeErrorScreenType) {
    when (errorScreenType) {
        QrCodeErrorScreenType.InsecureChannelDetected -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                InsecureChannelDetectedError()
            }
        }
        else -> Unit
    }
}

@Composable
private fun Buttons(onRetry: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.screen_qr_code_login_start_over_button),
        onClick = onRetry
    )
}

@PreviewsDayNight
@Composable
internal fun QrCodeErrorViewPreview(@PreviewParameter(QrCodeErrorTypeProvider::class) errorScreenType: QrCodeErrorScreenType) {
    ElementPreview {
        QrCodeErrorView(
            errorScreenType = errorScreenType,
            onRetry = {}
        )
    }
}

class QrCodeErrorTypeProvider : PreviewParameterProvider<QrCodeErrorScreenType> {
    override val values: Sequence<QrCodeErrorScreenType> = sequenceOf(
        QrCodeErrorScreenType.Cancelled,
        QrCodeErrorScreenType.Declined,
        QrCodeErrorScreenType.Expired,
        QrCodeErrorScreenType.ProtocolNotSupported,
        QrCodeErrorScreenType.InsecureChannelDetected,
        QrCodeErrorScreenType.UnknownError
    )
}
