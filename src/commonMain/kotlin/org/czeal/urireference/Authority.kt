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
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * A class representing the `authority` component of a URI reference.
 * 
 * 
 * 
 * This class provides the [.parse] method, which parses a given
 * string as an `authority` component, according to [
 * RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing succeeds, it creates an [Authority] object. If
 * parsing fails due to invalid input, it throws an `IllegalArgumentException`.
 * Individual components of the authority, such as `userinfo`, can be accessed
 * through corresponding getter methods, such as [.getUserinfo].
 * 
 * 
 * 
 * 
 * This class is immutable.
 * 
 * 
 * @see [RFC 3986,
 * 3.2. Authority](https://www.rfc-editor.org/rfc/rfc3986.section-3.2)
 * 
 * 
 * @author Hideki Ikeda
 */
class Authority private constructor(res: ProcessResult) : Comparable<Authority?> {
    /**
     * Internal class that holds intermediate values of the `authority` components
     * during some process. This class is intentionally package-private.
     */
    internal open class ProcessResult {
        var userinfo: String? = null
        var host: Host? = null
        var port: Int = -1


        /**
         * Converts this object to an [Authority] object.
         * 
         * @return
         * An [Authority] instance built from `this` object.
         */
        fun toAuthority(): Authority {
            return Authority(this)
        }
    }


    /**
     * Get the value of the `userinfo` component of this `Authority`
     * object.
     * 
     * @return
     * The value of the `userinfo` component of this `Authority`
     * object.
     */
    /**
     * The value of the `userinfo` component of this `Authority` object.
     */
    val userinfo: String?


    /**
     * Get the value of the `host` component of this `Authority` object.
     * 
     * @return
     * The value of the `host` component of this `Authority`
     * object.
     */
    /**
     * The value of the `host` component of this `Authority` object.
     */
    val host: Host?


    /**
     * Get the value of the `port` component of this `Authority` object.
     * 
     * @return
     * The value of the `port` component of this `Authority`
     * object.
     */
    /**
     * The value of the `port` component of this `Authority` object.
     */
    val port: Int


    /**
     * A private constructor.
     * 
     * @param res
     * The result obtained after some processing.
     */
    init {
        this.userinfo = res.userinfo
        this.host = res.host
        this.port = res.port
    }


    /**
     * Returns a string representation of this [Authority] object. The string
     * is constructed by concatenating the `userinfo`, `host`, and
     * `port` components.
     * 
     * @return
     * The string representation of this [Authority] object.
     */
    override fun toString(): String {
        val sb = StringBuilder()

        // Append the userinfo if present.
        if (userinfo != null) {
            sb.append(userinfo).append("@")
        }

        // Append the host value if present.
        if (host != null && host.value != null) {
            sb.append(host.value)
        }

        // Append the port if present.
        if (port != -1) {
            sb.append(":").append(port)
        }

        return sb.toString()
    }


    /**
     * Compares this [Authority] object with the specified object for equality.
     * The comparison is based on the values of `userinfo`, `host`,
     * and `port` components.
     * 
     * @param obj
     * The object to be compared for equality with this [Authority]
     * object.
     * 
     * @return
     * `true` if the specified object is equal to this [Authority]
     * object.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || this::class != other::class) {
            return false
        }

        val otherRef = other as Authority

        // Compare all components for equality.
        return this.userinfo == otherRef.userinfo &&
                this.host == otherRef.host && this.port == otherRef.port
    }


    /**
     * Returns the hash code value for this [Authority] object. The hash code
     * is generated based on the values of `userinfo`, `host`, and
     * `port` components.
     * 
     * @return The hash code value for this object.
     */
    override fun hashCode(): Int {
        return ObjectUtils.hash(userinfo, host, port)
    }


    /**
     * Compares this [Authority] object with another [Authority] object
     * for order. The comparison is based on the string representation of the [ ] objects.
     * 
     * @param other
     * The [Authority] object to be compared.
     * 
     * @return
     * 0 if this [Authority] object is equal to the specified [Authority]
     * object. A negative value if this [Authority] object is less
     * than the specified object. A positive value if this [Authority]
     * object is greater than the specified object.
     * 
     * @throws NullPointerException
     * If the specified [Authority] object is `null`.
     */
    override fun compareTo(other: Authority?): Int {
        if (other == null) {
            throw Utils.newNPE("A null value is not comparable.")
        }

        if (this == other) {
            return 0
        }

        return this.toString().compareTo(other.toString())
    }

    companion object {
        /**
         * Serial Version UID.
         */
        private const val serialVersionUID = 1L


        /**
         * Parses a string as the `authority` component of a URI reference according
         * to [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing
         * succeeds, this method creates an [Authority] object. If parsing fails
         * due to invalid input, it throws an `IllegalArgumentException`.
         * 
         * 
         * Examples:
         * <pre>`//--------------------------------------------------------------------------- // Example 1. Parse a string with a reg-name host and a port as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("example.com:8080", StandardCharsets.UTF_8); System.out.println(parsed.toString());// "example.com:8080" System.out.println(parsed.getUserinfo());// null System.out.println(uriRef.getAuthority().getHost().getType());// "REGNAME" System.out.println(uriRef.getAuthority().getHost().getValue()); // "example.com" System.out.println(parsed.getPort());// 8080 //--------------------------------------------------------------------------- // Example 2. Parse a string with an IPV4 host as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("101.102.103.104", StandardCharsets.UTF_8); System.out.println(parsed.toString());// "101.102.103.104" System.out.println(parsed.getUserinfo());// null System.out.println(parsed.getHost().getType());// "IPV4" System.out.println(parsed.getHost().getValue()); // "101.102.103.104" System.out.println(parsed.getPort());// -1 //--------------------------------------------------------------------------- // Example 3. Parse a string with an IPV6 host as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", StandardCharsets.UTF_8); System.out.println(parsed.toString());// "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]" System.out.println(parsed.getUserinfo());// null System.out.println(parsed.getHost().getType());// "IPV6" System.out.println(parsed.getHost().getValue()); // "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]" System.out.println(parsed.getPort());// -1 //--------------------------------------------------------------------------- // Example 4. Parse a string with percent-encoded values as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("%6A%6F%68%6E@example.com", StandardCharsets.UTF_8); System.out.println(parsed.toString());// "%6A%6F%68%6E@example.com" System.out.println(parsed.getUserinfo());// "%6A%6F%68%6E" System.out.println(parsed.getHost().getType());// "REGNAME" System.out.println(parsed.getHost().getValue()); // "example.com" System.out.println(parsed.getPort());// -1 `</pre>
         * 
         * @param authority
         * An input string to parse as the `authority` component of a
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
         * @see [RFC 3986 Uniform
         * Resource Identifier
        ](https://www.rfc-editor.org/rfc/rfc3986) */
        /**
         * Parses a string as the `authority` component of a URI reference according
         * to [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing
         * succeeds, this method creates an [Authority] object. If parsing fails
         * due to invalid input, it throws an `IllegalArgumentException`.
         * 
         * 
         * 
         * Note that this method works as if invoking it were equivalent to evaluating
         * the expression `[parse][.parse](authority, [ ].[UTF_8][StandardCharsets.UTF_8])`.
         * 
         * 
         * 
         * Examples:
         * <pre>`//--------------------------------------------------------------------------- // Example 1. Parse a string with a reg-name host and a port as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("example.com:8080"); System.out.println(parsed.toString());// "example.com:8080" System.out.println(parsed.getUserinfo());// null System.out.println(uriRef.getAuthority().getHost().getType());// "REGNAME" System.out.println(uriRef.getAuthority().getHost().getValue()); // "example.com" System.out.println(parsed.getPort());// 8080 //--------------------------------------------------------------------------- // Example 2. Parse a string with an IPV4 host as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("101.102.103.104"); System.out.println(parsed.toString());// "101.102.103.104" System.out.println(parsed.getUserinfo());// null System.out.println(parsed.getHost().getType());// "IPV4" System.out.println(parsed.getHost().getValue()); // "101.102.103.104" System.out.println(parsed.getPort());// -1 //--------------------------------------------------------------------------- // Example 3. Parse a string with an IPV6 host as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"); System.out.println(parsed.toString());// "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]" System.out.println(parsed.getUserinfo());// null System.out.println(parsed.getHost().getType());// "IPV6" System.out.println(parsed.getHost().getValue()); // "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]" System.out.println(parsed.getPort());// -1 //--------------------------------------------------------------------------- // Example 4. Parse a string with percent-encoded values as an authority. //--------------------------------------------------------------------------- Authority parsed = Authority.parse("%6A%6F%68%6E@example.com"); System.out.println(parsed.toString());// "%6A%6F%68%6E@example.com" System.out.println(parsed.getUserinfo());// "%6A%6F%68%6E" System.out.println(parsed.getHost().getType());// "REGNAME" System.out.println(parsed.getHost().getValue()); // "example.com" System.out.println(parsed.getPort());// -1 `</pre>
         * 
         * @param authority
         * An input string to parse as the `authority` component of a
         * URI reference.
         * 
         * @return
         * The `Authority` object representing the parsed `authority`
         * component.
         * 
         * @throws NullPointerException
         * If the value of the `authority` parameter is `null`.
         * 
         * @throws IllegalArgumentException
         * If the value of the `authority` parameter is invalid as the
         * `authority` of a URI reference.
         * 
         * @see [RFC 3986 Uniform
         * Resource Identifier
        ](https://www.rfc-editor.org/rfc/rfc3986) */
        @JvmOverloads
        @JvmStatic
        fun parse(authority: String?, charset: Charset? = Charsets.UTF8): Authority? {
            return AuthorityParser().parse(authority, charset)
        }
    }
}
