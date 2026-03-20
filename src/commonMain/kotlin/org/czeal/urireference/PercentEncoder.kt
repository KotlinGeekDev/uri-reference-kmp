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

import com.fleeksoft.charset.Charset
import com.fleeksoft.io.CharBufferFactory
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * An encoder that percent-encodes characters in a given string.
 * 
 * 
 * Examples:
 * <pre>`// Create an instance of PercentEncoder using UTF-8. PercentEncoder encoder = new PercentEncoder(StandardCharset.UTF_8); // Encode some string values. PercentEncoder.encode("ÀBC", StandardCharset.UTF_8); // output: "%C3%80BC" PercentEncoder.encode("ABC", StandardCharset.UTF_8); // output: "ABC" (Unreserved characters are not encoded.) `</pre>
 * 
 * @author Hideki Ikeda
 */
internal class PercentEncoder

/**
 * The private constructor.
 */
private constructor() {
    /**
     * Percent-encode the given input with the specified charset. This method will
     * not encode characters contained in `preservedChars` parameter.
     * 
     * @param input
     * The input to percent-encode.
     * 
     * @param charset
     * The charset to be used for encoding values.
     * 
     * @param preservedChars
     * The character set to preserve.
     * 
     * @return
     * The encoded string.
     * 
     * @throws NullPointerException
     * If the value of `input` or `charset` is `null`.
     * 
     * @see [
     * RFC 3986, 2.3. Unreserved Characters](https://www.rfc-editor.org/rfc/rfc3986.section-2.3)
     */
    private fun process(
        input: String, charset: Charset, preservedChars: MutableSet<Char?>?
    ): String {
        // Validate the arguments.
        validate(input, charset)

        // The builder for the resultant string.
        val outputBuilder = StringBuilder()

        // Encode each character in the input.
        for (i in 0..<input.length) {
            // The i-th character of the input value.
            val c = input[i]

            // If the character should be preserved.
            if (isPreserved(preservedChars, c)) {
                // Preserve the character as-is.
                outputBuilder.append(c)
            } else {
                // Encode the character with the charset and append it.
                outputBuilder.append(encode(charset, c))
            }
        }

        // Build the output string.
        return outputBuilder.toString()
    }


    private fun validate(input: String, charset: Charset) {
        // Ensure the input is not null.
        if (input == null) {
            throw Utils.newNPE("A input must not be null.")
        }

        // Ensure the charset is not null.
        if (charset == null) {
            throw Utils.newNPE("A charset must not be null.")
        }
    }


    private fun isPreserved(preservedChars: MutableSet<Char?>?, c: Char): Boolean {
        // If the preservedChars is not null, check if the character is
        // contained in preservedChars; otherwise, check if the character
        // is an unreserved character or not.
        return if (preservedChars != null) preservedChars.contains(c) else Utils.isUnreserved(c)
    }


    private fun encode(charset: Charset, c: Char): CharArray {
        // Encode the character into bytes using the charset.
        val bytes = toBytes(charset, c)

        // The output array.
        val chars = CharArray(3 * bytes.size)

        // For each byte.
        for (i in bytes.indices) {
            // Create a percent-encoded value.
            chars[3 * i] = '%'
            chars[3 * i + 1] = Utils.toHexDigit((bytes[i].toInt() and 0xF0) shr 4)
            chars[3 * i + 2] = Utils.toHexDigit(bytes[i].toInt() and 0xF)
        }

        // Return the output array.
        return chars
    }


    private fun toBytes(charset: Charset, c: Char): ByteArray {
        try {
            // Encode the character using the charset and then convert it to an
            // byte array.
            return charset.newEncoder()
                .encode(CharBufferFactory.wrap(c.toString()))
                .array()
        } catch (e: CharacterCodingException) {
            // Failed to encode the character.
            throw Utils.newIAE("Failed to encode the character \"$c\".")
        }
    }

    companion object {
        /**
         * Percent-encode the given input with the specified charset. This method
         * will not encode characters contained in `preservedChars` parameter.
         * 
         * @param input
         * Required. The input to encode.
         * 
         * @param charset
         * Required. The charset to be used for encoding values.
         * 
         * @param preservedChars
         * Optional. The character set to preserve.
         * 
         * @return
         * The encoded string.
         * 
         * @throws NullPointerException
         * If the value of `input` or `charset` is `null`.
         * 
         * @see [
         * RFC 3986, 2.3. Unreserved Characters](https://www.rfc-editor.org/rfc/rfc3986.section-2.3)
         */
        /**
         * Percent encode the given input with the specified charset. This method will
         * not encode unreserved characters defined in [
         * RFC 3986, 2.3. Unreserved Characters](https://www.rfc-editor.org/rfc/rfc3986#section-2.3).
         * 
         * @param input
         * Required. The input to encode.
         * 
         * @param charset
         * Required. The charset to be used for encoding values.
         * 
         * @return
         * The encoded string.
         * 
         * @throws NullPointerException
         * If the value of `input` or `charset` is `null`.
         * 
         * @see [RFC 3986,
         * 2.3. Unreserved Characters](https://www.rfc-editor.org/rfc/rfc3986.section-2.3)
         */
        @JvmOverloads
        @JvmStatic
        fun encode(
            input: String, charset: Charset, preservedChars: MutableSet<Char?>? = null
        ): String {
            return PercentEncoder().process(input, charset, preservedChars)
        }
    }
}
