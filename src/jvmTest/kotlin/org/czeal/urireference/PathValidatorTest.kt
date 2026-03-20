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


class PathValidatorTest {
    @Test
    fun test_validate() {
        //
        // relativeReference = true, hasAuthority = true
        //
        PathValidator().validate("/segment", StandardCharsets.UTF_8, true, true)
        PathValidator().validate("/segment/", StandardCharsets.UTF_8, true, true)
        PathValidator().validate("/segment1/segment2", StandardCharsets.UTF_8, true, true)
        PathValidator().validate("/", StandardCharsets.UTF_8, true, true)
        PathValidator().validate("//", StandardCharsets.UTF_8, true, true)
        PathValidator().validate("", StandardCharsets.UTF_8, true, true)
        PathValidator().validate(null, StandardCharsets.UTF_8, true, true)

        assertThrowsIAE<Throwable>(
            "The path value is invalid.",
            { PathValidator().validate("segment", StandardCharsets.UTF_8, true, true) })

        //
        // relativeReference = false, hasAuthority = false
        //
        PathValidator().validate("/segment", StandardCharsets.UTF_8, true, false)
        PathValidator().validate("/segment/", StandardCharsets.UTF_8, true, false)
        PathValidator().validate("/segment1/segment2", StandardCharsets.UTF_8, true, false)
        PathValidator().validate("/", StandardCharsets.UTF_8, true, false)
        PathValidator().validate("segment", StandardCharsets.UTF_8, true, false)
        PathValidator().validate("", StandardCharsets.UTF_8, true, false)
        PathValidator().validate(null, StandardCharsets.UTF_8, true, false)

        assertThrowsIAE<Throwable>(
            "The path value is invalid.",
            { PathValidator().validate("//", StandardCharsets.UTF_8, true, false) })

        //
        // relativeReference = false, hasAuthority = true
        //
        PathValidator().validate("/segment", StandardCharsets.UTF_8, false, true)
        PathValidator().validate("/segment/", StandardCharsets.UTF_8, false, true)
        PathValidator().validate("/segment1/segment2", StandardCharsets.UTF_8, false, true)
        PathValidator().validate("/", StandardCharsets.UTF_8, false, true)
        PathValidator().validate("//", StandardCharsets.UTF_8, false, true)
        PathValidator().validate("", StandardCharsets.UTF_8, false, true)
        PathValidator().validate(null, StandardCharsets.UTF_8, false, true)

        assertThrowsIAE<Throwable>(
            "The path value is invalid.",
            { PathValidator().validate("segment", StandardCharsets.UTF_8, false, true) })

        //
        // relativeReference = false, hasAuthority = false
        //
        PathValidator().validate("/segment", StandardCharsets.UTF_8, false, false)
        PathValidator().validate("/segment/", StandardCharsets.UTF_8, false, false)
        PathValidator().validate("/segment1/segment2", StandardCharsets.UTF_8, false, false)
        PathValidator().validate("/", StandardCharsets.UTF_8, false, false)
        PathValidator().validate("segment", StandardCharsets.UTF_8, false, false)
        PathValidator().validate("", StandardCharsets.UTF_8, false, false)
        PathValidator().validate(null, StandardCharsets.UTF_8, false, false)

        assertThrowsIAE<Throwable>(
            "The path value is invalid.",
            { PathValidator().validate("//", StandardCharsets.UTF_8, false, false) })
    }
}
