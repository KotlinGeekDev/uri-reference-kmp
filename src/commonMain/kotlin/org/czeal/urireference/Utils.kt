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

import dev.format.KotlinFormatter


/**
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * Utility class.
 * 
 * @author Hideki Ikeda
 */
internal object Utils {
    /**
     * Checks whether a character is in a range or not.
     * 
     * @param c
     * A character.
     * 
     * @param start
     * The first character in the range.
     * 
     * @param end
     * The last character in the range.
     * 
     * @return
     * `true` if the character is in a range; otherwise, `false`.
     */
    fun isInRange(c: Char, start: Char, end: Char): Boolean {
        return c in start..end
    }


    /**
     * Checks whether o nor a character is a digit (0-9).
     * 
     * @param c
     * A character.
     * 
     * @return
     * `true` if the character is a digit (0-9); otherwise, `false`.
     */
    fun isDigit(c: Char): Boolean {
        return isInRange(c, '0', '9')
    }


    /**
     * Checks whether o nor a character is an alphabet.
     * 
     * @param c
     * A character.
     * 
     * @return
     * `true` if the character is an alphabet; otherwise, `false`.
     */
    fun isAlphabet(c: Char): Boolean {
        return isInRange(c, 'a', 'z') || isInRange(c, 'A', 'Z')
    }


    /**
     * Checks whether or not a character is an unreserved character, as defined
     * in [RFC 3986,
     * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A).
     * 
     * <blockquote>
     * <pre style="font-family: 'Menlo', 'Courier', monospace;">`unreserved= ALPHA / DIGIT / "-" / "." / "_" / "~" `</pre>
    </blockquote> * 
     * 
     * @param c
     * A character.
     * 
     * @return
     * `true` if the character is an unreserved character; otherwise,
     * `false`.
     */
    fun isUnreserved(c: Char): Boolean {
        return isAlphabet(c) ||
                isDigit(c) || c == '-' || c == '.' || c == '_' || c == '~'
    }


    /**
     * Checks whether or not a character is a sub-delimiter, as defined in
     * [RFC 3986,
     * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A) as follows.
     * 
     * <blockquote>
     * <pre style="font-family: 'Menlo', 'Courier', monospace;">`subdelim = "!" / "$" / "&" / "'" / "(" / ")"          / "*" / "+" / "," / ";" / "=" `
    </pre> * 
    </blockquote> * 
     * 
     * @param c
     * A character.
     * 
     * @return
     * `true` if the character is a sub-delimiter; otherwise, `false`.
     */
    fun isSubdelim(c: Char): Boolean {
        return c == '!' || c == '$' || c == '&' || c == '\'' || c == '(' || c == ')' || c == '*' || c == '+' || c == ',' || c == ';' || c == '='
    }


    /**
     * Checks whether or not a character is a hexadecimal digit.
     * 
     * @param c
     * A character.
     * 
     * @return
     * `true` if the character is a hexadecimal digit; otherwise,
     * `false`.
     */
    fun isHexDigit(c: Char): Boolean {
        return isDigit(c) ||
                isInRange(c, 'a', 'f') ||
                isInRange(c, 'A', 'F')
    }


    /**
     * A string containing all valid hexadecimal digits (0-9, A-F).
     */
    private const val HEX_DIGITS = "0123456789ABCDEF"


    /**
     * Converts an integer to its equivalent hexadecimal digit.
     * 
     * 
     * 
     * This method takes an integer `i` in the range of 0 to 15 (inclusive)
     * and returns the corresponding hexadecimal character from the standard set
     * of hexadecimal digits (0-9, A-F).
     * 
     * 
     * @param i
     * The integer to convert to a hexadecimal character.
     * 
     * @return
     * The hexadecimal character representation of `i` if `i`
     * is in the valid range (0-15).
     * 
     * @throws IllegalArgumentException
     * If `i` is less than 0 or greater than 15.
     */
    fun toHexDigit(i: Int): Char {
        if (i < 0) {
            throw newIAE("The input integer is less than 0.")
        }

        if (i > 16) {
            throw newIAE("The input integer is larger than 16.")
        }

        return HEX_DIGITS[i]
    }


    /**
     * Converts a hexadecimal digit to its equivalent integer value.
     * 
     * @param c
     * The hexadecimal character to convert.
     * i
     * @return
     * The integer value of the hexadecimal character, or -1 if 'c' is not
     * a valid hexadecimal character.
     */
    fun fromHexDigit(c: Char): Int {
        if (c in '0'..'9') {
            return c.code - '0'.code
        }

        if (c in 'a'..'f') {
            return 10 + c.code - 'a'.code
        }

        if (c in 'A'..'F') {
            return 10 + c.code - 'A'.code
        }

        return -1
    }


