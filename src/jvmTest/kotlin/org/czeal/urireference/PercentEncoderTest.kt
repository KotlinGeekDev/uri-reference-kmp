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

import org.czeal.urireference.PercentEncoder.Companion.encode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class PercentEncoderTest {
    @Test
    fun test_encode() {
        Assertions.assertEquals("aA%3F", encode("aA?", StandardCharsets.UTF_8))
        Assertions.assertEquals("abcD123~%E3%82%A2", encode("abcD123~ア", StandardCharsets.UTF_8))
    }
}
