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
import kotlin.test.Test


class Ipv4ValidatorTest {
    @Test
    fun test_validate() {
        Ipv4AddressValidator().validate("192.168.1.1")
        Ipv4AddressValidator().validate("8.8.8.8")

        assertThrowsIAE<Throwable>(
            "The host value \"256.100.0.1\" is invalid as an IPv4 address because the octet \"256\" has an invalid character \"6\" at the index 2.",
            { Ipv4AddressValidator().validate("256.100.0.1") })

        assertThrowsIAE<Throwable>(
            "The host value \"192.168.1\" is invalid as an IPv4 address because the number of octets contained in the host is invalid.",
            { Ipv4AddressValidator().validate("192.168.1") })
    }
}
