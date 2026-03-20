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

import org.czeal.urireference.PercentDecoder.Companion.decode
import org.czeal.urireference.TestUtils.assertThrowsIAE
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class PercentDecoderTest {
    @Test
    fun test_decode() {
        Assertions.assertEquals("aA", decode("a%41", StandardCharsets.UTF_8))
        Assertions.assertEquals("aア", decode("a%e3%82%A2", StandardCharsets.UTF_8))
        Assertions.assertEquals("aアbc", decode("a%e3%82%A2bc", StandardCharsets.UTF_8))

        assertThrowsIAE<Throwable>(
            "The character \"X\" at the index 2 in the value \"a%XX\" is invalid as a hex digit.",
            { decode("a%XX", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The percent symbol \"%\" at the index 1 in the input value \"a%A\" is not followed by two characters.",
            { decode("a%A", StandardCharsets.UTF_8) })
    }
}
