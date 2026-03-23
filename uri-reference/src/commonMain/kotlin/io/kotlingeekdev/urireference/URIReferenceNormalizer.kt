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
 * Normalizes a URI reference according to [
 * RFC 3986, Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * 
 * Examples:
 * <pre>`// Normalize a URI reference. URIReference normalized = new URIReferenceNormalizer()                               .normalize("hTTp://example.com:80/a/b/c/../d/"); System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.getAuthority().getUserinfo());// null System.out.println(uriRef.getAuthority().getHost().getType());// "REGNAME" System.out.println(uriRef.getAuthority().getHost().getValue()); // "example.com" System.out.println(uriRef.getAuthority().getPort());// -1 System.out.println(uriRef.getPath());// "/a/b/d/" System.out.println(uriRef.getQuery());// null System.out.println(uriRef.getFragment());// null `</pre>
 * 
 * @see [RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class URIReferenceNormalizer {
    /**
     * Normalizes a URI reference [
     * RFC 3986, Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * 
     * 
     * This method does not modify the state of the give [URIReference] instance.
     * Instead, it creates a new [URIReference] instance and initializes it
     * with the information about the normalized URI reference.
     * 
     * 
     * 
     * 
     * Note that this method throws an `IllegalStateException` if the URI
     * reference specified by the `uriRef` argument has not been resolved
     * yet.
     * 
     * 
     * @param uriRef
     * The URI reference to normalize.
     * 
     * @return
     * A new `URIReference` instance representing the normalized
     * URI reference.
     * 
     * @throws IllegalStateException
     * If the URI reference specified by the `uriRef` argument has
     * not been resolved yet.
     */
    fun normalize(uriRef: URIReference?): URIReference {
        // Validate the URI reference.
        validate(uriRef)

        // The parse result.
        val res = URIReference.ProcessResult()

        // Set the charset.
        res.charset = uriRef!!.charset

        // The normalized URI reference is always a URI.
        res.relativeReference = false

        // Process the scheme.
        processScheme(res, uriRef)

        // Process the authority.
        processAuthority(res, uriRef)

        // Process the path.
        processPath(res, uriRef)

        // Process the query.
        processQuery(res, uriRef)

        // Process the fragment.
        processFragment(res, uriRef)

        // Build a URI reference instance.
        return res.toURIReference()
    }


    private fun validate(uriRef: URIReference?) {
        // Ensure the input URI reference is not null.
        if (uriRef == null) {
            throw Utils.newNPE("The URI reference must not be null.")
        }

        // Ensure the URI reference has been resolved according to the following
        // requirement.
        //
        //   RFC 3986, 5.2.1. Pre-parse the Base URI
        //
        //     A URI reference must be transformed to its target URI before
        //     it can be normalized.
        if (uriRef.isRelativeReference) {
            throw Utils.newISE("A relative references must be resolved before it can be normalized.")
        }
    }


    private fun processScheme(res: URIReference.ProcessResult, uriRef: URIReference) {
        // Normalize the scheme.
        res.scheme = SchemeNormalizer().normalize(uriRef.scheme!!)
    }


    private fun processAuthority(res: URIReference.ProcessResult, uriRef: URIReference) {
        // Normalize the authority.
        res.authority = AuthorityNormalizer().normalize(
            uriRef.authority, uriRef.charset!!, res.scheme
        )
    }


    private fun processPath(res: URIReference.ProcessResult, uriRef: URIReference) {
        // Normalize the path.
        res.path = PathNormalizer().normalize(
            uriRef.path, uriRef.charset!!, uriRef.hasAuthority()
        )
    }


    private fun processQuery(res: URIReference.ProcessResult, uriRef: URIReference) {
        // Normalize the query.
        res.query = QueryNormalizer().normalize(
            uriRef.query, uriRef.charset!!
        )
    }


    private fun processFragment(res: URIReference.ProcessResult, uriRef: URIReference) {
        // Normalize the fragment.
        res.fragment = FragmentNormalizer().normalize(
            uriRef.fragment, uriRef.charset!!
        )
    }
}
