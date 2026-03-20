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
 * Normalizes for the `path` component of a URI reference, according to
 * [RFC 3986, Section 6:
 * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * @see [RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class PathNormalizer : PercentEncodedStringNormalizer() {
    /**
     * Normalizes the `path` component of a URI reference according to
     * [RFC 3986, Section 6:
     * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * @param path
     * A path value. Expected to be not `null`.
     * 
     * @param charset
     * The charset used for the path. Expected to be not `null`.
     * 
     * @param hasAuthority
     * Whether the URI reference containing the path has an authority or not.
     * 
     * @return
     * A string value representing the normalized path.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(path: String?, charset: Charset, hasAuthority: Boolean): String? {
        if (path?.isEmpty() == true) {
            // The path is empty.

            // If the URI has an authority, normalize the path to "/"
            // to comply with the following requirement.
            //
            //   RFC 3986, 6.2.3. Scheme-Based Normalization
            //
            //     In general, a URI that uses the generic syntax for
            //     authority with an empty path should be normalized to
            //     a path of "/".

            if (hasAuthority) {
                return "/"
            }

            // Return the original path value.
            return path
        }

        // 1. Resolve the path based on the following requirement.
        //
        //   RFC 3986, 6.2.2.3. Path Segment Normalization
        //
        //     The complete path segments "." and ".." are intended only
        //     for use within relative references (Section 4.1) and are
        //     removed as part of the reference resolution process (Section
        //     5.2). However, some deployed implementations incorrectly
        //     assume that reference resolution is not necessary when the
        //     reference is already a URI and thus fail to remove dot-segments
        //     when they occur in non-relative paths. URI normalizers
        //     should remove dot-segments by applying the remove_dot_segments
        //     algorithm to the path, as described in Section 5.2.4.
        val dotRemoved = Utils.removeDotSegments(path!!)

        // 2. Normalize the resolved path.
        return process(
            dotRemoved,
            charset,
            StringBuilder()
        )
    }


    override fun toLowerCase(): Boolean {
        return false
    }
}
