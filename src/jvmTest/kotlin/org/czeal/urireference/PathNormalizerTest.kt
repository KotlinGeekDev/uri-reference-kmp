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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class PathNormalizerTest {
    @Test
    fun test_validate() {
        Assertions.assertEquals("/", PathNormalizer().normalize("", StandardCharsets.UTF_8, true))
        Assertions.assertEquals("", PathNormalizer().normalize("", StandardCharsets.UTF_8, false))
        Assertions.assertEquals("/b", PathNormalizer().normalize("/a/../b", StandardCharsets.UTF_8, true))
        Assertions.assertEquals("/b", PathNormalizer().normalize("/a/../../b", StandardCharsets.UTF_8, true))
        Assertions.assertEquals("b", PathNormalizer().normalize("../b", StandardCharsets.UTF_8, true))
        Assertions.assertEquals("/a/b/c", PathNormalizer().normalize("/a/b/c", StandardCharsets.UTF_8, true))
    }
}
