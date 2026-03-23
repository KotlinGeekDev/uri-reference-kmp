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

import kotlin.jvm.JvmStatic


/**
 * A class representing a single `query` parameter with a key and a value.
 * 
 * 
 * 
 * This class provides functionality to parse a `query` parameter from a
 * string and retrieve its key and value.
 * 
 * 
 * @author Hideki Ikeda
 */
internal class QueryParam
    (key: String?, value: String?) {
    /**
     * Gets the key of this `query` parameter.
     * 
     * @return The key of the `query` parameter.
     */
    /**
     * The key of the query parameter.
     */
    val key: String


    /**
     * Gets the value of this `query` parameter.
     * 
     * @return The value of the `query` parameter.
     */
    /**
     * The value of the query parameter.
     */
    val value: String?


    /**
     * Constructs a new [QueryParam] with the specified key and value.
     * 
     * @param key
     * The key of the `query` parameter. Cannot be `null`.
     * 
     * @param value
     * The value of the `query` parameter.
     * 
     * @throws NullPointerException
     * If the key is `null`.
     */
    init {
        if (key == null) {
            throw Utils.newNPE("The key must not be null.")
        }

        this.key = key
        this.value = value
    }


    /**
     * Returns a string representation of the query parameter.
     * 
     * 
     * 
     * The string is in the format `"key=value"`. If the value is `null`,
     * only the key is returned.
     * 
     * 
     * @return A string representation of the query parameter.
     */
    override fun toString(): String {
        if (value == null) {
            // If the value is null, just return the key.
            return key
        }

        // Concatenate the key and the value with "=".
        return "$key=$value"
    }

    companion object {
        /**
         * Converts an input string into a [QueryParam] object, distinguishing
         * between key and value.
         * 
         * 
         * 
         * This method parses strings of the form `"key=value"` into separate
         * "key" and "value" parts for the [QueryParam] object. If no `"="`
         * is present, the entire string is treated as the "key" with "value" being
         * `null`. The method only considers the first `"="` for splitting,
         * treating any subsequent `"="` as part of the "value".
         * 
         * 
         * @param queryParams
         * The query parameter string to parse.
         * 
         * @return
         * A new [QueryParam] object containing the parsed key and value.
         * 
         * @throws NullPointerException
         * If `input` is null.
         */
        @JvmStatic
        fun parse(queryParams: String?): QueryParam {
            if (queryParams == null) {
                throw Utils.newNPE("The input string must not be null.")
            }

            // Split the input string into two parts by the first "=" in the input string.
            val kv = queryParams.split("=".toRegex(), limit = 2).toTypedArray()

            // Set the key and value.
            val k = kv[0]

            // If the input string contains "=", set the value to the second element in the
            // array; otherwise, set it to null.
            val v = if (kv.size > 1) kv[1] else null

            // Create a QueryParam object.
            return QueryParam(k, v)
        }
    }
}
