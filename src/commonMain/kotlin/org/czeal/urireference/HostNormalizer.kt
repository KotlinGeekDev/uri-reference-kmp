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
 * Normalizes the `host` component of a URI reference, according to
 * [RFC 3986, Section 6:
 * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * @see [ RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class HostNormalizer : PercentEncodedStringNormalizer() {
    /**
     * Normalizes the `host` component of a URI reference, according to
     * [RFC 3986, Section
     * 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * 
     * 
     * This method does not modify the state of the original [org.czeal.urireference.Host] object
     * on which this method is called. Instead, it creates a new [org.czeal.urireference.Host] object
     * with the normalized host information.
     * 
     * 
     * @param host
     * A [org.czeal.urireference.Host] object to normalize.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the `host` parameter.
     * 
     * @return
     * The [org.czeal.urireference.Host] object representing the normalized host.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(host: Host, charset: Charset): Host {
        // Normalize the value.
        val normalizedValue = normalizeValue(host.value, charset)

        // Normalize the type.
        val normalizedType = HostTypeDeterminer()
            .determine(normalizedValue, charset)

        // Build a Host instance.
        return Host(normalizedType, normalizedValue)
    }


    private fun normalizeValue(originalValue: String?, charset: Charset): String? {
        if (originalValue == null || originalValue.isEmpty()) {
            return originalValue
        }

        return process(
            originalValue,
            charset,
            StringBuilder()
        )
    }


    override fun toLowerCase(): Boolean {
        return true
    }
}
