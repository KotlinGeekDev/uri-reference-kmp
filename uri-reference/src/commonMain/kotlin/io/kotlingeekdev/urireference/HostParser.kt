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
import com.fleeksoft.charset.Charsets


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * Parses a given string as the `host` of a URI reference, according to
 * [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986). If parsing
 * succeeds, this method creates a [Host] object. If parsing fails due to
 * invalid input string, it throws an `IllegalArgumentException`. Note that
 * this method works as if invoking it were equivalent to evaluating the expression
 * `[parse][.parse](host, [Charset].[ ][Charsets.UTF8])`.
 * 
 * @see [RFC 3986 - Uniform Resource
 * Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
internal class HostParser {
    /**
     * Parses a string as an `host` component of a URI reference based on
     * [RFC 3986](https://www.rfc-editor.org/rfc/rfc3986) and creates
     * a [Host] instance if parsing succeeds. If parsing fails due to invalid
     * input string, an `IllegalArgumentException` will be thrown.
     * 
     * @param host
     * Required. The input string for this parser to parse as an `host`.
     * 
     * @param charset
     * Required. The charset used in the `host`.
     * 
     * @return
     * The `Host` instance obtained by parsing the `host`
     * value as an `host` component.
     * 
     * @throws NullPointerException
     * If `charset` is `null`.
     * 
     * @throws IllegalArgumentException
     * If the `host` value is invalid as an `host`.
     * 
     * @see [
     * RFC 3986 Uniform Resource Identifier
    ](https://www.rfc-editor.org/rfc/rfc3986.section-4.2) */
    fun parse(host: String?, charset: Charset?): Host {
        if (charset == null) throw NullPointerException("charset is null")
        return Host(
            HostTypeDeterminer()
                .determine(host, charset), host
        )
    }
}
