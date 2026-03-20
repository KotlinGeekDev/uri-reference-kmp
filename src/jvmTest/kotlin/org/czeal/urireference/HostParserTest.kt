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

import org.czeal.urireference.TestUtils.assertThrowsIAE
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class HostParserTest {
    @Test
    fun test_parse() {
        val host1 = HostParser().parse("example.com", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, host1.type)
        Assertions.assertEquals("example.com", host1.value)

        val host2 = HostParser().parse("101.102.103.104", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV4, host2.type)
        Assertions.assertEquals("101.102.103.104", host2.value)

        val host3 = HostParser().parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV6, host3.type)
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host3.value)

        val host4 = HostParser().parse("[2001:db8:0:1:1:1:1:1]", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV6, host4.type)
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", host4.value)

        val host5 = HostParser().parse("[2001:0:9d38:6abd:0:0:0:42]", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV6, host5.type)
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host5.value)

        val host6 = HostParser().parse("[fe80::1]", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV6, host6.type)
        Assertions.assertEquals("[fe80::1]", host6.value)

        val host7 = HostParser().parse("[2001:0:3238:DFE1:63::FEFB]", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPV6, host7.type)
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", host7.value)

        val host8 = HostParser().parse("[v1.fe80::a+en1]", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.IPVFUTURE, host8.type)
        Assertions.assertEquals("[v1.fe80::a+en1]", host8.value)

        val host9 = HostParser().parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, host9.type)
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host9.value)

        val host10 = HostParser().parse("", StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, host10.type)
        Assertions.assertEquals("", host10.value)

        val host11 = HostParser().parse(null, StandardCharsets.UTF_8)
        Assertions.assertEquals(HostType.REGNAME, host11.type)
        Assertions.assertEquals(null, host11.value)

        assertThrowsIAE<Throwable>(
            "The host value \"例子.测试\" has an invalid character \"例\" at the index 0.",
            { HostParser().parse("例子.测试", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            { HostParser().parse("%XX", StandardCharsets.UTF_8) })
    }
}
