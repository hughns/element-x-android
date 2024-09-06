/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package io.element.android.features.preferences.impl.analytics

import androidx.compose.runtime.Composable
import io.element.android.features.analytics.api.preferences.AnalyticsPreferencesPresenter
import io.element.android.libraries.architecture.Presenter
import javax.inject.Inject

class AnalyticsSettingsPresenter @Inject constructor(
    private val analyticsPresenter: AnalyticsPreferencesPresenter,
) : Presenter<AnalyticsSettingsState> {
    @Composable
    override fun present(): AnalyticsSettingsState {
        val analyticsState = analyticsPresenter.present()

        return AnalyticsSettingsState(
            analyticsState = analyticsState,
        )
    }
}
