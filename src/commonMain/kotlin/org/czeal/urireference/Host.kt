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
 * A class representing the `host` component of a URI reference.
 * 
 * 
 * 
 * This class provides the [.parse] method, which parses a given
 * string as the `host` component of a URI reference, according to
 * [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing succeeds,
 * it creates a [Host] object, classifying the parsed `host` into one
 * of the specific types: "Registered Name", "IPv4 Address", "IPv6 Address" or
 * "IPvFuture Address". If parsing fails due to invalid input string, an `IllegalArgumentException` will be thrown. The [.getType] method can be used
 * to retrieve the type of the host.
 * 
 * 
 * 
 * 
 * This class is immutable.
 * 
 * 
 * @see [RFC 3986 Uniform Resource
 * Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
class Host
/**
 * A package-private constructor..
 * 
 * @param type
 * The type of the host.
 * 
 * @param value
 * The value of the host.
 */ internal constructor(
    /**
     * The type of this [Host] object.
     */
    val type: HostType,
    /**
     * The value of this [Host] object.
     */
    val value: String?
) : Comparable<Host?> {
    /**
     * Get the type of this [Host] object.
     * 
     * @return
     * The type of this [Host] object.
     */


    /**
     * Get the value of this [Host] object.
     * 
     * @return
     * The value of this [Host] object.
     */


    /**
     * Returns a string representation of this [Host] object. The string
     * representation is the value of this [Host] object.
     * 
     * @return
     * The string representation of this [Host] object.
     */
    override fun toString(): String {
        return value!!
    }


    /**
     * Compares this [Host] object with the specified object for equality.
     * The comparison is based on the type and value of this [Host] object.
     * 
     * @param obj
     * The object to be compared for equality with this [Host] object.
     * 
     * @return
     * `true` if the specified object is equal to this [Host]
     * object.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || this::class != other::class) {
            return false
        }

        val otherRef = other as Host

        // Compare all components for equality.
        return this.type == otherRef.type &&
                this.value == otherRef.value
    }


    /**
     * Returns a hash code value for this [Host] object. The hash code is
     * generated based on the type and value of this [Host] object.
     * 
     * @return
     * The hash code value for this [Host] object.
     */
    override fun hashCode(): Int {
        return ObjectUtils.hash(type, value)
    }


    /**
     * Compares this [Host] object with another [Host] object for order.
     * The comparison is based on the hash code of the [Host] objects.
     * 
     * @param other
     * The [Host] object to be compared.
     * 
     * @return
     * 0 if this [Host] object is equal to the specified [Host]
     * object. A negative value if this [Host] object is less than
     * the specified object. A positive value if this [Host] object
     * is greater than the specified object.
     * 
     * @throws NullPointerException
     * If the specified [Host] object is `null`.
     */
    override fun compareTo(other: Host?): Int {
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
         * Parses a string as an `host` component of a URI reference according
         * to [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing
         * succeeds, this method creates a [Host] instance. If parsing fails
         * due to invalid input string, it throws an `IllegalArgumentException`.
         * 
         * 
         * Examples:
         * <pre>`//--------------------------------------------------------------------------- // Example 1. Parse a string with a registered name as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("example.com"); System.out.println(parsed.getType());// "REGNAME" System.out.println(parsed.getValue()); // "example.com" //--------------------------------------------------------------------------- // Example 2. Parse a string with an IPV4 address as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("101.102.103.104"); System.out.println(parsed.getType());// "IPV4" System.out.println(parsed.getValue()); // "101.102.103.104" //--------------------------------------------------------------------------- // Example 3. Parse a string with an IPV6 address as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"); System.out.println(parsed.getType());// "IPV6" System.out.println(parsed.getValue()); // "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]" //--------------------------------------------------------------------------- // Example 4. Parse a string with an IPvFuture address as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("[v1.fe80::a+en1]"); System.out.println(parsed.getType());// "IPVFUTURE" System.out.println(parsed.getValue()); // "[v1.fe80::a+en1]" `</pre>
         * 
         * @param host
         * An input string to parse as the `host` component of a URI reference.
         * 
         * @param charset
         * The charset used for percent-encoding some characters (e.g. reserved
         * characters) contained in the `host` parameter.
         * 
         * @return
         * The `Host` object representing the parsed `host` component.
         * 
         * @throws NullPointerException
         * If the value of the `host` parameter or the `charset`
         * parameter is `null`.
         * 
         * @throws IllegalArgumentException
         * If the value of the `host` parameter is invalid as the `host` component of a URI reference.
         * 
         * @see [RFC 3986 Uniform
         * Resource Identifier
        ](https://www.rfc-editor.org/rfc/rfc3986) */
        /**
         * Parses a string as an `host` component of a URI reference, according
         * to [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing
         * succeeds, this method creates a [Host] object. If parsing fails due
         * to invalid input, it throws an `IllegalArgumentException`. Note that
         * this method works as if invoking it were equivalent to evaluating the expression
         * `[parse][.parse](input, [StandardCharsets].[ ][StandardCharsets.UTF_8])`.
         * 
         * 
         * Examples:
         * <pre>`//--------------------------------------------------------------------------- // Example 1. Parse a string with a registered name as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("example.com"); System.out.println(parsed.getType());// "REGNAME" System.out.println(parsed.getValue()); // "example.com" //--------------------------------------------------------------------------- // Example 2. Parse a string with an IPV4 address as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("101.102.103.104"); System.out.println(parsed.getType());// "IPV4" System.out.println(parsed.getValue()); // "101.102.103.104" //--------------------------------------------------------------------------- // Example 3. Parse a string with an IPV6 address as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"); System.out.println(parsed.getType());// "IPV6" System.out.println(parsed.getValue()); // "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]" //--------------------------------------------------------------------------- // Example 4. Parse a string with an IPvFuture address as a host. //--------------------------------------------------------------------------- Host parsed = Host.parse("[v1.fe80::a+en1]"); System.out.println(parsed.getType());// "IPVFUTURE" System.out.println(parsed.getValue()); // "[v1.fe80::a+en1]" `</pre>
         * 
         * @param host
         * The input string to parse as the `host` component of a URI
         * reference.
         * 
         * @return
         * The `Host` object representing the parsed `host` component.
         * 
         * @throws NullPointerException
         * If the value of the `host` parameter or the `charset`
         * parameter is `null`.
         * 
         * @throws IllegalArgumentException
         * If the value of the `host` parameter is invalid as the `host` component of a URI reference.
         * 
         * @see [RFC 3986 Uniform
         * Resource Identifier
        ](https://www.rfc-editor.org/rfc/rfc3986) */
        @JvmOverloads
        @JvmStatic
        fun parse(host: String?, charset: Charset? = Charsets.UTF8): Host {
            return HostParser().parse(host, charset)
        }
    }
}
