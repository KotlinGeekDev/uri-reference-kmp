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

import com.fleeksoft.charset.Charsets
import kotlin.test.Test


class QueryValidatorTest {
    @Test
    fun test_validate() {
        QueryValidator().validate("k1=v1&k2=v2", Charsets.UTF8)
        QueryValidator().validate("", Charsets.UTF8)
        QueryValidator().validate(null, Charsets.UTF8)

        TestUtils.assertThrowsIAE<Throwable>(
            "The query value \"[invalid_query]\" has an invalid character \"[\" at the index 0.",
            { QueryValidator().validate("[invalid_query]", Charsets.UTF8) })

        TestUtils.assertThrowsIAE<Throwable>(
            "The percent symbol \"%\" at the index 6 in the query value \"k1=v1&%1\" is not followed by two characters.",
            { QueryValidator().validate("k1=v1&%1", Charsets.UTF8) })

        TestUtils.assertThrowsIAE<Throwable>(
            "Failed to decode bytes represented by \"%FF\" in the query value \"k1=v1&%FF\".",
            { QueryValidator().validate("k1=v1&%FF", Charsets.UTF8) })
    }
}
