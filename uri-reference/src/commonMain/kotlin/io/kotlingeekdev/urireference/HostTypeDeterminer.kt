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


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * Determines the host type. Possible host type values are [REGNAME][HostType.REGNAME],
 * [IPV4][HostType.IPV4], [IPV6][HostType.IPV6] and [IPVFUTURE][HostType.IPVFUTURE].
 * 
 * @see [ RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class HostTypeDeterminer {
    /**
     * Determines the host type. Possible host type values are [REGNAME][HostType.REGNAME],
     * [IPV4][HostType.IPV4], [IPV6][HostType.IPV6] and [IPVFUTURE][HostType.IPVFUTURE].
     * 
     * @param value
     * A `host` value.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the `host` parameter.
     * 
     * @return
     * The type of the host value.
     */
    fun determine(value: String?, charset: Charset): HostType {
        // If the host is null or empty.
        if (value == null || value.isEmpty()) {
            // The host type is determined as a reg-name.
            return HostType.REGNAME
        }

        // If the host value starts with '[', indicating the host value being an
        // IP-literal.
        if (value.startsWith("[")) {
            return determineHostTypeForIpLiteral(value)
        }

        try {
            // Validate the host value as an IPv4 address.
            Ipv4AddressValidator().validate(value)

            // The host type is determined as an IPv4 address.
            return HostType.IPV4
        } catch (t: Throwable) {
            // If the check above fails, validate the host value as a reg-name.
            RegNameValidator().validate(value, charset)

            // The host type is determined as a reg-name.
            return HostType.REGNAME
        }
    }


    private fun determineHostTypeForIpLiteral(value: String): HostType {
        // Ensure the host value ends with ']'.
        if (!value.endsWith("]")) {
            throw Utils.newIAE(
                "The host value \"%s\" start with \"[\" but doesn't end with \"]\".",
                value
            )
        }

        // Extract the content enclosed by brackets.
        val enclosed = value.substring(1, value.length - 1)

        try {
            // Validate the host value as an IPv6 address.
            Ipv6AddressValidator().validate(enclosed)

            // The host type is determined as an IPv6 address.
            return HostType.IPV6
        } catch (t: Throwable) {
            // If the check above fails, validate the host value as an IPvFuture
            // address.
            IpvFutureValidator().validate(enclosed)

            // The host type is determined as an IPvFuture address.
            return HostType.IPVFUTURE
        }
    }
}
