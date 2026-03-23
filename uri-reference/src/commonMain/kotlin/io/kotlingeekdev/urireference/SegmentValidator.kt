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
 * 
 * 
 * Validates a value as a `segment`, according to "RFC 3986, Appendix A.
 * Collected ABNF for URI".
 * 
 * 
 * <blockquote>
 * <pre>`segment = *pchar `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986, Appendix A.
 * Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal open class SegmentValidator : PercentEncodedStringValidator("path segment") {
    /**
     * Validates a value as a `segment`.
     * 
     * @param segment
     * A `segment`.
     * 
     * @param charset
     * The charset used for the `segment`.
     * 
     * @throws IllegalArgumentException
     * If the `segment` value is invalid.
     */
    open fun validate(segment: String?, charset: Charset) {
        if (segment == null || segment.isEmpty()) {
            return
        }

        process(segment, charset, null)
    }


    override fun isValidOnNonPercent(c: Char): Boolean {
        return Utils.isUnreserved(c) || Utils.isSubdelim(c) || c == ':' || c == '@'
    }
}
