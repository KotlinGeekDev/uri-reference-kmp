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

import io.kotlingeekdev.urireference.QueryParam.Companion.parse
import kotlin.test.Test
import kotlin.test.assertEquals


class QueryParamTest {
    @Test
    fun test_parse() {
        assertDoesNotThrow<QueryParam> { parse("k=v") }
        assertDoesNotThrow<QueryParam> { parse("k=") }
        assertDoesNotThrow<QueryParam> { parse("=v") }
        assertDoesNotThrow<QueryParam> { parse("k") }
        assertDoesNotThrow<QueryParam> { parse("") }
        assertDoesNotThrow<QueryParam> { parse("k=v=v") }
        TestUtils.assertThrowsNPE<Throwable>("The input string must not be null.", { parse(null) })
    }


    @Test
    fun test_toString() {
        assertEquals("k=v", parse("k=v").toString())
        assertEquals("k=", parse("k=").toString())
        assertEquals("=v", parse("=v").toString())
        assertEquals("k", parse("k").toString())
        assertEquals("", parse("").toString())
        assertEquals("k=v=v", parse("k=v=v").toString())
    }


    @Test
    fun test_getKey() {
        assertEquals("k", parse("k=v").key)
        assertEquals("k", parse("k=").key)
        assertEquals("", parse("=v").key)
        assertEquals("k", parse("k").key)
        assertEquals("", parse("").key)
        assertEquals("k", parse("k=v=v").key)
    }


    @Test
    fun test_getValue() {
        assertEquals("v", parse("k=v").value)
        assertEquals("", parse("k=").value)
        assertEquals("v", parse("=v").value)
        assertEquals(null, parse("k").value)
        assertEquals(null, parse("").value)
        assertEquals("v=v", parse("k=v=v").value)
    }
}
