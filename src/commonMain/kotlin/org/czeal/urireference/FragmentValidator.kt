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
 * Validates the `fragment` component of a URI reference.
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class FragmentValidator : PercentEncodedStringValidator("fragment") {
    /**
     * Validates a given string as the `fragment` component of a URI reference.
     * 
     * @param fragment
     * An input string to parse as the `fragment` component of a URI
     * reference.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the `fragment` parameter.
     * 
     * @throws IllegalArgumentException
     * If the value of the `fragment` parameter is invalid.
     */
    fun validate(fragment: String?, charset: Charset) {
        if (fragment == null || fragment.isEmpty()) {
            return
        }

        process(fragment, charset, null)
    }


    override fun isValidOnNonPercent(c: Char): Boolean {
        return Utils.isUnreserved(c) ||
                Utils.isSubdelim(c) || c == ':' || c == '@' || c == '/' || c == '?'
    }
}
