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

import org.czeal.urireference.PathSegments.Companion.parse
import org.czeal.urireference.TestUtils.assertThrowsNPE
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.ThrowingSupplier


class PathSegmentsTest {
    @Test
    fun test_parse() {
        Assertions.assertDoesNotThrow<PathSegments?>(ThrowingSupplier { parse("/a/b/c") })
        Assertions.assertDoesNotThrow<PathSegments?>(ThrowingSupplier { parse("/") })
        Assertions.assertDoesNotThrow<PathSegments?>(ThrowingSupplier { parse("") })
        Assertions.assertDoesNotThrow<PathSegments?>(ThrowingSupplier { parse(null) })
    }


    @Test
    fun test_add() {
        Assertions.assertDoesNotThrow<PathSegments>(ThrowingSupplier { parse("/a/b/c")!!.add(listOf("d", "e")) })
        Assertions.assertDoesNotThrow<PathSegments>(ThrowingSupplier { parse("/a/b/c")!!.add(listOf("")) })

        assertThrowsNPE<Throwable>(
            "The segments must not be null.",
            { parse("/a/b/c")!!.add(null) })

        assertThrowsNPE<Throwable>(
            "A segment must not be null.",
            { parse("/a/b/c")!!.add(listOf(null)) })
    }


    @Test
    fun test_toString() {
        Assertions.assertEquals("/a/b/c", parse("/a/b/c").toString())
        Assertions.assertEquals("/a/b/c/d/e", parse("/a/b/c")!!.add(listOf("d", "e")).toString())
    }
}
