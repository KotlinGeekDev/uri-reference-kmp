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


class IpvFutureValidatorTest {
    @Test
    fun test_validate() {
        assertDoesNotThrow<Unit> { IpvFutureValidator().validate("v1.fe80::a+en1") }

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"[]\" is invalid because the content enclosed by brackets does not form a valid IPvFuture address as it is empty.",
            { IpvFutureValidator().validate("") })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"[v1]\" is invalid because the content enclosed by brackets does not form a valid IPvFuture address due to missing periods.",
            { IpvFutureValidator().validate("v1") })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"[v.fe80::a+en1]\" is invalid because the content enclosed by brackets does not form a valid IPvFuture address due to missing its version.",
            { IpvFutureValidator().validate("v.fe80::a+en1") })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"[v.fe80::a+en1]\" is invalid because the content enclosed by brackets does not form a valid IPvFuture address due to missing its version.",
            { IpvFutureValidator().validate("v.fe80::a+en1") })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"[v1.]\" is invalid because the content enclosed by brackets does not form a valid IPvFuture address as there is no content following the first period.",
            { IpvFutureValidator().validate("v1.") })

        TestUtils.assertThrowsIAE<Throwable>(
            "The host value \"[v1./fe80::a+en1]\" is invalid because the content enclosed by brackets does not form a valid IPvFuture address due to the segment after the first period \"/fe80::a+en1\", containing an invalid character \"/\" at the index 0.",
            { IpvFutureValidator().validate("v1./fe80::a+en1") })
    }
}
