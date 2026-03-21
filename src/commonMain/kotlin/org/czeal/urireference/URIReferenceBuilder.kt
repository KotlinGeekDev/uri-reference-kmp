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
import com.fleeksoft.charset.Charsets
import kotlin.jvm.JvmStatic


/**
 * A builder class for constructing URI references.
 * 
 * 
 * 
 * This class provides a fluent API to build a [org.czeal.urireference.URIReference] object incrementally
 * by setting its various components such as `scheme`, `userinfo`,
 * `host`, `port`, `path`, `query`, and `fragment`.
 * 
 * 
 * 
 * 
 * This builder supports both absolute and relative URI references.
 * 
 * 
 * 
 * Examples:
 * <pre>`//--------------------------------------------------------------------------- // Build a URI reference. //--------------------------------------------------------------------------- URIReference uriRef = new URIReferenceBuilder()                           .setScheme("http")                           .setHost("example.com")                           .setPath("/a/b/c")                           .appendQueryParam("k1", "v1")                           .build(); System.out.println(uriRef.toString());// "http://example.com/a/b/c?k1=v1" System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.getAuthority());// "example.com" System.out.println(uriRef.getUserinfo());// null System.out.println(uriRef.getAuthority().getHost().getType());// "REGNAME" System.out.println(uriRef.getAuthority().getHost().getValue()); // "example.com" System.out.println(uriRef.getPort());// -1 System.out.println(uriRef.getPath());// "/a/b/c" System.out.println(uriRef.getQuery());// "k1=v1" System.out.println(uriRef.getFragment());// null //--------------------------------------------------------------------------- // Build a URI reference from another URI reference. //--------------------------------------------------------------------------- URIReference uriRef = URIReferenceBuilder                           .fromURIReference("http://example.com/a/b/c?k1=v1")                           .appendPath("d", "e", "f")                           .appendQueryParam("k2", "v2")                           .build(); System.out.println(uriRef.toString());// "http://example.com/a/b/c/d/e/f?k1=v1&k2=v2" System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.getAuthority().toString());// "example.com" System.out.println(uriRef.getUserinfo());// null System.out.println(uriRef.getAuthority().getHost().getType());// "REGNAME" System.out.println(uriRef.getAuthority().getHost().getValue()); // "example.com" System.out.println(uriRef.getPort());// -1 System.out.println(uriRef.getPath());// "/a/b/c/d/e/f" System.out.println(uriRef.getQuery());// "k1=v1&k2=&v2" System.out.println(uriRef.getFragment());// null `</pre>
 * 
 * @see [RFC 3986 - Uniform Resource
 * Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
class URIReferenceBuilder {
    /**
     * The charset for percent-encoding characters (e.g. reserved characters) in
     * the resultant URI reference.
     */
    private var charset: Charset? = null


    /**
     * The scheme of the resultant URI reference.
     */
    private var scheme: String? = null


    /**
     * The userinfo of the resultant URI reference.
     */
    private var userinfo: String? = null


    /**
     * The host of the resultant URI reference.
     */
    private var host: String? = null


    /**
     * The port of the resultant URI reference.
     */
    private var port = -1


    /**
     * The path segments of the resultant URI reference.
     */
    private var pathSegments: PathSegments? = null


    /**
     * The query parameters of the resultant URI reference.
     */
    private var queryParams: QueryParams? = null


    /**
     * The fragment of the resultant URI reference.
     */
    private var fragment: String? = null


    /**
     * Whether the authority is required for the resultant URI reference or not.
     */
    private var authorityRequired = true


    /**
     * Sets information about a given [URIReferenceBuilder] instance. Specifically,
     * it copies the following information from the given `URIReferenceBuilder`
     * instance to this instance.
     * 
     * 
     *  * charset
     *  * scheme
     *  * userinfo
     *  * host
     *  * port
     *  * query
     *  * fragment
     * 
     * 
     * @param uriRef
     * A [org.czeal.urireference.URIReference] instance.
     * 
     * @return
     * `this` object.
     */
    fun uriRef(uriRef: URIReference?): URIReferenceBuilder {
        if (uriRef == null) {
            throw Utils.newNPE("The URI reference must not be null.")
        }

        charset = uriRef.charset
        scheme = uriRef.scheme
        userinfo = uriRef.userinfo
        host = uriRef.host?.value
        port = uriRef.port
        pathSegments = PathSegments.parse(uriRef.path)
        queryParams = QueryParams.parse(uriRef.query)
        fragment = uriRef.fragment

        return this
    }


    /**
     * Sets the charset used in the resultant URI reference.
     * 
     * 
     * 
     * This method replaces existing charset value with the given value.
     * 
     * 
     * @param charset
     * The charset.
     * 
     * @return
     * `this` object.
     */
    fun setCharset(charset: Charset?): URIReferenceBuilder {
        this.charset = charset

        return this
    }


    /**
     * Sets the scheme.
     * 
     * 
     * 
     * This method replaces existing `scheme` value with the given value.
     * 
     * 
     * @param scheme
     * The scheme of the resultant URI reference. Specifying `null`
     * for this property unsets the scheme.
     * 
     * @return
     * `this` object.
     */
    fun setScheme(scheme: String?): URIReferenceBuilder {
        this.scheme = scheme

        return this
    }


    /**
     * Sets the userinfo.
     * 
     * 
     * 
     * This method replaces existing `userinfo` value with the given value.
     * 
     * 
     * @param userinfo
     * The userinfo of the resultant URI reference. Specifying `null`
     * for this property unsets the userinfo.
     * 
     * @return
     * `this` object.
     */
    fun setUserinfo(userinfo: String?): URIReferenceBuilder {
        this.userinfo = userinfo

        return this
    }


    /**
     * Sets the host.
     * 
     * 
     * 
     * This method replaces existing `host` value with the given value.
     * 
     * 
     * @param host
     * The host of the resultant URI reference. Specifying `null`
     * for this property unsets the host.
     * 
     * @return
     * `this` object.
     */
    fun setHost(host: String?): URIReferenceBuilder {
        this.host = host

        return this
    }


    /**
     * Sets the port.
     * 
     * 
     * 
     * This method replaces existing `port` value with the given value.
     * 
     * 
     * @param port
     * The port of the resultant URI reference. Specifying `null`
     * for this property unsets the port.
     * 
     * @return
     * `this` object.
     */
    fun setPort(port: Int): URIReferenceBuilder {
        this.port = port

        return this
    }


    /**
     * Sets the path.
     * 
     * 
     * 
     * This method replaces existing `path` value with the given value.
     * 
     * 
     * @param path
     * The path of the resultant URI reference. Specifying `null`
     * for this property unsets the path.
     * 
     * @return
     * `this` object.
     */
    fun setPath(path: String?): URIReferenceBuilder {
        this.pathSegments = PathSegments.parse(path)

        return this
    }


    /**
     * Sets the query.
     * 
     * 
     * 
     * This method replaces the existing `query` value with the given value.
     * 
     * 
     * @param query
     * The query of the resultant URI reference. Specifying `null`
     * for this property unsets the query.
     * 
     * @return
     * `this` object.
     */
    fun setQuery(query: String?): URIReferenceBuilder {
        this.queryParams = QueryParams.parse(query)

        return this
    }


    /**
     * Sets the fragment.
     * 
     * 
     * 
     * This method replaces existing `fragment` value with the given value.
     * 
     * 
     * @param fragment
     * The fragment of the resultant URI reference. Specifying `null`
     * for this property unsets the fragment.
     * 
     * @return
     * `this` object.
     */
    fun setFragment(fragment: String?): URIReferenceBuilder {
        this.fragment = fragment

        return this
    }


    /**
     * Determines whether or not the authority is required in the resultant URI
     * reference.
     * 
     * @param authorityRequired
     * If `true`, the authority part is present in the resultant URI
     * reference; otherwise, it will be omitted.
     * 
     * @return
     * `this` object.
     */
    fun setAuthorityRequired(authorityRequired: Boolean): URIReferenceBuilder {
        this.authorityRequired = authorityRequired

        return this
    }


    /**
     * Appends a new query parameter to the resultant URI reference.
     * 
     * @param key
     * The key of the new query parameter.
     * 
     * @param value
     * The value of the new query parameter.
     * 
     * @return
     * `this` object.
     */
    fun appendQueryParam(key: String?, value: String?): URIReferenceBuilder {
        if (queryParams == null) {
            queryParams = QueryParams()
        }

        queryParams!!.add(key, value)

        return this
    }


    /**
     * Replaces the value(s) of the query parameter(s) specified by the key with
     * a new value.
     * 
     * @param key
     * The key of the query parameter(s) whose value(s) is to be replaced.
     * 
     * @param value
     * A new value of the new query parameter.
     * 
     * @return
     * `this` object.
     */
    fun replaceQueryParam(key: String?, value: String?): URIReferenceBuilder {
        if (queryParams != null) {
            queryParams!!.replace(key, value)
        }

        return this
    }


    /**
     * Removes the query parameter(s) specified by a key.
     * 
     * @param key
     * The key of the query parameter(s) to be removed.
     * 
     * @return
     * `this` object.
     */
    fun removeQueryParam(key: String?): URIReferenceBuilder {
        if (queryParams != null) {
            queryParams!!.remove(key)
        }

        return this
    }


    /**
     * Appends a path segment.
     * 
     * @param segment
     * A path segment.
     * 
     * @return
     * `this` object.
     */
    fun appendPathSegments(segment: List<String?>?): URIReferenceBuilder {
        if (pathSegments == null) {
            pathSegments = PathSegments()
        }

        pathSegments = pathSegments!!.add(segment?.toList())

        return this
    }


    /**
     * Builds a URI reference.
     * 
     * @return
     * An [org.czeal.urireference.URIReference] object representing the resultant URI reference.
     */
    fun build(): URIReference {
        // The resultant URI reference.
        val res = URIReference.ProcessResult()

        // Process the charset.
        processCharset(res)

        // Process the scheme.
        processScheme(res)

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


    private fun processCharset(res: URIReference.ProcessResult) {
        // Set the charset.
        res.charset = if (charset != null) charset else Charsets.UTF8
    }


    private fun processScheme(res: URIReference.ProcessResult) {
        if (scheme != null) {
            // Validate the scheme value.
            SchemeValidator().validate(scheme!!)

            // Set the scheme.
            res.scheme = scheme

            // The URI reference is a URI.
            res.relativeReference = false
        } else {
            // The URI reference is a relative reference.
            res.relativeReference = true
        }
    }


    private fun processAuthority(res: URIReference.ProcessResult) {
        // If the authority is not required.
        if (!authorityRequired) {
            return
        }

        // Create an authority.
        res.authority = AuthorityBuilder()
            .setCharset(charset)
            .setUserinfo(userinfo)
            .setHost(host)
            .setPort(port)
            .build()
    }


    private fun processPath(res: URIReference.ProcessResult) {
        // Convert the path segments to a string.
        val path = if (pathSegments == null) null else pathSegments.toString()

        // Validate the path.
        PathValidator().validate(
            path, res.charset!!, res.relativeReference, res.authority != null
        )

        // Set the path.
        res.path = path
    }


    private fun processQuery(res: URIReference.ProcessResult) {
        // Convert the query parameters to a string.
        val query = if (queryParams == null || queryParams!!.isEmpty) null else queryParams.toString()

        // Validate the query.
        QueryValidator().validate(query, res.charset!!)

        // Set the query.
        res.query = query
    }


    private fun processFragment(res: URIReference.ProcessResult) {
        // Validate the fragment.
        FragmentValidator().validate(fragment, res.charset!!)

        // Set the fragment.
        res.fragment = fragment
    }

    companion object {
        /**
         * Creates a [URIReferenceBuilder] instance with a given string representing
         * a URI reference. This method copies the following information to the created
         * instance:
         * 
         * 
         *  * charset
         *  * scheme
         *  * userinfo
         *  * host
         *  * port
         *  * query
         *  * fragment
         * 
         * 
         * 
         * 
         * Note that this method works as if invoking it were equivalent to evaluating
         * the following expression:
         * 
         * 
         * <pre>
         * `[fromURIReference][.fromURIReference]([ ].[parse][URIReference.parse](uriRef))`
        </pre> * 
         * 
         * @param uriRef
         * A string representing a URI reference.
         * 
         * @return
         * A [URIReferenceBuilder] instance initialized with the given
         * URI reference information.
         */
        @JvmStatic
        fun fromURIReference(uriRef: String?): URIReferenceBuilder {
            return fromURIReference(URIReference.parse(uriRef))
        }


        /**
         * Creates a [URIReferenceBuilder] instance with a given string representing
         * a URI reference. This method copies the following information to the created
         * instance:
         * 
         * 
         *  * charset
         *  * scheme
         *  * userinfo
         *  * host
         *  * port
         *  * query
         *  * fragment
         * 
         * 
         * 
         * 
         * Note that this method works as if invoking it were equivalent to evaluating
         * the following expression:
         * 
         * 
         * <pre>
         * `new URIReferenceBuilder().[uriRef][.uriRef](uriRef)`
        </pre> * 
         * 
         * @param uriRef
         * A [org.czeal.urireference.URIReference] instance.
         * 
         * @return
         * A [URIReferenceBuilder] instance initialized with the given
         * URI reference information.
         */
        fun fromURIReference(uriRef: URIReference?): URIReferenceBuilder {
            return URIReferenceBuilder().uriRef(uriRef)
        }
    }
}
