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
 * Validates the `userinfo` of a URI reference according to the following
 * syntax defined in [
 * "RFC 3986, Appendix A. Collected ABNF for URI"](https://www.rfc-editor.org/rfc/rfc3986#appendix-A).
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`userinfo = *( unreserved / pct-encoded / sub-delims / ":" ) `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class UserinfoValidator : PercentEncodedStringValidator("userinfo") {
    /**
     * Validates a value as a `userinfo`.
     * 
     * @param userinfo
     * A `userinfo` value.
     * 
     * @param charset
     * The charset used for the `userinfo` value.
     * 
     * @throws IllegalArgumentException
     * If the `userinfo` value is invalid.
     */
    fun validate(userinfo: String?, charset: Charset?) {
        if (userinfo == null || userinfo.isEmpty()) {
            return
        }

        process(userinfo, charset, null)
    }


    override fun isValidOnNonPercent(c: Char): Boolean {
        return Utils.isUnreserved(c) || Utils.isSubdelim(c) || c == ':'
    }
}
