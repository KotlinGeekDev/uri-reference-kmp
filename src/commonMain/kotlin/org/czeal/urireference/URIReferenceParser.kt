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


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * Parses a given string as a URI reference according to [
 * RFC 3986](https://www.rfc-editor.org/rfc/rfc3986) and creates a [URIReference] instance, representing either
 * a URI or a relative reference.
 * 
 * 
 * Examples:
 * <pre>`// Parse a URI. URIReference uriRef = new URIReferenceParser().parse("http://example.com/a"); System.out.println(uriRef.isRelativeReference());// false System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.hasAuthority());// true System.out.println(uriRef.getAuthority().toString()); // "example.com" System.out.println(uriRef.getUserinfo());// null System.out.println(uriRef.getHost().getType());// "REGNAME" System.out.println(uriRef.getHost().getValue());// "example.com" System.out.println(uriRef.getPort());// -1 System.out.println(uriRef.getPath());// "/a" System.out.println(uriRef.getQuery());// null System.out.println(uriRef.getFragment());// null `</pre>
 * 
 * @see [RFC 3986 - Uniform Resource
 * Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
internal class URIReferenceParser {
    /**
     * Inner class representing the result of the URI reference parsing process.
     * This class holds intermediate values of the URI components during the parse
     * process.
     */
    private class ParseResult : URIReference.ProcessResult() {
        var matcher: MatchResult? = null
    }


    /**
     * Parses the input string as a [
     * URI reference](https://www.rfc-editor.org/rfc/rfc3986#section-4.1) based on [
     * RFC 3986](https://www.rfc-editor.org/rfc/rfc3986).
     * 
     * 
     * 
     * The `uriRef` value must be a string representing a [
     * URI](https://www.rfc-editor.org/rfc/rfc3986#section-3) or [
     * relative reference](https://www.rfc-editor.org/rfc/rfc3986#section-4.2). If the `uriRef` value is invalid as a URI reference,
     * an `InvalidArgumentException` will be thrown.
     * 
     * 
     * @param uriRef
     * Required. The input string to parse as a URI reference.
     * 
     * @param charset
     * Required. The charset used in the input string.
     * 
     * @return
     * The URI reference obtained by parsing the input string.
     * 
     * @throws NullPointerException
     * If `uriRef` or `charset` is `null`.
     * 
     * @throws IllegalArgumentException
     * If the value of `uriRef` is invalid as a URI reference.
     * 
     * @see [RFC 3986
     * Uniform Resource Identifier
    ](https://www.rfc-editor.org/rfc/rfc3986.section-4.2) */
    fun parse(uriRef: String?, charset: Charset): URIReference {
        // Validate the arguments.
        validate(uriRef, charset)

        // The parse result.
        val res = ParseResult()

        // Set the charset.
        res.charset = charset

        // Match the input string as a URI reference.
        processInput(res, uriRef)

        // Process the authority.
        processAuthority(res)

        // Process the path.
        processPath(res)

        // Process the query.
        processQuery(res)

        // Process the fragment.
        processFragment(res)

        // Build a URI reference instance.
        return res.toURIReference()
    }


    private fun validate(uriRef: String?, charset: Charset?) {
        // Ensure the input string is not null.
        if (uriRef == null) {
            throw Utils.newNPE("The input string must not be null.")
        }

        // Ensure the charset is not null.
        if (charset == null) {
            throw Utils.newNPE("The charset must not be null.")
        }
    }


    private fun processInput(res: ParseResult, uriRef: String?) {
        // 4.1.  URI Reference
        //
        //   A URI-reference is either a URI or a relative reference.
        //   If the URI-reference's prefix does not match the syntax of a scheme
        //   followed by its colon separator, then the URI-reference is a relative
        //   reference.

        // Get a matcher to match the input string as a URI.

        val matcher = PATTERN_URI.matchEntire(uriRef!!)

        // If the input string  matches the URI pattern.
        if (uriRef.matches(PATTERN_URI)) {
            // The raw scheme.
            val scheme = matcher!!.groups["scheme"]?.value!!

            // If the raw scheme is valid.
            if (isSchemeValid(scheme)) {
                // The input string  starts with a valid scheme. Then, we can consider
                // the input string  as a URI.
                res.matcher = matcher
                res.scheme = scheme
                res.relativeReference = false
                return
            }
        }

        // We reach here if the input string  doesn't start with a valid scheme followed
        // by a colon. In this case, we consider the input string  as a relative reference.
        matchAsRelativeReference(res, uriRef)
    }


    private fun isSchemeValid(scheme: String): Boolean {
        try {
            // Validate the scheme.
            SchemeValidator().validate(scheme)
        } catch (e: IllegalArgumentException) {
            // The input string starts with a string followed by a colon but it is
            // invalid as a scheme. Then, we consider the input string  as a relative
            // reference.
            return false
        }

        // The scheme value is valid.
        return true
    }


    private fun matchAsRelativeReference(res: ParseResult, uriRef: String?) {
        // Get a matcher to match the input string as a relative reference.
        val matcher = PATTERN_REGEX_RELATIVE_REFERENCE.matchEntire(uriRef!!)

        // If the input string doesn't match the relative reference pattern.
        if (!uriRef.matches(PATTERN_REGEX_RELATIVE_REFERENCE)) {
            // The input string is invalid as a relative reference.
            throw Utils.newIAE(
                "The input string \"%s\" is invalid as a relative reference.", uriRef
            )
        }

        // OK. The input string is a valid relative reference.
        res.matcher = matcher
        res.relativeReference = true
    }


    private fun processAuthority(res: ParseResult) {
        // The raw authority.
        val authority = res.matcher!!.groups["authority"]?.value

        // Parse the raw authority as an Authority instance.
        res.authority = Authority.parse(authority, res.charset)
    }


    private fun processPath(res: ParseResult) {
        // The raw path.
        val path = res.matcher!!.groups["path"]?.value

        // Validate the raw path.
        PathValidator().validate(
            path, res.charset!!, res.relativeReference, res.authority != null
        )

        // Set it to the result.
        res.path = path
    }


    private fun processQuery(res: ParseResult) {
        // The raw query.
        val query = res.matcher!!.groups["query"]?.value

        // Validate the raw query.
        QueryValidator().validate(query, res.charset!!)

        // Set it to the result.
        res.query = query
    }


    private fun processFragment(res: ParseResult) {
        // The raw fragment.
        val fragment = res.matcher!!.groups["fragment"]?.value

        // Validate the raw fragment.
        FragmentValidator().validate(fragment, res.charset!!)

        // Set it to the result.
        res.fragment = fragment
    }

    companion object {
        /**
         * The pattern for parsing the five components of a URI, according to "RFC
         * 3986, Appendix B. Parsing a URI Reference with a Regular Expression".
         */
        private val PATTERN_URI: Regex = Regex(
            "(?<scheme>[^:/?#]+):(\\/\\/(?<authority>[^/?#]*))?(?<path>[^?#]*)(\\?(?<query>[^#]*))?(#(?<fragment>.*))?"
        )


        /**
         * The pattern for parsing the five components of a relative reference, according
         * to "RFC 3986, Appendix B. Parsing a URI Reference with a Regular Expression".
         */
        private val PATTERN_REGEX_RELATIVE_REFERENCE: Regex = Regex(
            "(\\/\\/(?<authority>[^/?#]*))?(?<path>[^?#]*)(\\?(?<query>[^#]*))?(#(?<fragment>.*))?"
        )
    }
}
