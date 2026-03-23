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


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * Validates the `scheme` component of a URI reference, according to the
 * syntax defined in [
 * RFC 3986, Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A) as follows.
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." ) `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class SchemeValidator {
    /**
     * Validates a scheme value.
     * 
     * @param scheme
     * A scheme value.
     * 
     * @throws IllegalArgumentException
     * If the scheme value is invalid.
     */
    fun validate(scheme: String?) {
        // If the scheme is null.
        if (scheme == null) {
            // The scheme must not be empty.
            throw Utils.newNPE("The scheme value must not be null.")
        }

        // If the scheme is empty.
        if (scheme.isEmpty()) {
            // The scheme must not be empty.
            throw Utils.newIAE("The scheme value must not be empty.")
        }

        // Validate the first character in the scheme.
        validateFirstCharacter(scheme)

        // Check the remaining characters in the scheme.
        validateRemainingCharacters(scheme)
    }


    private fun validateFirstCharacter(scheme: String) {
        val c = scheme.get(0)

        if (!Utils.isAlphabet(c)) {
            throw Utils.newIAE(
                "The scheme value \"%s\" has an invalid character \"%s\" at " +
                        "the index 0.", scheme, c
            )
        }
    }


    private fun validateRemainingCharacters(scheme: String) {
        for (i in 1..<scheme.length) {
            val c = scheme[i]

            if (!isValid(c)) {
                throw Utils.newIAE(
                    "The scheme value \"%s\" has an invalid character \"%s\" at " +
                            "the index %d.", scheme, c, i
                )
            }
        }
    }


    private fun isValid(c: Char): Boolean {
        return Utils.isAlphabet(c) ||
                Utils.isDigit(c) || c == '+' || c == '-' || c == '.'
    }
}
