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
 * 
 * 
 * Normalizes the `"port"` component of a URI reference.
 * 
 * 
 * 
 * 
 * The normalization is performed according to [
 * RFC 3986, Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * 
 * @see [ RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class PortNormalizer {
    /**
     * Normalize a `"port"` based on [
     * RFC 3986, Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * @param port
     * A port value.
     * 
     * @param normalizedScheme
     * The normalized scheme of a URI reference containing the authority.
     * Expected to be not `null`.
     * 
     * @return
     * An integer value representing the normalized port.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(port: Int, normalizedScheme: String?): Int {
        // Normalize the port value for the scheme based on the following
        // requirement.
        //
        //   RFC 3986, 6.2.3. Scheme-Based Normalization
        //
        //     In general, a URI that uses the generic syntax for authority
        //     with an empty path should be normalized to a path of "/".
        //     Likewise, an explicit ":port", for which the port is empty
        //     or the default for the scheme, is equivalent to one where
        //     the port and its ":" delimiter are elided and thus should
        //     be removed by scheme-based normalization.

        if ((port == -1) || isDefaultPortForScheme(port, normalizedScheme)) {
            return -1
        }

        return port
    }


    private fun isDefaultPortForScheme(port: Int, scheme: String?): Boolean {
        if ("http" == scheme) {
            // Check if the port value is 80, which is the default value
            // for "http".
            return port == 80
        }

        return false
    }
}
