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

class AuthorityNormalizerTest {
    @Test
    fun test_normalize() {
        val normalized1 = AuthorityNormalizer().normalize(
            Authority.parse("userinfoABC@EXAMPLE.com:80"), Charsets.UTF8, "http"
        )
        assertEquals("userinfoABC", normalized1!!.userinfo)
        assertEquals(HostType.REGNAME, normalized1.host!!.type)
        assertEquals("example.com", normalized1.host.value)
        assertEquals(-1, normalized1.port.toLong())
        assertEquals("userinfoABC@example.com", normalized1.toString())

        val normalized2 = AuthorityNormalizer().normalize(
            Authority.parse("userinfoABC@EXAMPLE.com:443"), Charsets.UTF8, "https"
        )
        assertEquals("userinfoABC", normalized2!!.userinfo)
        assertEquals(HostType.REGNAME, normalized2.host!!.type)
        assertEquals("example.com", normalized2.host.value)
        assertEquals(443, normalized2.port.toLong())
        assertEquals("userinfoABC@example.com:443", normalized2.toString())

        val normalized3 = AuthorityNormalizer().normalize(null, Charsets.UTF8, "http")
        assertEquals(null, normalized3)
    }
}
