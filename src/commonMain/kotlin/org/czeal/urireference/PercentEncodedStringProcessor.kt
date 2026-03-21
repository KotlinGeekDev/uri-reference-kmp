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
import com.fleeksoft.io.ByteArrayOutputStream
import com.fleeksoft.io.ByteBufferFactory
import com.fleeksoft.io.CharBuffer
import com.fleeksoft.io.exception.CharacterCodingException


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * An abstract class for processing percent-encoded strings.
 * 
 * @author Hideki Ikeda
 */
internal abstract class PercentEncodedStringProcessor {
    /**
     * Information about percent-encoded values.
     */
    class Info {
        private val sb = StringBuilder()
        private val bs = ByteArrayOutputStream()


        /**
         * Saves the given information.
         * 
         * @param percentEncodedValue
         * The percent-encoded value.
         * 
         * @param byteForPercentEncodedValue
         * The byte represented by the percent-encoded value.
         */
        fun add(percentEncodedValue: String?, byteForPercentEncodedValue: Byte) {
            sb.append(percentEncodedValue)
            bs.write(byteForPercentEncodedValue.toInt())
        }


        val string: String
            /**
             * Returns the sequence of the percent-encoded values.
             * 
             * @return
             * The sequence of the percent-encoded values.
             */
            get() = sb.toString()


        val byteArray: ByteArray
            /**
             * Returns the bytes represented by the percent-encoded values.
             * 
             * @return
             * The bytes represented by the percent-encoded values.
             */
            get() = bs.toByteArray()


        /**
         * Resets the internal information.
         */
        fun reset() {
            sb.setLength(0)
            bs.reset()
        }
    }


    /**
     * Processes an input string that could contain percent-encoded values and
     * outputs a string if necessary.
     * 
     * @param input
     * The input string.
     * 
     * @param charset
     * The charset used in the input string.
     * 
     * @param outputBuilder
     * The output string builder . This property is expected to be populated
     * in either/both [         onDecoded(Charset, StringBuilder, CharBuffer)][PercentEncodedStringProcessor.onDecoded] method or/and [         ][PercentEncodedStringProcessor.onNonPercent] method in subclasses.
     * 
     * @return
     * A string built by `outputBuilder` if `outputBuilder`
     * is specified; otherwise, `null`.
     */
    protected fun process(input: String, charset: Charset?, outputBuilder: StringBuilder?): String? {
        // The current index.
        var currentIndex = 0

        // The last index.
        val lastIndex = input.length - 1

        // The stream to store bytes represented by percent-encoded values.
        val info = Info()

        while (currentIndex <= lastIndex) {
            // The character at the current index.
            val c = input[currentIndex]

            if (c == '%') {
                // If the character is "%", which indicates a percent-encoded
                // value, process the percent-encoded value.
                onPercent(input, charset, outputBuilder, currentIndex, lastIndex, info)
                currentIndex += 3
            } else {
                // If the character is not "%", process the character.
                onNonPercent(input, outputBuilder, c, currentIndex)
                currentIndex++
            }
        }

        // Build the output string if the output builder is specified.
        return if (outputBuilder == null) null else outputBuilder.toString()
    }


    private fun onPercent(
        input: String, charset: Charset?, outputBuilder: StringBuilder?, currentIndex: Int,
        lastIndex: Int, info: Info
    ) {
        // Ensure there are characters at indexes "currentIndex + 1" and
        // "currentIndex + 2" in the input.
        if (currentIndex + 2 > lastIndex) {
            throw onMalformedPercentEncodedValue(input, currentIndex)
        }

        // Extract the target percent-encoded value "%XX" from the input string.
        val percentEncodedValue = input.substring(currentIndex, currentIndex + 3)

        // Convert the higher and lower hex digits of the percent-encoded value
        // to a byte.
        val b = toByte(input, currentIndex + 1, currentIndex + 2)

        // Save the percent-encoded value and the bytes.
        info.add(percentEncodedValue, b)

        // If the next index (currentIndex + 3) exceeds the last index  or the
        // character at the next index is not '%'.
        val nextIndex = currentIndex + 3
        if (nextIndex > lastIndex || input[nextIndex] != '%') {
            // Decode the percent-encoded values.
            decode(input, charset, outputBuilder, info)

            // Reset the information about the percent-encoded values.
            info.reset()
        }
    }


