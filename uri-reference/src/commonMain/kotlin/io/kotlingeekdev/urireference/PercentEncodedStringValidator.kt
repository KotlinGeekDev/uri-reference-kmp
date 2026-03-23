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

import com.fleeksoft.charset.Charset
import com.fleeksoft.io.CharBuffer


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * An abstract class for validating percent-encoded strings.
 * 
 * @author Hideki Ikeda
 */
internal abstract class PercentEncodedStringValidator protected constructor(protected val name: String?) :
    PercentEncodedStringProcessor() {
    override fun onDecoded(
        charset: Charset?, outputBuilder: StringBuilder?, buffer: CharBuffer
    ) {
        // Do nothing.
    }


    override fun onNonPercent(
        input: String, outputBuilder: StringBuilder?, c: Char, index: Int
    ) {
        if (!isValidOnNonPercent(c)) {
            throw Utils.newIAE(
                "The %s value \"%s\" has an invalid character \"%s\" at the index %d.",
                name, input, c, index
            )
        }
    }


    override fun onMalformedPercentEncodedValue(
        input: String, index: Int
    ): IllegalArgumentException {
        throw Utils.newIAE(
            "The percent symbol \"%%\" at the index %d in the %s value \"%s\" is " +
                    "not followed by two characters.", index, name, input
        )
    }


    override fun onInvalidHexDigit(
        input: String, hexDigit: Char, index: Int
    ): IllegalArgumentException {
        throw Utils.newIAE(
            "The %s value \"%s\" has an invalid hex digit \"%c\" at the index %d.",
            name, input, hexDigit, index
        )
    }


    override fun onDecodeFailed(input: String, info: Info?): IllegalArgumentException {
        throw Utils.newIAE(
            "Failed to decode bytes represented by \"%s\" in the %s value \"%s\".",
            info?.string, name, input
        )
    }


    /**
     * Checks whether or not the character is valid as a non-percent value.
     * 
     * @param c The character to check.
     * 
     * @return
     * `true` if the character is valid as a non-percent value;
     * otherwise, `false`.
     */
    protected abstract fun isValidOnNonPercent(c: Char): Boolean
}
