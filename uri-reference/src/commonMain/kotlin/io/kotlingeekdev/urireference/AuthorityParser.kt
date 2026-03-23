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
import kotlin.text.get


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * 
 * 
 * Parses a given string as the `authority` component of a URI reference,
 * according to [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If
 * parsing succeeds, it creates an [Authority] object. If parsing fails due
 * to invalid input string, it throws an `IllegalArgumentException`.
 * 
 * 
 * @see [RFC 3986 - Uniform Resource
 * Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
internal class AuthorityParser {
    /**
     * Inner class representing the result of the Authority building process.
     * This class holds intermediate values of the Authority components during the
     * parse process.
     */
    private class ParseResult : Authority.ProcessResult() {
        var matcher: MatchResult? = null
    }


    /**
     * Parses a given string as the `authority` component of a URI reference,
     * according to [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986)
     * If parsing succeeds, this method creates an [Authority] instance. If
     * parsing fails due to invalid input string, it throws an `IllegalArgumentException`.
     * 
     * @param authority
     * The input string to parse as the `authority` component of a
     * URI reference.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the `authority` parameter.
     * 
     * @return
     * The `Authority` object representing the parsed `authority`
     * component.
     * 
     * @throws NullPointerException
     * If the value of the `authority` parameter or the `charset`
     * parameter is `null`.
     * 
     * @throws IllegalArgumentException
     * If the value of the `authority` parameter is invalid as the
     * `authority` of a URI reference.
     * 
     * @see [
     * RFC 3986 Uniform Resource Identifier
    ](https://www.rfc-editor.org/rfc/rfc3986.section-4.2) */
    fun parse(authority: String?, charset: Charset?): Authority? {
        if (authority == null) {
            // The input string doesn't contain an authority.
            return null
        }

        // The parse result.
        val res = ParseResult()

        // Process the authority.
        processAuthority(res, authority)

        // Process the userinfo.
        processUserinfo(res, charset)

        // Process the host.
        processHost(res, charset)

        // Process the port.
        processPort(res)

        // Return the parser result.
        return res.toAuthority()
    }


    private fun processAuthority(res: ParseResult, authority: String) {
        // Get a matcher to match the input string as an authority.
        val matcher = PATTERN_AUTHORITY.matchEntire(authority)

        // Match the input string as an authority..
        if (!authority.matches(PATTERN_AUTHORITY)) {
            // The input string is invalid as an authority.
            throw Utils.newIAE("The input \"%s\" is invalid as an authority.", authority)
        }

        // Set the matcher.
        res.matcher = matcher
    }


    private fun processUserinfo(res: ParseResult, charset: Charset?) {
        // The raw userinfo.
        val rawUserinfo = res.matcher!!.groups["userinfo"]?.value

        // Validate the raw userinfo.
        UserinfoValidator().validate(rawUserinfo, charset)

        // Set it to the result.
        res.userinfo = rawUserinfo
    }


    private fun processHost(res: ParseResult, charset: Charset?) {
        // The raw host.
        val rawHost = res.matcher!!.groups["host"]?.value

        // Parse the raw host value into a Host instance.
        res.host = Host.parse(rawHost, charset)
    }


    private fun processPort(res: ParseResult) {
        // The raw port.
        val rawPort = res.matcher!!.groups["port"]?.value

        // Validate the raw port.
        PortValidator().validate(rawPort)

        // Parse the raw port into an int value.
        res.port = parsePort(rawPort)
    }


    private fun parsePort(rawPort: String?): Int {
        if (rawPort == null || rawPort.isEmpty()) {
            // If the "rawPort" value is null, it means the URI reference doesn't
            // contain a port. If it is empty, it means the input string contains
            // a colon (":") delimiter for the port value but the port value is
            // empty. In these cases, the value of the parsed authority component
            // is set to -1 (the default value).
            return -1
        }

        try {
            // Parse the extracted port value into an int value and set it as the
            // value of "newAuthority.port".
            return rawPort.toInt()
        } catch (e: NumberFormatException) {
            // The number in the port is invalid.
            // NOTE: We will reach this point if, for instance, the number is too
            // large as an int value.
            throw Utils.newIAE("The port value \"%s\" is invalid as a number.", rawPort)
        }
    }

    companion object {
        /**
         * The regular expression for parsing an authority.
         */
        private val PATTERN_AUTHORITY: Regex = Regex(
            "((?<userinfo>[^@]*)@)?(?<host>(\\[[^]]*\\])|[^:]*)?(:(?<port>.*))?"
        )
    }
}
