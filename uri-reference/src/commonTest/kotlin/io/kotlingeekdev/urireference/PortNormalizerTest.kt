/*
 * Copyright (C) 2024-2025 Hideki Ikeda
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
package io.kotlingeekdev.urireference

import kotlin.test.Test
import kotlin.test.assertEquals


class PortNormalizerTest {
    @Test
    fun test_normalize() {
        assertEquals(-1, PortNormalizer().normalize(80, "http"))
        assertEquals(80, PortNormalizer().normalize(80, "custom"))
        assertEquals(443, PortNormalizer().normalize(443, "https"))
    }
}
