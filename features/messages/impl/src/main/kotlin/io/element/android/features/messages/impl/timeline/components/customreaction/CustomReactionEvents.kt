/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package io.element.android.features.messages.impl.timeline.components.customreaction

import io.element.android.features.messages.impl.timeline.model.TimelineItem

sealed interface CustomReactionEvents {
    data class ShowCustomReactionSheet(val event: TimelineItem.Event) : CustomReactionEvents
    data object DismissCustomReactionSheet : CustomReactionEvents
}
