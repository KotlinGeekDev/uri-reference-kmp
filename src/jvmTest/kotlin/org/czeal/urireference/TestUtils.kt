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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows


object TestUtils {
    fun <T : Throwable?> assertThrowsNPE(executable: InlineExecutable) {
        assertThrowsNPE<Throwable?>(null, executable)
    }


    fun <T : Throwable?> assertThrowsNPE(
        message: String?, executable: InlineExecutable
    ) {
        assertThrowsException(NullPointerException::class.java, message, executable)
    }


    fun <T : Throwable?> assertThrowsIAE(
        message: String?, executable: InlineExecutable
    ) {
        assertThrowsException(
            IllegalArgumentException::class.java,
            message,
            executable
        )
    }


    fun <T : Throwable> assertThrowsISE(
        message: String?, executable: InlineExecutable
    ) {
        assertThrowsException(IllegalStateException::class.java, message, executable)
    }


    inline fun <reified T : Throwable> assertThrowsException(
        expectedType: Class<T>, message: String?, executable: InlineExecutable
    ) {
        // Assert the 'executable' throw an exception.
        val ex = assertThrows<T> { executable.execute() }

        // Ensure the exception is not null.
        Assertions.assertNotNull(ex)

        // Ensure the message matches the expected one.
        if (message != null) {
            Assertions.assertEquals(message, ex!!.message)
        }
    }
}

fun interface InlineExecutable {
    @Throws()
    fun execute()
}
