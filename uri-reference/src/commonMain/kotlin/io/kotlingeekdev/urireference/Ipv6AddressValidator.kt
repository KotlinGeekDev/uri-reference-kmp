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
 * 
 * 
 * Validates the `host` component of a URI reference as an IPv6 address according
 * to the syntax defined in [
 * RFC 3986, Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A) as follows.
 * 
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`IPv6address =6( h16 ":" ) ls32             /"::" 5( h16 ":" ) ls32             / [h16 ] "::" 4( h16 ":" ) ls32             / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32             / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32             / [ *3( h16 ":" ) h16 ] "::"h16 ":"ls32             / [ *4( h16 ":" ) h16 ] "::"ls32             / [ *5( h16 ":" ) h16 ] "::"h16             / [ *6( h16 ":" ) h16 ] "::" h16= 1*4HEXDIG ls3= ( h16 ":" h16 ) / IPv4address IPv4address = dec-octet "." dec-octet "." dec-octet "." dec-octet dec-octet= DIGIT; 0-9             / %x31-39 DIGIT; 10-99             / "1" 2DIGIT; 100-199             / "2" %x30-34 DIGIT; 200-249             / "25" %x30-35; 250-255 `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class Ipv6AddressValidator {
    /**
     * Validates the `host` component of a URI reference as an IPv6 address.
     * 
     * @param ipv6Address
     * An IPv6 address value. Expected to be a value enclosed by the brackets
     * in a host value.
     * 
     * @throws IllegalArgumentException
     * If the IPv6 Address value is invalid.
     */
    fun validate(ipv6Address: String) {
        // RFC 3986, Appendix A. Collected ABNF for URI
        //
        //   IPv6address =                            6( h16 ":" ) ls32
        //               /                       "::" 5( h16 ":" ) ls32
        //               / [               h16 ] "::" 4( h16 ":" ) ls32
        //               / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
        //               / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
        //               / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
        //               / [ *4( h16 ":" ) h16 ] "::"              ls32
        //               / [ *5( h16 ":" ) h16 ] "::"              h16
        //               / [ *6( h16 ":" ) h16 ] "::"

        // Divide the string into up-to two parts by the first "::".

        val parts: Array<String?> = ipv6Address.split("::".toRegex(), limit = 2).toTypedArray()

        if (parts.size == 1) {
            // The number of the parts is 1, it means the host value doesn't
            // contain "::".
            validateIpv6WithoutDoubleColons(ipv6Address)
            return
        }

        if (parts.size == 2) {
            // The number of the parts is 2, it means the host value contains
            // "::".
            validateIpv6WithDoubleColons(parts[0]!!, parts[1]!!, ipv6Address)
            return
        }

        // We won't reach here.
    }


    private fun validateIpv6WithoutDoubleColons(ipv6Address: String) {
        // In this case, the input string must follow the following syntax.
        //
        //   6( h16 ":" ) ( h16 ":" h16 )
        //   6( h16 ":" ) IPv4address

        // Divide the input string by ':'.

        val segments: Array<String?> = ipv6Address.split(":".toRegex()).toTypedArray()

        if (segments.size == 7) {
            // This means the input string contains 6 colons. Then, the input
            // string must follow the syntax below.
            //
            //   6( h16 ":" ) IPv4address
            //
            validateH16Array(segments.copyOfRange( 0, 7), ipv6Address)
            validate(segments[6]!!)
            return
        }

        if (segments.size == 8) {
            // This means the input string contains 7 colons. Then, the input
            // string must follow the syntax below.
            //
            //   6( h16 ":" ) ( h16 ":" h16 )
            //
            validateH16Array(segments, ipv6Address)
            return
        }

        // The number of segments contained in the host value is incorrect.
        throw Utils.newIAE(
            "The host value \"[%s]\" is invalid because the content enclosed " +
                    "by brackets does not form a valid IPv6 address due to an incorrect " +
                    "number of segments.", ipv6Address
        )
    }


    private fun validateIpv6WithDoubleColons(
        valueBeforeDoubleColons: String, valueAfterDoubleColons: String, value: String?
    ) {
        // In this case, the input string contains "::", then it must follow one
        // of the below syntaxes.
        //
        //                         "::" 6( h16 ":" ) h16
        //   [               h16 ] "::" 5( h16 ":" ) h16
        //   [ *1( h16 ":" ) h16 ] "::" 4( h16 ":" ) h16
        //   [ *2( h16 ":" ) h16 ] "::" 3( h16 ":" ) h16
        //   [ *3( h16 ":" ) h16 ] "::" 2( h16 ":" ) h16
        //   [ *4( h16 ":" ) h16 ] "::"    h16 ":"   h16
        //   [ *5( h16 ":" ) h16 ] "::"    h16
        //   [ *6( h16 ":" ) h16 ] "::"
        //                         "::" 5( h16 ":" ) IPv4address
        //   [               h16 ] "::" 4( h16 ":" ) IPv4address
        //   [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) IPv4address
        //   [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) IPv4address
        //   [ *3( h16 ":" ) h16 ] "::"    h16 ":"   IPv4address
        //   [ *4( h16 ":" ) h16 ] "::"              IPv4address

        // Check the value before "::".

        val bitsBeforeDoubleColons =
            checkValueBeforeDoubleColons(valueBeforeDoubleColons, value)

        // Check the value after "::".
        val bitsAfterDoubleColons =
            checkValueAfterDoubleColons(valueAfterDoubleColons, value)

        // The total bits.
        val totalBits = bitsBeforeDoubleColons + bitsAfterDoubleColons

        // Ensure the total bits does not exceed the maximum value.
        if (totalBits > MAX_BITS) {
            throw Utils.newIAE(
                "The host value \"[%s]\" is invalid because the content enclosed " +
                        "by brackets does not form a valid IPv6 address, exceeding the " +
                        "maximum limit of 128 bits.", value
            )
        }
    }


    private fun checkValueBeforeDoubleColons(value: String, enclosed: String?): Int {
        if (value.isEmpty()) {
            // The first part is empty. Return 0 as the bits represented by the
            // first part.
            return 0
        }

        // Divide the part with ":".
        val segments: Array<String?> = value.split(":".toRegex()).toTypedArray()

        // Ensure each segment is H16.
        validateH16Array(segments, enclosed)

        // Return the total bits represented by the first part.
        return 16 * segments.size
    }


    private fun checkValueAfterDoubleColons(part: String, enclosed: String?): Int {
        if (part.isEmpty()) {
            // The second part is empty. Return 0 as the bits represented by the
            // second part.
            return 0
        }

        // Divide the segment with ":".
        val segments: Array<String?> = part.split(":".toRegex()).toTypedArray()

        try {
            // Try to ensure all the segment parts are 16-bit pieces.
            validateH16Array(segments, enclosed)

            // Calculate the total bits represented by the second part.
            return 16 * segments.size
        } catch (e: IllegalArgumentException) {
            // If the check above fails, ensure all the segments except for the
            // last segment are H16 and the last segment is ipv4.
            validateH16Array(segments.copyOfRange(0, segments.size), enclosed)
            Ipv4AddressValidator().validate(segments[segments.size - 1]!!)

            // Calculate the total bits represented by the second part.
            return 16 * (segments.size - 1) + 32
        }
    }


    private fun validateH16Array(segments: Array<String?>, enclosed: String?) {
        // Ensure each segment is a 16-bit piece.
        for (i in segments.indices) {
            validateH16(segments[i]!!, enclosed)
        }
    }


    private fun validateH16(segment: String, enclosed: String?) {
        // Ensure the segment is not empty.
        if (segment.isEmpty()) {
            throw Utils.newIAE(
                "The host value \"[%s]\" is invalid because the content enclosed " +
                        "by brackets does not form a valid IPv6 address due to an empty " +
                        "segment.", enclosed
            )
        }

        // Ensure the length of the segment doesn't exceed 4.
        if (segment.length > 4) {
            throw Utils.newIAE(
                "The host value \"[%s]\" is invalid because the content enclosed " +
                        "by brackets does not form a valid IPv6 address due to the segment " +
                        "\"%s\" exceeding the maximum limit of 4 characters.", enclosed, segment
            )
        }

        // Ensure each character in the segment is a hex digit.
        for (i in 0..<segment.length) {
            val c = segment.get(i)

            if (!Utils.isHexDigit(c)) {
                throw Utils.newIAE(
                    "The host value \"[%s]\" is invalid because the content enclosed " +
                            "by brackets does not form a valid IPv6 address due to the segment \"%s\", " +
                            "containing an invalid character \"%s\" at the index %d.", enclosed, segment, c, i
                )
            }
        }
    }

    companion object {
        /**
         * The maximum value for the total bits represented by an IPv6 address value
         * containing double colons.
         */
        private val MAX_BITS = 16 * 7
    }
}
