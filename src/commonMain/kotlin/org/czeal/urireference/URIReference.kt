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
 * A class representing a URI reference, as defined in [
 * RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax](https://www.rfc-editor.org/rfc/rfc3986). This class
 * provides various methods for working with URI references, including parsing,
 * resolving, and normalizing them.
 * 
 * 
 * 
 * The [.parse] method parses a given string as a URI reference according
 * to RFC 3986 and creates a [URIReference] instance, representing either
 * a URI or a relative reference. The [.isRelativeReference] can be used
 * to check if the parsed URI instance represents a relative reference (returns
 * `true`) or a URI (returns `false`). Access URI components such as
 * `scheme` using corresponding getter methods like [.getScheme].
 * 
 * 
 * 
 * 
 * The [.resolve] method and [URIReference.resolve]
 * method return a new `URIReference` instance representing a URI reference
 * obtained by resolving the current URI reference against a given base URI, following
 * the rules in RFC 3986, 5 Reference Resolution.
 * 
 * 
 * 
 * 
 * The [.normalize] method returns a new `URIReference` instance representing
 * a URI reference obtained by normalizing the current URI reference, in accordance
 * with RFC 3986, 6 Normalization and Comparison. Note that a URI reference must
 * be resolved before it can be normalized.
 * 
 * 
 * 
 * 
 * This class is immutable.
 * 
 * 
 * 
 * Examples:
 * <pre>`//--------------------------------------------------------------------------- // Parsing. //--------------------------------------------------------------------------- URIReference uriRef = URIReference                           .parse("http://example.com/a"); // Parse a URI. System.out.println(uriRef.isRelativeReference());// false System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.hasAuthority());// true System.out.println(uriRef.getAuthority().toString()); // "example.com" System.out.println(uriRef.getUserinfo());// null System.out.println(uriRef.getHost().getType());// "REGNAME" System.out.println(uriRef.getHost().getValue());// "example.com" System.out.println(uriRef.getPort());// -1 System.out.println(uriRef.getPath());// "/a" System.out.println(uriRef.getQuery());// null System.out.println(uriRef.getFragment());// null //--------------------------------------------------------------------------- // Resolution. //--------------------------------------------------------------------------- URIReference uriRef = URIReference                           .parse("http://example.com") // Parse a base URI.                           .resolve("/a/b");// Resolve a relative reference against the base URI. System.out.println(uriRef.isRelativeReference());// false System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.hasAuthority());// true System.out.println(uriRef.getAuthority().toString()); // "example.com" System.out.println(uriRef.getUserinfo());// null System.out.println(uriRef.getHost().getType());// "REGNAME" System.out.println(uriRef.getHost().getValue());// "example.com" System.out.println(uriRef.getPort());// -1 System.out.println(uriRef.getPath());// "/a/b" System.out.println(uriRef.getQuery());// null System.out.println(uriRef.getFragment());// null //--------------------------------------------------------------------------- // Normalization. //--------------------------------------------------------------------------- URIReference uriRef = URIReference                           .parse("hTTp://example.com:80/a/b/c/../d/") // Parse a URI.                           .normalize();// Normalize it. System.out.println(uriRef.isRelativeReference());// false System.out.println(uriRef.getScheme());// "http" System.out.println(uriRef.hasAuthority());// true System.out.println(uriRef.getAuthority().toString()); // "example.com:80" System.out.println(uriRef.getUserinfo());// null System.out.println(uriRef.getHost().getType());// "REGNAME" System.out.println(uriRef.getHost().getValue());// "example.com" System.out.println(uriRef.getPort());// -1 System.out.println(uriRef.getPath());// "/a/b/d/" System.out.println(uriRef.getQuery());// null System.out.println(uriRef.getFragment());// null `</pre>
 * 
 * @see [RFC 3986 - Uniform
 * Resource Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
class URIReference private constructor(res: ProcessResult) : Comparable<URIReference?> {
    /**
     * Internal class that holds intermediate values of the URI components during
     * some process This class is intentionally package-private.
     */
    internal open class ProcessResult {
        var charset: Charset? = null
        var relativeReference: Boolean = false
        var scheme: String? = null
        var authority: Authority? = null
        var path: String? = null
        var query: String? = null
        var fragment: String? = null


        /**
         * Converts this object to a [URIReference] instance.
         * 
         * @return
         * A [URIReference] instance built from `this` object.
         */
        fun toURIReference(): URIReference {
            return URIReference(this)
        }
    }


    /**
     * Returns the charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in this URI reference.
     * 
     * @return
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in this URI reference.
     */
    /**
     * The charset used for percent-encoding some characters (e.g. reserved characters)
     * contained in the URI reference.
     */
    val charset: Charset?


    /**
     * Returns `true` if the URI reference is a relative reference. See
     * [RFC 3986,
     * 4.2. Relative Reference](https://www.rfc-editor.org/rfc/rfc3986#section-4.2) for more details.
     * 
     * @return
     * `true` if the URI reference is a relative reference. otherwise,
     * `false`.
     * 
     * @see [RFC 3986,
     * 4.2. Relative Reference](https://www.rfc-editor.org/rfc/rfc3986.section-4.2)
     */
    /**
     * Whether the URI reference is a relative reference or not.
     */
    val isRelativeReference: Boolean


    /**
     * Get the scheme of this URI reference. If this URI reference is
     * a relative reference, `null` is returned.
     * 
     * @return
     * The scheme of this URI reference.
     */
    /**
     * The value of the scheme.
     */
    val scheme: String?


    /**
     * Get the authority of this URI reference.
     * 
     * @return
     * The authority of this URI reference.
     */
    /**
     * The value of the authority.
     */
    val authority: Authority?


    /**
     * Get the path of this URI reference.
     * 
     * @return
     * The path of this URI reference.
     */
    /**
     * The value of the path.
     */
    val path: String?


    /**
     * Get the query of this URI reference.
     * 
     * @return
     * The query of this URI reference.
     */
    /**
     * The value of the query.
     */
    val query: String?


    /**
     * Get the fragment of this URI reference.
     * 
     * @return
     * The fragment of this URI reference.
     */
    /**
     * The value of the fragment.
     */
    val fragment: String?


    /**
     * A private constructor. This is expected to be used by [ProcessResult]
     * class.
     * 
     * @param res
     * The result obtained after some processing.
     */
    init {
        this.charset = res.charset
        this.isRelativeReference = res.relativeReference
        this.scheme = res.scheme
        this.authority = res.authority
        this.path = res.path.toString()
        this.query = res.query
        this.fragment = res.fragment
    }


    val userinfo: String?
        /**
         * Get the userinfo of this URI reference.
         * 
         * @return
         * The userinfo of this URI reference.
         */
        get() = if (hasAuthority()) this.authority!!.userinfo else null


    val host: Host?
        /**
         * Get the host of this URI reference.
         * 
         * @return
         * The host of this URI reference.
         */
        get() = if (hasAuthority()) this.authority!!.host else null


    val port: Int
        /**
         * Get the port of this URI reference.
         * 
         * @return
         * The port of this URI reference.
         */
        get() = if (hasAuthority()) this.authority!!.port else -1


    /**
     * Checks whether or not this URI reference has an authority.
     * 
     * @return
     * `true` if this URI reference has an authority;
     * otherwise, `false`.
     */
    fun hasAuthority(): Boolean {
        return this.authority != null
    }


    /**
     * Returns a string representation of this [URIReference] object.
     * 
     * 
     * 
     * The string is constructed by concatenating the `scheme`, `authority`,
     * `path`, `query`, and `fragment` components, separated by
     * appropriate delimiters.
     * 
     * 
     * @return
     * A string representation of this [URIReference] object.
     */
    override fun toString(): String {
        val sb = StringBuilder()

        // Append a scheme if present.
        if (this.scheme != null) {
            sb.append(this.scheme).append(":")
        }

        // Append an authority if present.
        if (this.authority != null) {
            sb.append("//").append(this.authority.toString())
        }

        // Append a path if present.
        if (this.path != null) {
            sb.append(this.path)
        }

        // Append a path if present.
        if (this.query != null) {
            sb.append("?").append(this.query)
        }

        // Append a fragment if present.
        if (this.fragment != null) {
            sb.append("#").append(this.fragment)
        }

        return sb.toString()
    }


    /**
     * Compares this [URIReference] object with the specified object for equality.
     * 
     * 
     * 
     * The comparison is based on the values of `scheme`, `authority`,
     * `path`, `query`, and `fragment` components.
     * 
     * 
     * @param other
     * The object to be compared for equality with this [URIReference].
     * 
     * @return
     * `true` if the specified object is equal to this [URIReference].
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || this::class != other::class) {
            return false
        }

        val otherRef = other as URIReference

        // Compare all components for equality.
        return this.scheme == otherRef.scheme &&
                this.authority == otherRef.authority &&
                this.path == otherRef.path &&
                this.query == otherRef.query &&
                this.fragment == otherRef.fragment
    }


    /**
     * Returns a hash code value for this [URIReference] object.
     * 
     * 
     * 
     * The hash code is generated based on the values of `scheme`, `authority`, `path`, `query`, and `fragment` components.
     * 
     * 
     * @return A hash code value for this object.
     */
    override fun hashCode(): Int {
        return ObjectUtils.hash(
            this.scheme, this.authority, this.path, this.query, this.fragment
        )
    }


    /**
     * Compares this [URIReference] object with another [URIReference]
     * object for order. The comparison is based on the string representation of
     * the [URIReference] objects.
     * 
     * @param other
     * The [URIReference] object to be compared.
     * 
     * @return
     * 0 if this [URIReference] object is equal to the specified
     * [URIReference] object. A negative value if this [URIReference]
     * object is less than the specified object. A positive value if this
     * [URIReference] object is greater than the specified object.
     * 
     * @throws NullPointerException
     * If the specified [URIReference] object is `null`.
     */
    override fun compareTo(other: URIReference?): Int {
        if (other == null) {
            throw Utils.newNPE("A null value is not comparable.")
        }

        if (this == other) {
            return 0
        }

        return this.toString().compareTo(other.toString())
    }


    /**
     * Resolves the given URI reference against this URI reference.
     * 
     * This method works as if invoking it were equivalent to evaluating the expression
     * `[resolve][.resolve]([ ][.parse](uriRef, [.getCharset]))`.
     * 
     * 
     * Examples:
     * <pre>`// A base URI. URIReference baseUri = URIReference.parse("http://example.com"); // A relative reference. String relRef = "/path1/path2"; // Resolve the relative reference against the base URI. URIReference resolved = baseUri.resolve(relRef); // This will output "http://example.com/path1/path2". System.out.println(resolved.toString()); `</pre>
     * 
     * @param uriRef
     * A string representing a URI reference to be resolved against this
     * URI reference.
     * 
     * @return The URI reference obtained by resolving the input string against
     * this URI reference.
     * 
     * @throws NullPointerException
     * If `uriRef` is `null`.
     * 
     * @throws IllegalStateException
     * If this URI reference is not an absolute URI.
     */
    fun resolve(uriRef: String?): URIReference? {
        return resolve(parse(uriRef, this.charset!!))
    }


    /**
     * Resolve the given URI reference against this URI reference.
     * 
     * 
     * Examples:
     * <pre>`// A base URI. URIReference baseUri = URIReference.parse("http://example.com"); // A relative reference. URIReference relRef = URIReference.parse("/path1/path2"); // Resolve the relative reference against the base URI. URIReference resolved = baseUri.resolve(relRef); // This will output "http://example.com/path1/path2". System.out.println(resolved.toString()); `</pre>
     * 
     * @param uriRef
     * A URI reference to be resolved against this URI reference.
     * 
     * @return The URI reference obtained by resolving the input string against
     * this URI reference.
     * 
     * @throws NullPointerException
     * If `str` is `null`.
     * 
     * @throws IllegalStateException
     * If this URI reference is not an absolute URI.
     */
    fun resolve(uriRef: URIReference): URIReference {
        return URIReferenceResolver().resolve(uriRef, this)
    }


    /**
     * Normalizes this URI reference.
     * 
     * 
     * 
     * This method does not modify the state of the original [URIReference]
     * instance on which this method is called. Instead, it creates a new [ ] instance and initializes it with the information about the
     * normalized URI reference.
     * 
     * 
     * 
     * 
     * Note that this method throws an `IllegalStateException` if this URI
     * reference has not been resolved yet.
     * 
     * 
     * @return
     * A new `URIReference` instance representing the normalized
     * URI reference.
     * 
     * @throws IllegalStateException
     * If this URI reference has not been resolved yet.
     */
    fun normalize(): URIReference? {
        return URIReferenceNormalizer().normalize(this)
    }

    companion object {
        /**
         * Serial Version UID.
         */
        private const val serialVersionUID = 1L


        /**
         * Parses a string based on in [
         * RFC 3986](https://www.rfc-editor.org/rfc/rfc3986) and creates a `URIReference` instance if parsing succeeds.
         * If parsing fails due to invalid input string, an `IllegalArgumentException`
         * will be thrown.
         * 
         * 
         * 
         * Note that this method works as if invoking it were equivalent to evaluating
         * the expression `[parse][.parse](String uriRef,
         * [StandardCharsets].[UTF_8][StandardCharsets.UTF_8])`.
         * 
         * 
         * 
         * Examples:
         * <pre>`// Example 1. Parse a string as a URI. URIReference.parse("http://example.com"); // Example 2. Parse a string as a relative reference. URIReference.parse("//example.com/path1"); // Example 3. Parse a string with an IPV4 host as a URI. URIReference.parse("http://101.102.103.104"); // Example 4. Parse a string with an IPV6 host as a URI. URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"); // Example 5. Parse a string with percent-encoded values as a URI. URIReference.parse("http://%6A%6F%68%6E@example.com"); `</pre>
         * 
         * @param uriRef
         * A input string to parse as a URI reference.
         * 
         * @return
         * The `URIReference` instance obtained by parsing the input string.
         * 
         * @throws NullPointerException
         * If `uriRef` or `charset` is `null`.
         * 
         * @throws IllegalArgumentException
         * If `uriRef` is invalid as a URI reference.
         * 
         * @see [RFC 3986 Uniform
         * Resource Identifier
        ](https://www.rfc-editor.org/rfc/rfc3986) */
        @JvmStatic
        fun parse(uriRef: String?): URIReference {
            return parse(uriRef, Charsets.UTF8)
        }


        /**
         * 
         * 
         * Parses a string based on in [
         * RFC 3986](https://www.rfc-editor.org/rfc/rfc3986) and creates a `URIReference` instance if parsing succeeds.
         * If parsing fails due to invalid input string, an `IllegalArgumentException`
         * will be thrown.
         * 
         * 
         * 
         * Examples:
         * <pre>`// Example 1. Create a URI using UTF-8 encoding. URIReference.parse("http://example.com", StandardCharsets.UTF_8); // Example 2. Create a relative reference using UTF-8 encoding. URIReference.parse("//example.com/path1", StandardCharsets.UTF_8); // Example 3. Create a URI with IPV4 host using UTF-8 encoding. URIReference.parse("http://101.102.103.104", StandardCharsets.UTF_8); // Example 4. Create a URI with IPV6 host using UTF-8 encoding. URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", StandardCharsets.UTF_8); // Example 5. Create a URI with percent-encoded values using UTF-8 encoding. URIReference.parse("http://%6A%6F%68%6E@example.com", StandardCharsets.UTF_8); `</pre>
         * 
         * @param uriRef
         * The input string to be parsed as a `URIReference`
         * instance.
         * 
         * @param charset
         * The charset used for percent-encoding some characters (e.g. reserved
         * characters) contained in the input string.
         * 
         * @return
         * The `URIReference` instance obtained by parsing the input string.
         * 
         * @throws NullPointerException
         * If `uriRef` or `charset` is `null`.
         * 
         * @throws IllegalArgumentException
         * If `uriRef` is invalid as a URI reference.
         * 
         * @see [RFC 3986 Uniform
         * Resource Identifier
        ](https://www.rfc-editor.org/rfc/rfc3986) */
        @JvmStatic
        fun parse(uriRef: String?, charset: Charset): URIReference {
            return URIReferenceParser().parse(uriRef, charset)
        }
    }
}
