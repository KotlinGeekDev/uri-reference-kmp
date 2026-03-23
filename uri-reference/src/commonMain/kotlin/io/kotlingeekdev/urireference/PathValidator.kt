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
 * Validates for "path" component of a URI reference according to the syntax defined
 * in [RFC 3986, 3.3.
 * Path](https://www.rfc-editor.org/rfc/rfc3986#section-3.3) and [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A) as follows.
 * 
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`<u>RFC 3986, 3.3. Path</u>   If a URI contains an authority component, then the path component must   either be empty or begin with a slash ("/") character. If a URI does   not contain an authority component, then the path cannot begin with   two slash characters ("//"). In addition, a URI reference (Section 4.1)   may be a relative-path reference, in which case the first path segment   cannot contain a colon (":") character. <u>RFC 3986, Appendix A. Collected ABNF for URI</u>   URI           = scheme ":" hier-part [ "?" query ] [ "#" fragment ]   hier-part     = "//" authority path-abempty                 / path-absolute                 / path-rootless                 / path-empty   relative-ref  = relative-part [ "?" query ] [ "#" fragment ]   relative-part = "//" authority path-abempty                 / path-absolute                 / path-noscheme                 / path-empty   path-abempty  = *( "/" segment )   path-absolute = "/" [ segment-nz *( "/" segment ) ]   path-noscheme = segment-nz-nc *( "/" segment )   path-rootless = segment-nz *( "/" segment )   path-empty    = <pchar> `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986,
 * 3.3. Path](https://www.rfc-editor.org/rfc/rfc3986.section-3.3)
 * 
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class PathValidator {
    /**
     * Validates a path value.
     * 
     * @param path
     * A path value.
     * 
     * @param charset
     * The charset used for percent-encoding the path value.
     * 
     * @param relativeReference
     * Whether or not the URI reference is a relative reference.
     * 
     * @param hasAuthority
     * Whether or not the URI reference has an authority.
     */
    fun validate(
        path: String?, charset: Charset, relativeReference: Boolean, hasAuthority: Boolean
    ) {
        // RFC 3986, 3.3. Path
        //
        //   If a URI contains an authority component, then the path component must
        //   either be empty or begin with a slash ("/") character. If a URI does
        //   not contain an authority component, then the path cannot begin with
        //   two slash characters ("//"). In addition, a URI reference (Section 4.1)
        //   may be a relative-path reference, in which case the first path segment
        //   cannot contain a colon (":") character.
        //

        // RFC 3986, Appendix A. Collected ABNF for URI
        //
        //   URI           = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
        //
        //   hier-part     = "//" authority path-abempty
        //                 / path-absolute
        //                 / path-rootless
        //                 / path-empty
        //
        //   relative-ref  = relative-part [ "?" query ] [ "#" fragment ]
        //
        //   relative-part = "//" authority path-abempty
        //                 / path-absolute
        //                 / path-noscheme
        //                 / path-empty
        //
        //   path-abempty  = *( "/" segment )
        //   path-absolute = "/" [ segment-nz *( "/" segment ) ]
        //   path-noscheme = segment-nz-nc *( "/" segment )
        //   path-rootless = segment-nz *( "/" segment )
        //   path-empty    = 0<pchar>
        //

        if (isValidPath(path, charset, relativeReference, hasAuthority)) {
            return
        }

        throw Utils.newIAE("The path value is invalid.")
    }


    private fun isValidPath(
        path: String?,
        charset: Charset,
        relativeReference: Boolean,
        hasAuthority: Boolean
    ): Boolean {
        if (hasAuthority) {
            // When the URI reference has an authority, "path-abempty"
            // is only allowed.
            return isPathAbempty(path, charset)
        }

        // When the URI reference does not have an authority, one of
        // the following types is allowed:
        //
        //   - "path-empty"
        //   - "path-absolute"
        //   - "path-noscheme" (when relativeReference is true)
        //   - "path-rootless" (when relativeReference is false)
        return if (relativeReference) (isPathEmpty(path) || isPathAbsolute(path!!, charset) || isPathNoscheme(
            path,
            charset
        )) else (isPathEmpty(path) || isPathAbsolute(path!!, charset) || isPathRootless(path, charset))
    }


    private fun isPathAbempty(path: String?, charset: Charset): Boolean {
        if (isPathEmpty(path)) {
            // The path value is null or an empty string. Then, the path
            // value is a "path-abempty".
            return true
        }

        if (!path!!.startsWith("/")) {
            // The path value does not start with a slash. Then, the path
            // value is not a "path-abempty".
            return false
        }

        if (path.length == 1) {
            // The path only contains the first slash. Then, the path
            // value is a "path-abempty".
            return true
        }

        try {
            // The path segments.
            val segments: Array<String?> = path.substring(1).split("/".toRegex()).toTypedArray()

            // Validate each segment.
            for (i in segments.indices) {
                SegmentValidator().validate(segments[i], charset)
            }
        } catch (t: Throwable) {
            // A segment in the path is invalid. Then, the path value
            // is not a "path-abempty".
            return false
        }

        // The path value is a "path-abempty".
        return true
    }


    private fun isPathEmpty(path: String?): Boolean {
        return path == null || path.isEmpty()
    }


    private fun isPathAbsolute(path: String, charset: Charset): Boolean {
        // We don't call isPathEmpty() here because we assume it is called
        // prior to this method.
        // if (isPathEmpty(path))
        // {
        //     // The path value is null or an empty string. Then, the
        //     // path value is not a "path-absolute".
        //     return false;
        // }

        if (!path.startsWith("/")) {
            // The path value does not start with a slash. Then, the path
            // value is not a "path-absolute".
            return false
        }

        if (path.length == 1) {
            // The path value only contains the first slash. Then, the
            // path value is a "path-absolute".
            return true
        }

        // Split the path into segments.
        val segments: Array<String?> = path.substring(1).split("/".toRegex()).toTypedArray()

        try {
            // Validate the first element.
            SegmentNzValidator().validate(segments[0], charset)

            // Validate remaining segments.
            for (i in 1..<segments.size) {
                SegmentValidator().validate(segments[i], charset)
            }
        } catch (t: Throwable) {
            // A segment in the path is invalid. Then, the path value
            // is not a "path-absolute".
            return false
        }

        // The path value is a "path-absolute".
        return true
    }


    private fun isPathNoscheme(path: String, charset: Charset): Boolean {
        // We don't call isPathEmpty() here because we assume it is called
        // prior to this method.
        // if (isPathEmpty(path))
        // {
        //     // The path value is null or an empty string. Then, the
        //     // path value is not a "path-noscheme".
        //     return false;
        // }

        // Split the path into segments.

        val segments: Array<String?> = path.split("/".toRegex()).toTypedArray()

        try {
            // Validate the first element.
            SegmentNzNcValidator().validate(segments[0], charset)

            // Validate the remaining segments.
            for (i in 1..<segments.size) {
                SegmentValidator().validate(segments[i], charset)
            }
        } catch (t: Throwable) {
            // A segment in the path is invalid. Then, the path value
            // is not a "path-noscheme".
            return false
        }

        // The path value is a "path-noscheme".
        return true
    }


    private fun isPathRootless(path: String, charset: Charset): Boolean {
        // We don't call isPathEmpty() here because we assume it is called
        // prior to this method.
        // if (isPathEmpty(path))
        // {
        //     // The path value is null or an empty string. Then, the
        //     // path value is not a "path-rootless".
        //     return false;
        // }

        // Split the path into segments.

        val segments: Array<String?> = path.split("/".toRegex()).toTypedArray()

        try {
            // Validate the first element.
            SegmentNzValidator().validate(segments[0], charset)

            // Validate the remaining segments.
            for (i in 1..<segments.size) {
                SegmentValidator().validate(segments[i], charset)
            }
        } catch (t: Throwable) {
            // A segment in the path is invalid. Then, the path value
            // is not a "path-rootless".
            return false
        }

        // The path value is a "path-rootless".
        return true
    }
}
