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
 * 
 * 
 * Validates the `host` component of a URI reference as a "reg-name" (registered
 * name), according to the syntax defined in [
 * RFC 3986, Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A) as follows.
 * 
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`reg-name = *( unreserved / pct-encoded / sub-delims ) `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class RegNameValidator : PercentEncodedStringValidator("host") {
    /**
     * Validates a value as a reg-name (registered name) of a host.
     * 
     * @param regName
     * A reg-name (registered name) value of a host.
     * 
     * @param charset
     * The charset used for the reg-name value.
     * 
     * @throws IllegalArgumentException
     * If the reg-name value is invalid.
     */
    fun validate(regName: String?, charset: Charset) {
        if (regName == null || regName.isEmpty()) {
            return
        }

        process(regName, charset, null)
    }


    override fun isValidOnNonPercent(c: Char): Boolean {
        return Utils.isUnreserved(c) || Utils.isSubdelim(c)
    }
}
