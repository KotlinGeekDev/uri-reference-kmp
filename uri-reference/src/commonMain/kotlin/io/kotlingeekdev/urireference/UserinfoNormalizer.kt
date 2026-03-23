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
 * Normalizes the `userinfo` component of a URI reference according to
 * [RFC 3986, Section 6:
 * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * 
 * 
 * Examples:
 * <pre>`// Parse a URI and normalize it. URIReference normalized = URIReference                               .parse("hTTp://example.com:80/a/b/c/../d/")                               .normalize(); System.out.println(normalized.isRelativeReference());// false System.out.println(normalized.getScheme());// "http" System.out.println(normalized.hasAuthority());// true System.out.println(normalized.getAuthority().toString()); // "example.com:80" System.out.println(normalized.getUserinfo());// null System.out.println(normalized.getHost().getType());// "REGNAME" System.out.println(normalized.getHost().getValue());// "example.com" System.out.println(normalized.getPort());// -1 System.out.println(normalized.getPath());// "/a/b/d/" System.out.println(normalized.getQuery());// null System.out.println(normalized.getFragment());// null `</pre>
 * 
 * @see [RFC 3986, Section 6:
 * Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class UserinfoNormalizer : PercentEncodedStringNormalizer() {
    /**
     * Normalizes a `userinfo` according to [
     * RFC 3986, Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * @param userinfo
     * A `userinfo` to normalize.
     * 
     * @param charset
     * The charset used for the path. Expected to be not `null`.
     * 
     * @return
     * A string value representing the normalized `userinfo`.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(userinfo: String?, charset: Charset): String? {
        if (userinfo == null || userinfo.isEmpty()) {
            return userinfo
        }

        return process(
            userinfo,
            charset,
            StringBuilder()
        )
    }


    override fun toLowerCase(): Boolean {
        return false
    }
}
