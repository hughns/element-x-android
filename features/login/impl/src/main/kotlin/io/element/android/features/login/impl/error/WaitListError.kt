/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package io.element.android.features.login.impl.error

import io.element.android.libraries.core.bool.orFalse

fun Throwable.isWaitListError(): Boolean {
    return message?.contains("IO_ELEMENT_X_WAIT_LIST").orFalse()
}