    private fun toByte(input: String, higherHexDigitIndex: Int, lowerHexDigitIndex: Int): Byte {
        // The higher hex digit.
        val higherHexDigit = input.get(higherHexDigitIndex)

        // Read the higher hex digit in the percent-encoded value and convert it
        // to an int value.
        val intOfHigherHexDigit = toIntOfHexDigit(
            input, higherHexDigitIndex, higherHexDigit
        )

        // The lower hex digit.
        val lowerHexDigit = input[lowerHexDigitIndex]

        // Read the lower hex digit in the percent-encoded value and convert it
        // to an int value.
        val intOfLowerHexDigit = toIntOfHexDigit(
            input, lowerHexDigitIndex, lowerHexDigit
        )

        // Calculate a byte represented by the percent-encoded value.
        return ((intOfHigherHexDigit shl 4) + intOfLowerHexDigit).toByte()
    }


    private fun toIntOfHexDigit(input: String, index: Int, hexDigit: Char): Int {
        // Convert the hex digit to an int value.
        val intOfHexDigit = Utils.fromHexDigit(hexDigit)

        // Ensure the hex digit is valid.
        if (intOfHexDigit == -1) {
            throw onInvalidHexDigit(input, hexDigit, index)
        }

        // Return the int value.
        return intOfHexDigit
    }


    protected fun decode(
        input: String, charset: Charset?, outputBuilder: StringBuilder?, info: Info
    ) {
        // The buffer to store decoded results.
        val docodedCharBuffer: CharBuffer

        try {
            // Decode the bytes stored in the byte stream.
            docodedCharBuffer = charset?.newDecoder()
                ?.decode(ByteBufferFactory.wrap(info.byteArray))!!
        } catch (e: CharacterCodingException) {
            // Failed to decode bytes represented by a sequence of percent-encoded
            // values.
            throw onDecodeFailed(input, info)
        }

        // Process when the percent-encoded values have been decoded.
        onDecoded(charset, outputBuilder, docodedCharBuffer)
    }


    /**
     * Invoked when a malformed percent-encoded value is found in the input.
     * 
     * @param input
     * The input value.
     * 
     * @param index
     * The index of the malformed percent-encoded value in the input.
     * 
     * @return
     * An `IllegalArgumentException` to be thrown.
     */
    protected abstract fun onMalformedPercentEncodedValue(
        input: String, index: Int
    ): IllegalArgumentException


    /**
     * Invoked when an invalid hex digit is found in the input.
     * 
     * @param input
     * The input value.
     * 
     * @param hexDigit
     * The invalid hex digit.
     * 
     * @param index
     * The index of the invalid hex digit in the input.
     * 
     * @return
     * An `IllegalArgumentException` to be thrown.
     */
    protected abstract fun onInvalidHexDigit(
        input: String, hexDigit: Char, index: Int
    ): IllegalArgumentException


    /**
     * Invoked when failed to decoded percent-encoded values contained in the input.
     * 
     * @param input
     * The input value.
     * 
     * @param info
     * Information about the percent-encoded values.
     * 
     * @return
     * An `IllegalArgumentException` to be thrown.
     */
    protected abstract fun onDecodeFailed(input: String, info: Info?): IllegalArgumentException


    /**
     * Invoked when percent-encoded values contained in the input have been
     * successfully decoded.
     * 
     * @param charset
     * The charset.
     * 
     * @param outputBuilder
     * The output builder.
     * 
     * @param buffer
     * The char buffer containing decoded characters.
     */
    protected abstract fun onDecoded(
        charset: Charset?, outputBuilder: StringBuilder?, buffer: CharBuffer
    )


    /**
     * Invoked when a non-percent value is found in the input.
     * 
     * @param input
     * The input value.
     * 
     * @param outputBuilder
     * The output builder.
     * 
     * @param c
     * The non-percent character.
     * 
     * @param index
     * The index of the non-percent character in the input.
     */
    protected abstract fun onNonPercent(
        input: String, outputBuilder: StringBuilder?, c: Char, index: Int
    )
}
