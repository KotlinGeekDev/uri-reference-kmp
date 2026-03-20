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


class UserinfoValidatorTest {
    @Test
    fun test_validate() {
        UserinfoValidator().validate("userinfo", StandardCharsets.UTF_8)
        UserinfoValidator().validate("user:password", StandardCharsets.UTF_8)
        UserinfoValidator().validate("", StandardCharsets.UTF_8)
        UserinfoValidator().validate(null, StandardCharsets.UTF_8)

        assertThrowsIAE<Throwable>(
            "The userinfo value \"user password\" has an invalid character \" \" at the index 4.",
            { UserinfoValidator().validate("user password", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The userinfo value \"user#password\" has an invalid character \"#\" at the index 4.",
            { UserinfoValidator().validate("user#password", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The userinfo value \"user/password\" has an invalid character \"/\" at the index 4.",
            { UserinfoValidator().validate("user/password", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The userinfo value \"%XXuserinfo\" has an invalid hex digit \"X\" at the index 1.",
            { UserinfoValidator().validate("%XXuserinfo", StandardCharsets.UTF_8) })
    }
}
