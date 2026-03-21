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

import org.czeal.urireference.Authority.Companion.parse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class AuthorityTest {
    @Test
    fun test_parse() {
        assertDoesNotThrow<Authority?> { parse("example.com") }
        assertDoesNotThrow<Authority?> { parse("john@example.com:80") }
        assertDoesNotThrow<Authority?> { parse("example.com:001") }
        assertDoesNotThrow<Authority?> { parse("%6A%6F%68%6E@example.com") }
        assertDoesNotThrow<Authority?> { parse("101.102.103.104") }
        assertDoesNotThrow<Authority?> { parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]") }
        assertDoesNotThrow<Authority?> { parse("[2001:db8:0:1:1:1:1:1]") }
        assertDoesNotThrow<Authority?> { parse("[2001:0:9d38:6abd:0:0:0:42]") }
        assertDoesNotThrow<Authority?> { parse("[fe80::1]") }
        assertDoesNotThrow<Authority?> { parse("[2001:0:3238:DFE1:63::FEFB]") }
        assertDoesNotThrow<Authority?> { parse("[v1.fe80::a+en1]") }
        assertDoesNotThrow<Authority?>{ parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D") }
        assertDoesNotThrow<Authority?> { parse("") }
        assertDoesNotThrow<Authority?> { parse(null) }

        TestUtils.assertThrowsIAE<Throwable?>(
            "The port value \"password@example.com\" has an invalid character \"p\" at the index 0.",
             { parse("user@name:password@example.com") })

        TestUtils.assertThrowsIAE<Throwable?>(
            "The userinfo value \"müller\" has an invalid character \"ü\" at the index 1.",
             { parse("müller@example.com") })

        TestUtils.assertThrowsIAE<Throwable?>(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
             { parse("%XX") })
    }


    @Test
    fun test_getUserInfo() {
        assertEquals(null, parse("example.com")!!.userinfo)
        assertEquals("john", parse("john@example.com:80")!!.userinfo)
        assertEquals(null, parse("example.com:001")!!.userinfo)
        assertEquals("%6A%6F%68%6E", parse("%6A%6F%68%6E@example.com")!!.userinfo)
        assertEquals(null, parse("101.102.103.104")!!.userinfo)
        assertEquals(null, parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")!!.userinfo)
        assertEquals(null, parse("[2001:db8:0:1:1:1:1:1]")!!.userinfo)
        assertEquals(null, parse("[2001:0:9d38:6abd:0:0:0:42]")!!.userinfo)
        assertEquals(null, parse("[fe80::1]")!!.userinfo)
        assertEquals(null, parse("[2001:0:3238:DFE1:63::FEFB]")!!.userinfo)
        assertEquals(null, parse("[v1.fe80::a+en1]")!!.userinfo)
        assertEquals(null, parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D")!!.userinfo)
        assertEquals(null, parse("")!!.userinfo)
    }


    @Test
    fun test_getHost() {
        assertEquals(Host(HostType.REGNAME, "example.com"), parse("example.com")!!.host)
        assertEquals(Host(HostType.REGNAME, "example.com"), parse("john@example.com:80")!!.host)
        assertEquals(Host(HostType.REGNAME, "example.com"), parse("example.com:001")!!.host)
        assertEquals(Host(HostType.REGNAME, "example.com"), parse("%6A%6F%68%6E@example.com")!!.host)
        assertEquals(Host(HostType.IPV4, "101.102.103.104"), parse("101.102.103.104")!!.host)
        assertEquals(
            Host(HostType.IPV6, "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"),
            parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")!!.host
        )
        assertEquals(Host(HostType.IPV6, "[2001:db8:0:1:1:1:1:1]"), parse("[2001:db8:0:1:1:1:1:1]")!!.host)
        assertEquals(
            Host(HostType.IPV6, "[2001:0:9d38:6abd:0:0:0:42]"),
            parse("[2001:0:9d38:6abd:0:0:0:42]")!!.host
        )
        assertEquals(Host(HostType.IPV6, "[fe80::1]"), parse("[fe80::1]")!!.host)
        assertEquals(
            Host(HostType.IPV6, "[2001:0:3238:DFE1:63::FEFB]"),
            parse("[2001:0:3238:DFE1:63::FEFB]")!!.host
        )
        assertEquals(Host(HostType.IPVFUTURE, "[v1.fe80::a+en1]"), parse("[v1.fe80::a+en1]")!!.host)
        assertEquals(
            Host(HostType.REGNAME, "%65%78%61%6D%70%6C%65%2E%63%6F%6D"),
            parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D")!!.host
        )
        assertEquals(Host(HostType.REGNAME, ""), parse("")!!.host)
    }


    @Test
    fun test_getPort() {
        assertEquals(-1, parse("example.com")!!.port)
        assertEquals(80, parse("john@example.com:80")!!.port)
        assertEquals(1, parse("example.com:001")!!.port)
        assertEquals(-1, parse("%6A%6F%68%6E@example.com")!!.port)
        assertEquals(-1, parse("101.102.103.104")!!.port)
        assertEquals(-1, parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")!!.port)
        assertEquals(-1, parse("[2001:db8:0:1:1:1:1:1]")!!.port)
        assertEquals(-1, parse("[2001:0:9d38:6abd:0:0:0:42]")!!.port)
        assertEquals(-1, parse("[fe80::1]")!!.port)
        assertEquals(-1, parse("[2001:0:3238:DFE1:63::FEFB]")!!.port)
        assertEquals(-1, parse("[v1.fe80::a+en1]")!!.port)
        assertEquals(-1, parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D")!!.port)
        assertEquals(-1, parse("")!!.port)
    }


    @Test
    fun test_toString() {
        assertEquals("example.com", parse("example.com").toString())
        assertEquals("john@example.com:80", parse("john@example.com:80").toString())
        assertEquals("example.com:1", parse("example.com:001").toString())
        assertEquals("%6A%6F%68%6E@example.com", parse("%6A%6F%68%6E@example.com").toString())
        assertEquals("101.102.103.104", parse("101.102.103.104").toString())
        assertEquals(
            "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]",
            parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").toString()
        )
        assertEquals("[2001:db8:0:1:1:1:1:1]", parse("[2001:db8:0:1:1:1:1:1]").toString())
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", parse("[2001:0:9d38:6abd:0:0:0:42]").toString())
        assertEquals("[fe80::1]", parse("[fe80::1]").toString())
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", parse("[2001:0:3238:DFE1:63::FEFB]").toString())
        assertEquals("[v1.fe80::a+en1]", parse("[v1.fe80::a+en1]").toString())
        assertEquals(
            "%65%78%61%6D%70%6C%65%2E%63%6F%6D",
            parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").toString()
        )
        assertEquals("", parse("").toString())
    }


    @Test
    fun test_equals() {
        assertFalse(parse("FB")!!.equals(parse("Ea")))
    }


    @Test
    fun test_compareTo() {
        assertEquals(0, parse("example.com")!!.compareTo(parse("example.com")))

        assertTrue(parse("127.0.0.1")!!.compareTo(parse("example.com")) < 0)

        TestUtils.assertThrowsNPE<Throwable?>(
            "A null value is not comparable.",
             { parse("example.com")!!.compareTo(null) })
    }
}
