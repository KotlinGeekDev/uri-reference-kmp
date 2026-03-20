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

import org.czeal.urireference.QueryParams.Companion.parse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.ThrowingSupplier


class QueryParamsTest {
    @Test
    fun test_parse() {
        Assertions.assertDoesNotThrow<QueryParams?>(ThrowingSupplier { parse("k1=v1&k2=v2") })
        Assertions.assertDoesNotThrow<QueryParams?>(ThrowingSupplier { parse("k1=v1&k2") })
        Assertions.assertDoesNotThrow<QueryParams?>(ThrowingSupplier { parse("k1=v1&k2") })
        Assertions.assertDoesNotThrow<QueryParams?>(ThrowingSupplier { parse("k1=v1&") })
        Assertions.assertDoesNotThrow<QueryParams?>(ThrowingSupplier { parse("") })
        Assertions.assertDoesNotThrow<QueryParams?>(ThrowingSupplier { parse(null) })
    }


    @Test
    fun test_add() {
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2=v2")!!.add("k3", "v3") })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2")!!.add("k3", null) })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2")!!.add("k3", "") })
    }


    @Test
    fun test_replace() {
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier {
            parse("k1=v1&k2=v2")!!.replace(
                "k2",
                "new-value"
            )
        })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2=v2")!!.replace("k3", "v3") })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2")!!.replace("k2", "new-value") })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier {
            parse("k1=v1&k2=v2")!!.replace(
                null,
                "new-value"
            )
        })
    }


    @Test
    fun test_remove() {
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2=v2")!!.remove("k2") })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2=v2-1&k2=v2-2")!!.remove("k2") })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2=v2")!!.remove("") })
        Assertions.assertDoesNotThrow<QueryParams>(ThrowingSupplier { parse("k1=v1&k2=v2")!!.remove(null) })
    }


    @Test
    fun test_toString() {
        Assertions.assertEquals("k1=v1&k2=v2", parse("k1=v1&k2=v2").toString())
        Assertions.assertEquals("k1=v1&k2", parse("k1=v1&k2").toString())
        Assertions.assertEquals("k1=v1&", parse("k1=v1&").toString())
    }
}