    /**
     * Removes dot segments from the given path as stated in
     * ["RFC 3986,
     * 5.2.4. Remove Dot Segments"](https://www.rfc-editor.org/rfc/rfc3986#section-5.2.4).
     * 
     * @param path
     * The path from which dot segments are to be removed.
     * 
     * @return
     * The path from which dot segments are removed.
     */
    fun removeDotSegments(path: String): String {
        // Initialize the input with the no-appended path components and the output
        // with the empty string.
        var input = path
        var output = ""

        // While the input is not empty, loop the following steps.
        while (input.length > 0) {
            // If the input begins with a prefix of "../" or "./", then
            // remove that prefix from the input;
            var m = Regex("^\\.?\\.\\/")
            if (m.find(input) != null) {
                input = m.replaceFirst(input,"")
                continue
            }

            // If the input begins with a prefix of "/./" or "/.", where
            // "." is a complete path segment, then replace that prefix
            // with "/" in the input.
            m = Regex("^\\/\\.(\\/|$)")
            if (m.find(input) != null) {
                input = m.replaceFirst(input,"/")
                continue
            }

            // If the input begins with a prefix of "/../" or "/..",
            // where ".." is a complete path segment, then replace that
            // prefix with "/" in the input and remove the last segment
            // and its preceding "/" (if any) from the output.
            m = Regex("^\\/\\.\\.(\\/|$)")
            if (m.find(input) != null) {
                input = m.replaceFirst(input, "/")
                output = dropLastSegment(output, true)
                continue
            }

            // If the input consists only of "." or "..", then remove
            // that from the input.
            m = Regex("^\\.?\\.$")
            if (m.find(input) != null) {
                input = m.replaceFirst(input, "")
                continue
            }

            // Move the first path segment in the input buffer to the
            // end of the output, including the initial "/" character
            // (if any) and any subsequent characters up to, but not
            // including, the next "/" character or the end of the input.
            m = Regex("^(?<firstsegment>\\/?[^/]*)(?<remaining>.*)$")
            val matchResult = m.find(input)
            if (matchResult != null) {
                input = matchResult.groups["remaining"]!!.value
                output += matchResult.groups["firstsegment"]!!.value
                continue
            }
        }

        return output
    }


    /**
     * Drops the last segment (= characters after the last slash) of a path and
     * optionally the last slash. If the path doesn't contain slash, an empty string
     * is returned.
     * 
     * @param path
     * The path.
     * 
     * @param dropLastSlash
     * Whether or not to drop the last slash if present.
     * 
     * @return The path from which the last segment is removed.
     */
    fun dropLastSegment(path: String, dropLastSlash: Boolean): String {
        // The regular expression for the target.
        val regex = if (dropLastSlash) "\\/?[^/]*$" else "[^/]*$"

        // Get a matcher for the pattern.
        val m = Regex(regex)


        // Find the target. (Any inputs matches the pattern.)
        m.find(path)

        // Drop the target.
        return m.replaceFirst(path,"")
    }


    /**
     * Create an `NullPointerException` with the given message.
     * 
     * @param msg
     * The error message.
     * 
     * @param args
     * The arguments referenced by the error message.
     * 
     * @return
     * An `NullPointerException` with the given message.
     */
    fun newNPE(msg: String, vararg args: Any?): NullPointerException {
        return NullPointerException(KotlinFormatter.format(msg, *args))
    }


    /**
     * Create an `IllegalArgumentException` with the given message.
     * 
     * @param msg
     * The error message.
     * 
     * @param args
     * The arguments referenced by the error message.
     * 
     * @return
     * An `IllegalArgumentException` with the given message.
     */
    fun newIAE(msg: String, vararg args: Any?): IllegalArgumentException {
        return IllegalArgumentException(KotlinFormatter.format(msg, *args))
    }


    /**
     * Create an `IllegalStateException` with the given message.
     * 
     * @param msg
     * The error message.
     * 
     * @param args
     * The arguments referenced by the error message.
     * 
     * @return
     * An `IllegalStateException` with the given message.
     */
    fun newISE(msg: String, vararg args: Any?): IllegalStateException {
        return IllegalStateException(KotlinFormatter.format(msg, *args))
    }
}
