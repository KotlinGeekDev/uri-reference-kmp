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

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull


object TestUtils {
    fun <T : Throwable?> assertThrowsNPE(executable: InlineExecutable) {
        assertThrowsNPE<Throwable?>(null, executable)
    }


    fun <T : Throwable?> assertThrowsNPE(
        message: String?, executable: InlineExecutable
    ) {
        assertThrowsException<NullPointerException>(message, executable)
    }


    fun <T : Throwable?> assertThrowsIAE(
        message: String?, executable: InlineExecutable
    ) {
        assertThrowsException<IllegalArgumentException>(
            message,
            executable
        )
    }


    fun <T : Throwable> assertThrowsISE(
        message: String?, executable: InlineExecutable
    ) {
        assertThrowsException<IllegalStateException>(message, executable)
    }


    inline fun <reified T : Throwable> assertThrowsException(
        message: String?, executable: InlineExecutable
    ) {
        // Assert the 'executable' throw an exception.

        val ex = assertFailsWith(T::class, {executable.execute()})

        // Ensure the exception is not null.
        assertNotNull(ex)

        // Ensure the message matches the expected one.
        if (message != null) {
            assertEquals(message, ex.message)
        }
    }
}

fun <T> assertDoesNotThrow(executable: InlineExecutable) {
    try {
        executable.execute()
    } catch (e: Throwable) {
        throw AssertionError(e)
    }
}

fun interface InlineExecutable {
    @Throws(Throwable::class)
    fun execute()
}
