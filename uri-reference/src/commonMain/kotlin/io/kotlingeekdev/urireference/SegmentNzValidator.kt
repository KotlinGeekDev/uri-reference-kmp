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
 * Validates a value as a `segment-nz` (non-zero-length path segment),
 * according to the following syntax defined in "RFC 3986, Appendix A. Collected
 * ABNF for URI".
 * 
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`segment-nz = 1*pchar `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986, Appendix A.
 * Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal open class SegmentNzValidator : SegmentValidator() {
    /**
     * Validates a value as a `segment-nz` (non-zero-length path segment).
     * 
     * @param segment
     * A `segment-nz` value.
     * 
     * @param charset
     * The charset used for the `segment-nz` value.
     * 
     * @throws IllegalArgumentException
     * If the `segment` value is invalid.
     */
    override fun validate(segment: String?, charset: Charset) {
        if (segment == null) {
            throw Utils.newNPE("The %s value must not be null.", name)
        }

        if (segment.isEmpty()) {
            throw Utils.newIAE("The %s value must not be empty.", name)
        }

        process(segment, charset, null)
    }
}
