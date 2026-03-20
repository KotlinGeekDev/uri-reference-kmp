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


class AuthorityBuilderTest {
    @Test
    fun test_build() {
        val authority1 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setUserinfo("john").setHost("example.com").setPort(80).build()
        Assertions.assertEquals("john", authority1.userinfo)
        Assertions.assertEquals(HostType.REGNAME, authority1.host!!.type)
        Assertions.assertEquals("example.com", authority1.host.value)
        Assertions.assertEquals(80, authority1.port)
        Assertions.assertEquals("john@example.com:80", authority1.toString())

        val authority2 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setHost("example.com").setPort(80).build()
        Assertions.assertEquals(null, authority2.userinfo)
        Assertions.assertEquals(HostType.REGNAME, authority2.host!!.type)
        Assertions.assertEquals("example.com", authority2.host.value)
        Assertions.assertEquals(80, authority2.port)
        Assertions.assertEquals("example.com:80", authority2.toString())

        val authority3 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setPort(80).build()
        Assertions.assertEquals(null, authority3.userinfo)
        Assertions.assertEquals(HostType.REGNAME, authority3.host!!.type)
        Assertions.assertEquals(null, authority3.host.value)
        Assertions.assertEquals(80, authority3.port)
        Assertions.assertEquals(":80", authority3.toString())

        val authority4 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).build()
        Assertions.assertEquals(null, authority4.userinfo)
        Assertions.assertEquals(HostType.REGNAME, authority3.host.type)
        Assertions.assertEquals(null, authority3.host.value)
        Assertions.assertEquals(-1, authority4.port)
        Assertions.assertEquals("", authority4.toString())

        val authority5 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setUserinfo("john").setHost("101.102.103.104").setPort(80).build()
        Assertions.assertEquals("john", authority5.userinfo)
        Assertions.assertEquals(HostType.IPV4, authority5.host!!.type)
        Assertions.assertEquals("101.102.103.104", authority5.host.value)
        Assertions.assertEquals(80, authority5.port)
        Assertions.assertEquals("john@101.102.103.104:80", authority5.toString())

        val authority6 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setUserinfo("john").setHost("101.102.103.104").setPort(80).build()
        Assertions.assertEquals("john", authority6.userinfo)
        Assertions.assertEquals(HostType.IPV4, authority6.host!!.type)
        Assertions.assertEquals("101.102.103.104", authority6.host.value)
        Assertions.assertEquals(80, authority6.port)
        Assertions.assertEquals("john@101.102.103.104:80", authority6.toString())

        val authority7 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setUserinfo("john").setHost("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")
            .setPort(80).build()
        Assertions.assertEquals("john", authority7.userinfo)
        Assertions.assertEquals(HostType.IPV6, authority7.host!!.type)
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority7.host.value)
        Assertions.assertEquals(80, authority7.port)
        Assertions.assertEquals("john@[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]:80", authority7.toString())

        val authority8 = AuthorityBuilder()
            .setCharset(StandardCharsets.UTF_8).setUserinfo("john").setHost("[v1.fe80::a+en1]").setPort(80).build()
        Assertions.assertEquals("john", authority8.userinfo)
        Assertions.assertEquals(HostType.IPVFUTURE, authority8.host!!.type)
        Assertions.assertEquals("[v1.fe80::a+en1]", authority8.host.value)
        Assertions.assertEquals(80, authority8.port)
        Assertions.assertEquals("john@[v1.fe80::a+en1]:80", authority8.toString())

        TestUtils.assertThrowsIAE<IllegalArgumentException>(
            "The userinfo value \"?\" has an invalid character \"?\" at the index 0."
        ) { AuthorityBuilder().setCharset(StandardCharsets.UTF_8).setUserinfo("?").build() }

        TestUtils.assertThrowsIAE<IllegalArgumentException>(
            "The userinfo value \"%XX\" has an invalid hex digit \"X\" at the index 1."
        ) { AuthorityBuilder().setCharset(StandardCharsets.UTF_8).setUserinfo("%XX").build() }
    }
}
