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
import kotlin.test.Test
import kotlin.test.assertEquals


class PathSegmentsTest {
    @Test
    fun test_parse() {
        assertDoesNotThrow<PathSegments?>( { parse("/a/b/c") })
        assertDoesNotThrow<PathSegments?>( { parse("/") })
        assertDoesNotThrow<PathSegments?>( { parse("") })
        assertDoesNotThrow<PathSegments?>( { parse(null) })
    }


    @Test
    fun test_add() {
        assertDoesNotThrow<PathSegments>( { parse("/a/b/c")!!.add(listOf("d", "e")) })
        assertDoesNotThrow<PathSegments>( { parse("/a/b/c")!!.add(listOf("")) })

        assertThrowsNPE<Throwable>(
            "The segments must not be null.",
            { parse("/a/b/c")!!.add(null) })

        assertThrowsNPE<Throwable>(
            "A segment must not be null.",
            { parse("/a/b/c")!!.add(listOf(null)) })
    }


    @Test
    fun test_toString() {
        assertEquals("/a/b/c", parse("/a/b/c").toString())
        assertEquals("/a/b/c/d/e", parse("/a/b/c")!!.add(listOf("d", "e")).toString())
    }
}
