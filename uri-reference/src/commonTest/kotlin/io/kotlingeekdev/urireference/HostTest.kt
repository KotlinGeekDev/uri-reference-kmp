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
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class HostTest {
    @Test
    fun test_parse() {
        assertDoesNotThrow<Host>( { Host.parse("example.com") })
        assertDoesNotThrow<Host>( { Host.parse("101.102.103.104") })
        assertDoesNotThrow<Host>( { Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]") })
        assertDoesNotThrow<Host>( { Host.parse("[2001:db8:0:1:1:1:1:1]") })
        assertDoesNotThrow<Host>( { Host.parse("[2001:0:9d38:6abd:0:0:0:42]") })
        assertDoesNotThrow<Host>( { Host.parse("[fe80::1]") })
        assertDoesNotThrow<Host> { Host.parse("[2001:0:3238:DFE1:63::FEFB]") }
        assertDoesNotThrow<Host>( { Host.parse("[v1.fe80::a+en1]") })
        assertDoesNotThrow<Host>( { Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D") })
        assertDoesNotThrow<Host>( { Host.parse("") })
        assertDoesNotThrow<Host>( { Host.parse(null) })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"例子.测试\" has an invalid character \"例\" at the index 0.",
            { Authority.parse("例子.测试") })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            { Authority.parse("%XX") })
    }


    @Test
    fun test_getType() {
        assertEquals(HostType.REGNAME, Host.parse("example.com").type)
        assertEquals(HostType.IPV4, Host.parse("101.102.103.104").type)
        assertEquals(HostType.IPV6, Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").type)
        assertEquals(HostType.IPV6, Host.parse("[2001:db8:0:1:1:1:1:1]").type)
        assertEquals(HostType.IPV6, Host.parse("[2001:0:9d38:6abd:0:0:0:42]").type)
        assertEquals(HostType.IPV6, Host.parse("[fe80::1]").type)
        assertEquals(HostType.IPV6, Host.parse("[2001:0:3238:DFE1:63::FEFB]").type)
        assertEquals(HostType.IPVFUTURE, Host.parse("[v1.fe80::a+en1]").type)
        assertEquals(HostType.REGNAME, Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").type)
        assertEquals(HostType.REGNAME, Host.parse("").type)
        assertEquals(HostType.REGNAME, Host.parse(null).type)
    }


    @Test
    fun test_getValue() {
        assertEquals("example.com", Host.parse("example.com").value)
        assertEquals("101.102.103.104", Host.parse("101.102.103.104").value)
        assertEquals(
            "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]",
            Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").value
        )
        assertEquals("[2001:db8:0:1:1:1:1:1]", Host.parse("[2001:db8:0:1:1:1:1:1]").value)
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", Host.parse("[2001:0:9d38:6abd:0:0:0:42]").value)
        assertEquals("[fe80::1]", Host.parse("[fe80::1]").value)
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", Host.parse("[2001:0:3238:DFE1:63::FEFB]").value)
        assertEquals("[v1.fe80::a+en1]", Host.parse("[v1.fe80::a+en1]").value)
        assertEquals(
            "%65%78%61%6D%70%6C%65%2E%63%6F%6D",
            Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").value
        )
        assertEquals("", Host.parse("").value)
        assertEquals(null as String?, Host.parse(null).value)
    }


    @Test
    fun test_equals() {
        assertFalse(Host.parse("FB").equals(Host.parse("Ea")))
    }


    @Test
    fun test_compareTo() {
        assertEquals(0, Host.parse("example.com").compareTo(Host.parse("example.com")))

        assertTrue(Host.parse("127.0.0.1").compareTo(Host.parse("example.com")) < 0)

        TestUtils.assertThrowsNPE<Throwable>(
            "A null value is not comparable.",
            { Host.parse("example.com").compareTo(null) })
    }
}
