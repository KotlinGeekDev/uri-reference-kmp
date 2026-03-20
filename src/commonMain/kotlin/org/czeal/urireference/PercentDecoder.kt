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
import com.fleeksoft.io.CharBuffer
import kotlin.jvm.JvmStatic


/**
 * Decodes percent-encoded values in a given string.
 * 
 * 
 * 
 * The following is examples of the usage.
 * 
 * 
 * 
 * Examples:
 * <pre>`// Decode some percent-encoded values. PercentDecoder.decode("%41BC", StandardCharset.UTF_8); // "ABC" PercentDecoder.decode("%C3%80BC", StandardCharset.UTF_8); // "ÀBC" `</pre>
 * 
 * @author Hideki Ikeda
 */
internal class PercentDecoder
/**
 * The private constructor.
 */
private constructor() : PercentEncodedStringProcessor() {
    private fun process(input: String, charset: Charset): String? {
        // Validate the input.
        validate(input, charset)

        // Process the input.
        return process(
            input,
            charset,
            StringBuilder()
        )
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


    override fun onDecoded(
        charset: Charset?, outputBuilder: StringBuilder?, buffer: CharBuffer
    ) {
        outputBuilder?.append(buffer)
    }


    override fun onNonPercent(
        input: String, outputBuilder: StringBuilder?, c: Char, index: Int
    ) {
        outputBuilder?.append(c)
    }


    override fun onMalformedPercentEncodedValue(
        input: String, index: Int
    ): IllegalArgumentException {
        return Utils.newIAE(
            "The percent symbol \"%%\" at the index %d in the input value \"%s\" " +
                    "is not followed by two characters.", index, input
        )
    }


    override fun onInvalidHexDigit(
        input: String, hexDigit: Char, index: Int
    ): IllegalArgumentException {
        return Utils.newIAE(
            "The character \"%s\" at the index %d in the value \"%s\" is invalid " +
                    "as a hex digit.", hexDigit, index, input
        )
    }


    override fun onDecodeFailed(input: String, info: Info?): IllegalArgumentException {
        return Utils.newIAE(
            "Failed to decode \"%s\" in the value \"%s\".", info?.string, input
        )
    }

    companion object {
        /**
         * Decode percent-encoded values contained in the given string.
         * 
         * @param input
         * A string containing percent-encoded values to decode.
         * 
         * @param charset
         * The charset to be used for decoding values.
         * 
         * @return
         * The decoded string.
         * 
         * @throws NullPointerException
         * If the input value is null.
         * 
         * @throws IllegalArgumentException
         * If the input value has invalid percent-encoded values that can not
         * be decoded.
         */
        @JvmStatic
        fun decode(input: String, charset: Charset): String? {
            return PercentDecoder().process(input, charset)
        }
    }
}
