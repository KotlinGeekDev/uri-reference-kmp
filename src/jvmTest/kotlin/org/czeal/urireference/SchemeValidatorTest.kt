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
import org.junit.jupiter.api.Test


class SchemeValidatorTest {
    @Test
    fun test_validate() {
        SchemeValidator().validate("http")
        SchemeValidator().validate("ftp")
        SchemeValidator().validate("a+f")

        assertThrowsIAE<Throwable>(
            "The scheme value \"1http\" has an invalid character \"1\" at the index 0.",
            { SchemeValidator().validate("1http") })

        assertThrowsIAE<Throwable>(
            "The scheme value \"http_\" has an invalid character \"_\" at the index 4.",
            { SchemeValidator().validate("http_") })

        assertThrowsIAE<Throwable>(
            "The scheme value must not be empty.", { SchemeValidator().validate("") })

        assertThrowsNPE<Throwable>(
            "The scheme value must not be null.", { SchemeValidator().validate(null) })
    }
}
