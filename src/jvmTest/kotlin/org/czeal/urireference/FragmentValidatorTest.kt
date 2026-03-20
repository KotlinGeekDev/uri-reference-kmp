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


class FragmentValidatorTest {
    @Test
    fun test_validate() {
        FragmentValidator().validate("section1", StandardCharsets.UTF_8)
        FragmentValidator().validate("fig%20A", StandardCharsets.UTF_8)
        FragmentValidator().validate("2.3", StandardCharsets.UTF_8)
        FragmentValidator().validate("", StandardCharsets.UTF_8)
        FragmentValidator().validate(null, StandardCharsets.UTF_8)

        assertThrowsIAE<Throwable>(
            "The fragment value \"#fragment\" has an invalid character \"#\" at the index 0.",
            { FragmentValidator().validate("#fragment", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The fragment value \" frag\" has an invalid character \" \" at the index 0.",
            { FragmentValidator().validate(" frag", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The percent symbol \"%\" at the index 8 in the fragment value \"fragment%1\" is not followed by two characters.",
            { FragmentValidator().validate("fragment%1", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The fragment value \"fragment%XX\" has an invalid hex digit \"X\" at the index 9.",
            { FragmentValidator().validate("fragment%XX", StandardCharsets.UTF_8) })
    }
}
