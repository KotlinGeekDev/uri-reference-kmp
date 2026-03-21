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

import com.fleeksoft.charset.Charsets
import org.czeal.urireference.TestUtils.assertThrowsIAE
import org.czeal.urireference.TestUtils.assertThrowsNPE
import kotlin.test.Test


class SegmentNzNcValidatorTest {
    @Test
    fun test_validate() {
        SegmentNzNcValidator().validate("abcde12345-._~", Charsets.UTF8)
        SegmentNzNcValidator().validate("()+_", Charsets.UTF8)
        SegmentNzNcValidator().validate("!$&'()*+,;=", Charsets.UTF8)
        SegmentNzNcValidator().validate("@", Charsets.UTF8)

        assertThrowsIAE<Throwable>(
            "The path segment value must not be empty.",
            { SegmentNzNcValidator().validate("", Charsets.UTF8) })

        assertThrowsNPE<Throwable>(
            "The path segment value must not be null.",
            { SegmentNzNcValidator().validate(null, Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The path segment value \"segment:\" has an invalid character \":\" at the index 7.",
            { SegmentNzNcValidator().validate("segment:", Charsets.UTF8) })
    }
}
