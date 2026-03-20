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

import org.czeal.urireference.QueryParam.Companion.parse
import org.czeal.urireference.TestUtils.assertThrowsNPE
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.ThrowingSupplier


class QueryParamTest {
    @Test
    fun test_parse() {
        Assertions.assertDoesNotThrow<QueryParam> { parse("k=v") }
        Assertions.assertDoesNotThrow<QueryParam> { parse("k=") }
        Assertions.assertDoesNotThrow<QueryParam> { parse("=v") }
        Assertions.assertDoesNotThrow<QueryParam> { parse("k") }
        Assertions.assertDoesNotThrow<QueryParam> { parse("") }
        Assertions.assertDoesNotThrow<QueryParam> { parse("k=v=v") }
        assertThrowsNPE<Throwable>("The input string must not be null.", { parse(null) })
    }


    @Test
    fun test_toString() {
        Assertions.assertEquals("k=v", parse("k=v").toString())
        Assertions.assertEquals("k=", parse("k=").toString())
        Assertions.assertEquals("=v", parse("=v").toString())
        Assertions.assertEquals("k", parse("k").toString())
        Assertions.assertEquals("", parse("").toString())
        Assertions.assertEquals("k=v=v", parse("k=v=v").toString())
    }


    @Test
    fun test_getKey() {
        Assertions.assertEquals("k", parse("k=v").key)
        Assertions.assertEquals("k", parse("k=").key)
        Assertions.assertEquals("", parse("=v").key)
        Assertions.assertEquals("k", parse("k").key)
        Assertions.assertEquals("", parse("").key)
        Assertions.assertEquals("k", parse("k=v=v").key)
    }


    @Test
    fun test_getValue() {
        Assertions.assertEquals("v", parse("k=v").value)
        Assertions.assertEquals("", parse("k=").value)
        Assertions.assertEquals("v", parse("=v").value)
        Assertions.assertEquals(null, parse("k").value)
        Assertions.assertEquals(null, parse("").value)
        Assertions.assertEquals("v=v", parse("k=v=v").value)
    }
}
