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
import org.czeal.urireference.Authority.Companion.parse
import kotlin.test.Test
import kotlin.test.assertEquals


class AuthorityParserTest {
    @Test
    fun test_parse() {
        val authority1 = AuthorityParser().parse("example.com", Charsets.UTF8)
        assertEquals(null, authority1!!.userinfo)
        assertEquals(HostType.REGNAME, authority1.host!!.type)
        assertEquals("example.com", authority1.host.value)
        assertEquals(-1, authority1.port)
        assertEquals("example.com", authority1.toString())

        val authority2 = AuthorityParser().parse("john@example.com:80", Charsets.UTF8)
        assertEquals("john", authority2!!.userinfo)
        assertEquals(HostType.REGNAME, authority2.host!!.type)
        assertEquals("example.com", authority2.host.value)
        assertEquals(80, authority2.port)
        assertEquals("john@example.com:80", authority2.toString())

        val authority3 = AuthorityParser().parse("example.com:001", Charsets.UTF8)
        assertEquals(null, authority3!!.userinfo)
        assertEquals(HostType.REGNAME, authority3.host!!.type)
        assertEquals("example.com", authority3.host.value)
        assertEquals(1, authority3.port)
        assertEquals("example.com:1", authority3.toString())

        val authority4 = AuthorityParser().parse("%6A%6F%68%6E@example.com", Charsets.UTF8)
        assertEquals("%6A%6F%68%6E", authority4!!.userinfo)
        assertEquals(HostType.REGNAME, authority4.host!!.type)
        assertEquals("example.com", authority4.host.value)
        assertEquals(-1, authority4.port)
        assertEquals("%6A%6F%68%6E@example.com", authority4.toString())

        val authority5 = AuthorityParser().parse("101.102.103.104", Charsets.UTF8)
        assertEquals(null, authority5!!.userinfo)
        assertEquals(HostType.IPV4, authority5.host!!.type)
        assertEquals("101.102.103.104", authority5.host.value)
        assertEquals(-1, authority5.port)
        assertEquals("101.102.103.104", authority5.toString())

        val authority6 = AuthorityParser().parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", Charsets.UTF8)
        assertEquals(null, authority6!!.userinfo)
        assertEquals(HostType.IPV6, authority6.host!!.type)
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority6.host.value)
        assertEquals(-1, authority6.port)
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority6.toString())

        val authority7 = AuthorityParser().parse("[2001:db8:0:1:1:1:1:1]", Charsets.UTF8)
        assertEquals(null, authority7!!.userinfo)
        assertEquals(HostType.IPV6, authority7.host!!.type)
        assertEquals("[2001:db8:0:1:1:1:1:1]", authority7.host.value)
        assertEquals(-1, authority7.port)
        assertEquals("[2001:db8:0:1:1:1:1:1]", authority7.toString())

        val authority8 = AuthorityParser().parse("[2001:db8:0:1:1:1:1:1]", Charsets.UTF8)
        assertEquals(null, authority8!!.userinfo)
        assertEquals(HostType.IPV6, authority8.host!!.type)
        assertEquals("[2001:db8:0:1:1:1:1:1]", authority8.host.value)
        assertEquals(-1, authority8.port)
        assertEquals("[2001:db8:0:1:1:1:1:1]", authority8.toString())

        val authority9 = AuthorityParser().parse("[2001:0:9d38:6abd:0:0:0:42]", Charsets.UTF8)
        assertEquals(null, authority9!!.userinfo)
        assertEquals(HostType.IPV6, authority9.host!!.type)
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", authority9.host.value)
        assertEquals(-1, authority9.port)
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", authority9.toString())

        val authority10 = AuthorityParser().parse("[fe80::1]", Charsets.UTF8)
        assertEquals(null, authority10!!.userinfo)
        assertEquals(HostType.IPV6, authority10.host!!.type)
        assertEquals("[fe80::1]", authority10.host.value)
        assertEquals(-1, authority10.port)
        assertEquals("[fe80::1]", authority10.toString())

        val authority11 = AuthorityParser().parse("[2001:0:3238:DFE1:63::FEFB]", Charsets.UTF8)
        assertEquals(null, authority11!!.userinfo)
        assertEquals(HostType.IPV6, authority11.host!!.type)
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", authority11.host.value)
        assertEquals(-1, authority11.port)
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", authority11.toString())

        val authority12 = AuthorityParser().parse("[v1.fe80::a+en1]", Charsets.UTF8)
        assertEquals(null, authority12!!.userinfo)
        assertEquals(HostType.IPVFUTURE, authority12.host!!.type)
        assertEquals("[v1.fe80::a+en1]", authority12.host.value)
        assertEquals(-1, authority12.port)
        assertEquals("[v1.fe80::a+en1]", authority12.toString())

        val authority13 = AuthorityParser().parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D", Charsets.UTF8)
        assertEquals(null, authority13!!.userinfo)
        assertEquals(HostType.REGNAME, authority13.host!!.type)
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", authority13.host.value)
        assertEquals(-1, authority13.port)
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", authority13.toString())

        val authority14 = AuthorityParser().parse("", Charsets.UTF8)
        assertEquals(null, authority14!!.userinfo)
        assertEquals(HostType.REGNAME, authority14.host!!.type)
        assertEquals("", authority14.host.value)
        assertEquals(-1, authority14.port)
        assertEquals("", authority14.toString())

        TestUtils.assertThrowsIAE<Throwable>(
            "The port value \"password@example.com\" has an invalid character \"p\" at the index 0.",
             { parse("user@name:password@example.com", Charsets.UTF8) })

        TestUtils.assertThrowsIAE<Throwable>(
            "The userinfo value \"müller\" has an invalid character \"ü\" at the index 1.",
             { parse("müller@example.com", Charsets.UTF8) })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
             { parse("%XX", Charsets.UTF8) })
    }
}
