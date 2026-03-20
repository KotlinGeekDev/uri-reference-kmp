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
 * Normalizes the `authority` component of a URI reference, according to
 * [RFC 3986, Section 6:
 * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * 
 * 
 * Note that the normalization doesn't modify the state of the given [org.czeal.urireference.Authority]
 * object. Instead, it creates a new [org.czeal.urireference.Authority] object and initializes it
 * with the normalized `authority` information.
 * 
 * 
 * @see [RFC 3986,
 * 3.2. Authority](https://www.rfc-editor.org/rfc/rfc3986.section-3.2)
 * 
 * 
 * @see [RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class AuthorityNormalizer {
    /**
     * Normalizes the `authority` component of a URI reference, according
     * to [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6). This method does not modify
     * the state of the original [org.czeal.urireference.Authority] object on which this method is
     * called. Instead, it creates a new [org.czeal.urireference.Authority] object with the normalized
     * `authority` information.
     * 
     * @param authority
     * An [org.czeal.urireference.Authority] object to normalize.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the `authority` parameter.
     * 
     * @param normalizedScheme
     * The normalized scheme of the URI reference containing the authority.
     * 
     * @return
     * The [org.czeal.urireference.Authority] object representing the normalized `authority`
     * component.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(
        authority: Authority?, charset: Charset, normalizedScheme: String?
    ): Authority? {
        if (authority == null) {
            // The authority does not exist in the original URI reference.
            return null
        }

        // The normalization result.
        val res = Authority.ProcessResult()

        // Process the userinfo.
        processUserinfo(res, authority, charset)

        // Process the host.
        processHost(res, authority, charset)

        // Process the port.
        processPort(res, authority, normalizedScheme)

        // Build an Authority instance.
        return res.toAuthority()
    }


    private fun processUserinfo(
        res: Authority.ProcessResult, authority: Authority, charset: Charset
    ) {
        res.userinfo = UserinfoNormalizer()
            .normalize(authority.userinfo, charset)
    }


    private fun processHost(
        res: Authority.ProcessResult, authority: Authority, charset: Charset
    ) {
        res.host = HostNormalizer().normalize(authority.host!!, charset)
    }


    private fun processPort(
        res: Authority.ProcessResult, authority: Authority, normalizedScheme: String?
    ) {
        res.port = PortNormalizer().normalize(authority.port, normalizedScheme)
    }
}
