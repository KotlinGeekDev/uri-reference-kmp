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


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * 
 * 
 * Validates the `port` component of a URI reference according to the syntax
 * defined in [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986#appendix-A) as follows.
 * 
 * 
 * <blockquote>
 * <pre style="font-family: 'Menlo', 'Courier', monospace;">`port = *DIGIT `</pre>
</blockquote> * 
 * 
 * @see [RFC 3986,
 * Appendix A. Collected ABNF for URI](https://www.rfc-editor.org/rfc/rfc3986.appendix-A)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class PortValidator {
    /**
     * Validates a value as a port.
     * 
     * @param port
     * A port value.
     * 
     * @throws IllegalArgumentException
     * If the `port` value is invalid.
     */
    fun validate(port: String?) {
        if (port == null) {
            // If the port value is null, it means the input string doesn't
            // contain a port.
            return
        }

        if (port.isEmpty()) {
            // If the port value is empty, it means the input string contains
            // a colon (":") delimiter for the port value but the port value is
            // empty.
            return
        }

        for (i in 0..<port.length) {
            val c = port[i]

            if (!Utils.isDigit(c)) {
                throw Utils.newIAE(
                    "The port value \"%s\" has an invalid character \"%s\" at the " +
                            "index %d.", port, c, i
                )
            }
        }
    }


    /**
     * Validates a value as a port.
     * 
     * @param port
     * A port value.
     * 
     * @throws IllegalArgumentException
     * If the `port` value is invalid.
     */
    fun validate(port: Int) {
        if (port == -1) {
            // -1 is the default value.
            return
        }

        if (port < 0) {
            throw Utils.newIAE("The port value \"%d\" is negative.", port)
        }
    }
}
