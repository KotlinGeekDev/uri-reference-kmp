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


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * Resolves a URI reference against a base URI according to [
 * RFC 3986, Section 5: Reference Resolution](https://www.rfc-editor.org/rfc/rfc3986#section-5).
 * 
 * 
 * Examples:
 * <pre>`// Parse a relative reference. URIReference relRef = URIReference.parse("/a/b"); // Parse a base URI. URIReference baseUri = URIReference.parse("http://example.com"); // Resolve the relative reference against the base URI. URIReference resolved = new URIReferenceResolver(relRef, baseUri); System.out.println(resolved.isRelativeReference());// false System.out.println(resolved.getScheme());// "http" System.out.println(resolved.hasAuthority());// true System.out.println(resolved.getAuthority().toString()); // "example.com" System.out.println(resolved.getUserinfo());// null System.out.println(resolved.getHost().getType());// "REGNAME" System.out.println(resolved.getHost().getValue());// "example.com" System.out.println(resolved.getPort());// -1 System.out.println(resolved.getPath());// "/a/b" System.out.println(resolved.getQuery());// null System.out.println(resolved.getFragment());// null `</pre>
 * 
 * @see [RFC 3986, Section 5:
 * Reference Resolution](https://www.rfc-editor.org/rfc/rfc3986.section-5)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class URIReferenceResolver {
    /**
     * Resolves a URI reference against a base URI.
     * 
     * @param uriRef
     * A URI reference to resolve against the value of `baseUriRef`.
     * 
     * @param baseUriRef
     * A base URI against which the value of `uriRef` is resolved.
     * 
     * @return The URI reference obtained by resolving `uriRef` against
     * `baseUriRef`.
     * 
     * @throws NullPointerException
     * If `uriRef` or `baseUriRef` is `null`.
     * 
     * @throws IllegalStateException
     * If this URI reference is not an absolute URI.
     */
    fun resolve(uriRef: URIReference, baseUriRef: URIReference): URIReference {
        // Validate the arguments.
        validate(uriRef, baseUriRef)

        // The resolution result.
        val res = URIReference.ProcessResult()

        // Set the charset.
        res.charset = uriRef.charset

        // The resolved URI reference is always a URI.
        res.relativeReference = false

        // Resolve the URI Reference against the base URI.
        process(res, uriRef, baseUriRef)

        // Return the result.
        return res.toURIReference()
    }


    private fun validate(uriRef: URIReference, baseUriRef: URIReference) {
        // Ensure the URI reference to be resolved is not null.
        if (uriRef == null) {
            throw Utils.newNPE("The URI reference to be resolved must not be null.")
        }

        // Ensure the base URI is not null.
        if (baseUriRef == null) {
            throw Utils.newNPE("The base URI reference must not be null.")
        }

        // Check the charset of both URI reference.
        if (uriRef.charset !== baseUriRef.charset) {
            throw Utils.newIAE(
                "The charset of the target URI reference doesn't match the charset of the base URI."
            )
        }

        // Ensure this URI reference can be used as a base URI.
        //
        // RFC 3986, 5.1. Establishing a Base URI
        //
        //   A base URI must conform to the <absolute-URI> syntax rule
        //   (Section 4.3). If the base URI is obtained from a URI reference,
        //   then that reference must be converted to absolute form and
        //   stripped of any fragment component prior to its use as a
        //   base URI.
        //
        // RFC 3986, 4.3.  Absolute URI
        //
        //   Some protocol elements allow only the absolute form of a URI without
        //   a fragment identifier.  For example, defining a base URI for later
        //   use by relative references calls for an absolute-URI syntax rule that
        //   does not allow a fragment.
        //
        //     absolute-URI  = scheme ":" hier-part [ "?" query ]
        if (baseUriRef.scheme == null) {
            throw Utils.newISE("The base URI must have a scheme.")
        }

        if (baseUriRef.fragment != null) {
            throw Utils.newISE("The base URI must not have a fragment.")
        }
    }


    private fun process(
        res: URIReference.ProcessResult, uriRef: URIReference, baseUriRef: URIReference
    ) {
        if (uriRef.scheme != null) {
            processOnNonNullScheme(res, uriRef)
        } else {
            processOnNullScheme(res, uriRef, baseUriRef)
        }

        res.fragment = uriRef.fragment
    }


    private fun processOnNonNullScheme(res: URIReference.ProcessResult, uriRef: URIReference) {
        res.scheme = uriRef.scheme
        res.authority = uriRef.authority
        res.path = Utils.removeDotSegments(uriRef.path!!)
        res.query = uriRef.query
    }


    private fun processOnNullScheme(
        res: URIReference.ProcessResult, uriRef: URIReference, baseUriRef: URIReference
    ) {
        if (uriRef.authority != null) {
            processOnNonNullAuthority(res, uriRef, baseUriRef)
        } else {
            processOnNullAuthority(res, uriRef, baseUriRef)
        }

        res.scheme = baseUriRef.scheme
    }


    private fun processOnNonNullAuthority(
        res: URIReference.ProcessResult, uriRef: URIReference, baseUriRef: URIReference?
    ) {
        res.authority = uriRef.authority
        res.path = Utils.removeDotSegments(uriRef.path!!)
        res.query = uriRef.query
    }


    private fun processOnNullAuthority(
        res: URIReference.ProcessResult, uriRef: URIReference, baseUriRef: URIReference
    ) {
        if (uriRef.path?.isNotEmpty() == true) {
            processOnNonNullPath(res, uriRef, baseUriRef)
        } else {
            processOnNullPath(res, uriRef, baseUriRef)
        }

        res.authority = baseUriRef.authority
    }


    private fun processOnNonNullPath(
        res: URIReference.ProcessResult, uriRef: URIReference, baseUriRef: URIReference
    ) {
        if (uriRef.path?.startsWith("/") == true) {
            res.path = Utils.removeDotSegments(uriRef.path)
        } else {
            res.path = Utils.removeDotSegments(
                mergePath(
                    uriRef.path!!, baseUriRef.path!!, baseUriRef.hasAuthority()
                )
            )
        }

        res.query = uriRef.query
    }


    private fun processOnNullPath(
        res: URIReference.ProcessResult, uriRef: URIReference, baseUriRef: URIReference
    ) {
        res.path = baseUriRef.path

        if (uriRef.query != null) {
            res.query = uriRef.query
        } else {
            res.query = baseUriRef.query
        }
    }

    companion object {
        /**
         * Merges a relative-path reference with the path of a base URI according to
         * [RFC 3986,
         * 5.2.3. Merge Paths](https://www.rfc-editor.org/rfc/rfc3986#section-5.2.3).
         * 
         * @param uriRefPath
         * The path of a URI reference to merge
         * 
         * @param basePath
         * The path of a base URI to merge the
         * 
         * @param hasAuthority
         * Whether the base URI has an authority or not.
         * 
         * @return The merged path.
         */
        private fun mergePath(
            uriRefPath: String, basePath: String, hasAuthority: Boolean
        ): String {
            // NOTE:
            //     uriRefPath.length() > 0
            //     uriRefPath.startsWith("/") != false

            if (hasAuthority && basePath.isEmpty()) {
                return "/$uriRefPath"
            } else {
                return Utils.dropLastSegment(basePath, false) + uriRefPath
            }
        }
    }
}
