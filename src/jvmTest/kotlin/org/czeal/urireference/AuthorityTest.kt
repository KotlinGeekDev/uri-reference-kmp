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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.ThrowingSupplier


class AuthorityTest {
    @Test
    fun test_parse() {
        Assertions.assertDoesNotThrow<Authority?> { parse("example.com") }
        Assertions.assertDoesNotThrow<Authority?> { parse("john@example.com:80") }
        Assertions.assertDoesNotThrow<Authority?> { parse("example.com:001") }
        Assertions.assertDoesNotThrow<Authority?> { parse("%6A%6F%68%6E@example.com") }
        Assertions.assertDoesNotThrow<Authority?> { parse("101.102.103.104") }
        Assertions.assertDoesNotThrow<Authority?> { parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]") }
        Assertions.assertDoesNotThrow<Authority?> { parse("[2001:db8:0:1:1:1:1:1]") }
        Assertions.assertDoesNotThrow<Authority?> { parse("[2001:0:9d38:6abd:0:0:0:42]") }
        Assertions.assertDoesNotThrow<Authority?> { parse("[fe80::1]") }
        Assertions.assertDoesNotThrow<Authority?> { parse("[2001:0:3238:DFE1:63::FEFB]") }
        Assertions.assertDoesNotThrow<Authority?>(ThrowingSupplier { parse("[v1.fe80::a+en1]") })
        Assertions.assertDoesNotThrow<Authority?>(ThrowingSupplier { parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D") })
        Assertions.assertDoesNotThrow<Authority?>(ThrowingSupplier { parse("") })
        Assertions.assertDoesNotThrow<Authority?>(ThrowingSupplier { parse(null) })

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
        Assertions.assertEquals(null, parse("example.com")!!.userinfo)
        Assertions.assertEquals("john", parse("john@example.com:80")!!.userinfo)
        Assertions.assertEquals(null, parse("example.com:001")!!.userinfo)
        Assertions.assertEquals("%6A%6F%68%6E", parse("%6A%6F%68%6E@example.com")!!.userinfo)
        Assertions.assertEquals(null, parse("101.102.103.104")!!.userinfo)
        Assertions.assertEquals(null, parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")!!.userinfo)
        Assertions.assertEquals(null, parse("[2001:db8:0:1:1:1:1:1]")!!.userinfo)
        Assertions.assertEquals(null, parse("[2001:0:9d38:6abd:0:0:0:42]")!!.userinfo)
        Assertions.assertEquals(null, parse("[fe80::1]")!!.userinfo)
        Assertions.assertEquals(null, parse("[2001:0:3238:DFE1:63::FEFB]")!!.userinfo)
        Assertions.assertEquals(null, parse("[v1.fe80::a+en1]")!!.userinfo)
        Assertions.assertEquals(null, parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D")!!.userinfo)
        Assertions.assertEquals(null, parse("")!!.userinfo)
    }


    @Test
    fun test_getHost() {
        Assertions.assertEquals(Host(HostType.REGNAME, "example.com"), parse("example.com")!!.host)
        Assertions.assertEquals(Host(HostType.REGNAME, "example.com"), parse("john@example.com:80")!!.host)
        Assertions.assertEquals(Host(HostType.REGNAME, "example.com"), parse("example.com:001")!!.host)
        Assertions.assertEquals(Host(HostType.REGNAME, "example.com"), parse("%6A%6F%68%6E@example.com")!!.host)
        Assertions.assertEquals(Host(HostType.IPV4, "101.102.103.104"), parse("101.102.103.104")!!.host)
        Assertions.assertEquals(
            Host(HostType.IPV6, "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"),
            parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")!!.host
        )
        Assertions.assertEquals(Host(HostType.IPV6, "[2001:db8:0:1:1:1:1:1]"), parse("[2001:db8:0:1:1:1:1:1]")!!.host)
        Assertions.assertEquals(
            Host(HostType.IPV6, "[2001:0:9d38:6abd:0:0:0:42]"),
            parse("[2001:0:9d38:6abd:0:0:0:42]")!!.host
        )
        Assertions.assertEquals(Host(HostType.IPV6, "[fe80::1]"), parse("[fe80::1]")!!.host)
        Assertions.assertEquals(
            Host(HostType.IPV6, "[2001:0:3238:DFE1:63::FEFB]"),
            parse("[2001:0:3238:DFE1:63::FEFB]")!!.host
        )
        Assertions.assertEquals(Host(HostType.IPVFUTURE, "[v1.fe80::a+en1]"), parse("[v1.fe80::a+en1]")!!.host)
        Assertions.assertEquals(
            Host(HostType.REGNAME, "%65%78%61%6D%70%6C%65%2E%63%6F%6D"),
            parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D")!!.host
        )
        Assertions.assertEquals(Host(HostType.REGNAME, ""), parse("")!!.host)
    }


    @Test
    fun test_getPort() {
        Assertions.assertEquals(-1, parse("example.com")!!.port)
        Assertions.assertEquals(80, parse("john@example.com:80")!!.port)
        Assertions.assertEquals(1, parse("example.com:001")!!.port)
        Assertions.assertEquals(-1, parse("%6A%6F%68%6E@example.com")!!.port)
        Assertions.assertEquals(-1, parse("101.102.103.104")!!.port)
        Assertions.assertEquals(-1, parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")!!.port)
        Assertions.assertEquals(-1, parse("[2001:db8:0:1:1:1:1:1]")!!.port)
        Assertions.assertEquals(-1, parse("[2001:0:9d38:6abd:0:0:0:42]")!!.port)
        Assertions.assertEquals(-1, parse("[fe80::1]")!!.port)
        Assertions.assertEquals(-1, parse("[2001:0:3238:DFE1:63::FEFB]")!!.port)
        Assertions.assertEquals(-1, parse("[v1.fe80::a+en1]")!!.port)
        Assertions.assertEquals(-1, parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D")!!.port)
        Assertions.assertEquals(-1, parse("")!!.port)
    }


    @Test
    fun test_toString() {
        Assertions.assertEquals("example.com", parse("example.com").toString())
        Assertions.assertEquals("john@example.com:80", parse("john@example.com:80").toString())
        Assertions.assertEquals("example.com:1", parse("example.com:001").toString())
        Assertions.assertEquals("%6A%6F%68%6E@example.com", parse("%6A%6F%68%6E@example.com").toString())
        Assertions.assertEquals("101.102.103.104", parse("101.102.103.104").toString())
        Assertions.assertEquals(
            "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]",
            parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").toString()
        )
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", parse("[2001:db8:0:1:1:1:1:1]").toString())
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", parse("[2001:0:9d38:6abd:0:0:0:42]").toString())
        Assertions.assertEquals("[fe80::1]", parse("[fe80::1]").toString())
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", parse("[2001:0:3238:DFE1:63::FEFB]").toString())
        Assertions.assertEquals("[v1.fe80::a+en1]", parse("[v1.fe80::a+en1]").toString())
        Assertions.assertEquals(
            "%65%78%61%6D%70%6C%65%2E%63%6F%6D",
            parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").toString()
        )
        Assertions.assertEquals("", parse("").toString())
    }


    @Test
    fun test_equals() {
        Assertions.assertFalse(parse("FB")!!.equals(parse("Ea")))
    }


    @Test
    fun test_compareTo() {
        Assertions.assertEquals(0, parse("example.com")!!.compareTo(parse("example.com")))

        Assertions.assertTrue(parse("127.0.0.1")!!.compareTo(parse("example.com")) < 0)

        TestUtils.assertThrowsNPE<Throwable?>(
            "A null value is not comparable.",
             { parse("example.com")!!.compareTo(null) })
    }
}
