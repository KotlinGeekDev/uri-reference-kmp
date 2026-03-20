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
import org.junit.jupiter.api.function.Executable


class PortTest {
    @Test
    fun test_parse() {
        Assertions.assertDoesNotThrow(Executable { PortValidator().validate(PORT1) })
        Assertions.assertDoesNotThrow(Executable { PortValidator().validate(PORT2) })
        Assertions.assertDoesNotThrow(Executable { PortValidator().validate(PORT3) })

        assertThrowsIAE<Throwable>(
            "The port value \"-1\" has an invalid character \"-\" at the index 0.",
            { PortValidator().validate(PORT4) })

        assertThrowsIAE<Throwable>(
            "The port value \"A\" has an invalid character \"A\" at the index 0.",
            { PortValidator().validate(PORT5) })
    }

    companion object {
        private const val PORT1 = "80"
        private const val PORT2 = ""
        private val PORT3: String? = null
        private const val PORT4 = "-1"
        private const val PORT5 = "A"
    }
}
