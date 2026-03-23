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
 * Normalizes the `fragment` component of a URI reference, according to
 * [RFC 3986, Section 6:
 * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * @see [ RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class FragmentNormalizer : PercentEncodedStringNormalizer() {
    /**
     * Normalizes the `fragment` of a URI reference, according to
     * [RFC 3986, Section
     * 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * @param fragment
     * A `fragment` value to normalize.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the `fragment` parameter.
     * 
     * @return
     * The new string representing the normalized `fragment` component.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(fragment: String?, charset: Charset): String? {
        if (fragment == null || fragment.isEmpty()) {
            return fragment
        }

        return process(
            fragment,
            charset,
            StringBuilder()
        )
    }


    override fun toLowerCase(): Boolean {
        return false
    }
}
