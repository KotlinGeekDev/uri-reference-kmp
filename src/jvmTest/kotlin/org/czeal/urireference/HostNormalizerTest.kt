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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class HostNormalizerTest {
    @Test
    fun test_normalize() {
        val normalized1 = HostNormalizer().normalize(Host(HostType.REGNAME, "HOst"), StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, normalized1.type)
        Assertions.assertEquals("host", normalized1.value)

        val normalized2 = HostNormalizer().normalize(Host(HostType.REGNAME, "hos%74"), StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, normalized2.type)
        Assertions.assertEquals("host", normalized2.value)

        val normalized3 = HostNormalizer().normalize(Host(HostType.REGNAME, "1%2E1%2E1%2E1"), StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV4, normalized3.type)
        Assertions.assertEquals("1.1.1.1", normalized3.value)

        val normalized4 = HostNormalizer().normalize(
            Host(
                HostType.REGNAME,
                "[%32%30%30%31:%30%64%62%38:%38%35%61%33:%30%30%30%30:%30%30%30%30:%38%61%32%65:%30%33%37%30:%37%33%33%34]"
            ), StandardCharsets.UTF_8
        )
        Assertions.assertEquals(HostType.IPV6, normalized4.type)
        Assertions.assertEquals("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", normalized4.value)

        val normalized5 = HostNormalizer().normalize(
            Host(HostType.REGNAME, "[%76%31.%66%65%38%30::%61+%65%6E%31]"),
            StandardCharsets.UTF_8
        )
        Assertions.assertEquals(HostType.IPVFUTURE, normalized5.type)
        Assertions.assertEquals("[v1.fe80::a+en1]", normalized5.value)

        val normalized6 = HostNormalizer().normalize(Host(HostType.REGNAME, ""), StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, normalized6.type)
        Assertions.assertEquals("", normalized6.value)

        val normalized7 = HostNormalizer().normalize(Host(HostType.REGNAME, null), StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, normalized7.type)
        Assertions.assertEquals(null, normalized7.value)
    }
}
