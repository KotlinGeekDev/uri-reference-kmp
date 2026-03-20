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
import org.czeal.urireference.TestUtils.assertThrowsNPE
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.ThrowingSupplier


class HostTest {
    @Test
    fun test_parse() {
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("example.com") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("101.102.103.104") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("[2001:db8:0:1:1:1:1:1]") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("[2001:0:9d38:6abd:0:0:0:42]") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("[fe80::1]") })
        Assertions.assertDoesNotThrow<Host> { Host.parse("[2001:0:3238:DFE1:63::FEFB]") }
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("[v1.fe80::a+en1]") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse("") })
        Assertions.assertDoesNotThrow<Host>(ThrowingSupplier { Host.parse(null) })

        assertThrowsIAE<Throwable>(
            "The host value \"例子.测试\" has an invalid character \"例\" at the index 0.",
            { Authority.parse("例子.测试") })

        assertThrowsIAE<Throwable>(
            "The host value \"%XX\" has an invalid hex digit \"X\" at the index 1.",
            { Authority.parse("%XX") })
    }


    @Test
    fun test_getType() {
        Assertions.assertEquals(HostType.REGNAME, Host.parse("example.com").type)
        Assertions.assertEquals(HostType.IPV4, Host.parse("101.102.103.104").type)
        Assertions.assertEquals(HostType.IPV6, Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").type)
        Assertions.assertEquals(HostType.IPV6, Host.parse("[2001:db8:0:1:1:1:1:1]").type)
        Assertions.assertEquals(HostType.IPV6, Host.parse("[2001:0:9d38:6abd:0:0:0:42]").type)
        Assertions.assertEquals(HostType.IPV6, Host.parse("[fe80::1]").type)
        Assertions.assertEquals(HostType.IPV6, Host.parse("[2001:0:3238:DFE1:63::FEFB]").type)
        Assertions.assertEquals(HostType.IPVFUTURE, Host.parse("[v1.fe80::a+en1]").type)
        Assertions.assertEquals(HostType.REGNAME, Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").type)
        Assertions.assertEquals(HostType.REGNAME, Host.parse("").type)
        Assertions.assertEquals(HostType.REGNAME, Host.parse(null).type)
    }


    @Test
    fun test_getValue() {
        Assertions.assertEquals("example.com", Host.parse("example.com").value)
        Assertions.assertEquals("101.102.103.104", Host.parse("101.102.103.104").value)
        Assertions.assertEquals(
            "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]",
            Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").value
        )
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", Host.parse("[2001:db8:0:1:1:1:1:1]").value)
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", Host.parse("[2001:0:9d38:6abd:0:0:0:42]").value)
        Assertions.assertEquals("[fe80::1]", Host.parse("[fe80::1]").value)
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", Host.parse("[2001:0:3238:DFE1:63::FEFB]").value)
        Assertions.assertEquals("[v1.fe80::a+en1]", Host.parse("[v1.fe80::a+en1]").value)
        Assertions.assertEquals(
            "%65%78%61%6D%70%6C%65%2E%63%6F%6D",
            Host.parse("%65%78%61%6D%70%6C%65%2E%63%6F%6D").value
        )
        Assertions.assertEquals("", Host.parse("").value)
        Assertions.assertEquals(null as String?, Host.parse(null).value)
    }


    @Test
    fun test_equals() {
        Assertions.assertFalse(Host.parse("FB").equals(Host.parse("Ea")))
    }


    @Test
    fun test_compareTo() {
        Assertions.assertEquals(0, Host.parse("example.com").compareTo(Host.parse("example.com")))

        Assertions.assertTrue(Host.parse("127.0.0.1").compareTo(Host.parse("example.com")) < 0)

        assertThrowsNPE<Throwable>("A null value is not comparable.", { Host.parse("example.com").compareTo(null) })
    }
}
