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

import org.czeal.urireference.TestUtils.assertThrowsIAE
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class RegNameValidatorTest {
    @Test
    fun test_validate() {
        RegNameValidator().validate("example.com", StandardCharsets.UTF_8)
        RegNameValidator().validate("a_example.com", StandardCharsets.UTF_8)
        RegNameValidator().validate("a-example.com", StandardCharsets.UTF_8)
        RegNameValidator().validate("%20example.com", StandardCharsets.UTF_8)

        assertThrowsIAE<Throwable>(
            "The host value \"%XXexample.com\" has an invalid hex digit \"X\" at the index 1.",
            { RegNameValidator().validate("%XXexample.com", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"a@example.com\" has an invalid character \"@\" at the index 1.",
            { RegNameValidator().validate("a@example.com", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"example.com/a\" has an invalid character \"/\" at the index 11.",
            { RegNameValidator().validate("example.com/a", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"example.com?a\" has an invalid character \"?\" at the index 11.",
            { RegNameValidator().validate("example.com?a", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"example.com:a\" has an invalid character \":\" at the index 11.",
            { RegNameValidator().validate("example.com:a", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"例子.测试\" has an invalid character \"例\" at the index 0.",
            { RegNameValidator().validate("例子.测试", StandardCharsets.UTF_8) })
    }
}
