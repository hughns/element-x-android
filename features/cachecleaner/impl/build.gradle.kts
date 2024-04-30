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

plugins {
    id("io.element.android-compose-library")
    alias(libs.plugins.anvil)
}

android {
    namespace = "io.element.android.features.cachecleaner.impl"
}

anvil {
    generateDaggerFactories.set(true)
}

dependencies {
    // DI
    implementation(projects.libraries.di)
    implementation(libs.dagger)
    anvil(projects.anvilcodegen)

    api(projects.features.cachecleaner.api)
    implementation(projects.libraries.core)
    implementation(libs.coroutines.core)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.test.truth)
    testImplementation(projects.tests.testutils)
}
