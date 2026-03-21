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
package org.czeal.urireference

import com.fleeksoft.charset.Charsets
import kotlin.test.Test
import kotlin.test.assertEquals


class QueryNormalizerTest {
    @Test
    fun test_normalize() {
        assertEquals("k1=v1&k2=v2", QueryNormalizer().normalize("k1=v1&k2=v2", Charsets.UTF8))
        assertEquals("K1=V1&K2=V2", QueryNormalizer().normalize("K1=V1&K2=V2", Charsets.UTF8))
        assertEquals("", QueryNormalizer().normalize("", Charsets.UTF8))
        assertEquals(null as String?, QueryNormalizer().normalize(null, Charsets.UTF8))
    }
}
