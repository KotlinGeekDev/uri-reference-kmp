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

import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * A class representing a collection of `query` parameters.
 * 
 * 
 * 
 * This class provides functionality to parse a string of `query` parameters,
 * add, replace, and remove parameters, and convert the collection back to a string
 * format.
 * 
 * 
 * @author Hideki Ikeda
 */
internal class QueryParams
@JvmOverloads constructor(params: MutableList<QueryParam?>? = ArrayList(0)) {
    /**
     * Internal storage for query parameters.
     */
    private var params: MutableList<QueryParam?>


    /**
     * Constructs a [QueryParams] object with the specified list of parameters.
     * 
     * @param params
     * The list of [QueryParam] objects to be included in the [         ].
     * 
     * @throws NullPointerException
     * If the provided list of parameters is `null`.
     */
    /**
     * Constructs an empty [QueryParams] object.
     */
    init {
        if (params == null) {
            throw Utils.newNPE("The params must not be null.")
        }

        this.params = params
    }


    /**
     * Adds a new parameter to the collection.
     * 
     * @param key
     * The key of the query parameter.
     * 
     * @param value
     * The value of the query parameter.
     * 
     * @return
     * `this` object.
     */
    fun add(key: String?, value: String?): QueryParams {
        params.add(QueryParam(key, value))

        return this
    }


    /**
     * Replaces the value of an existing parameter. If the key does not exist, no
     * action is taken.
     * 
     * @param key
     * The key of the query parameter to replace.
     * 
     * @param value
     * The new value for the query parameter.
     * 
     * @return
     * `this` object.
     */
    fun replace(key: String?, value: String?): QueryParams {
        params = params
            .map { e: QueryParam? -> if (e!!.key == key) QueryParam(
                key,
                value
            ) else e }
            .toMutableList()


        return this
    }


    /**
     * Removes a parameter from the collection based on its key. If the key does
     * not exist, no action is taken.
     * 
     * @param key
     * The key of the query parameter to remove.
     * 
     * @return
     * `this` object.
     */
    fun remove(key: String?): QueryParams {
        params = params
            .filter { e: QueryParam? -> e!!.key != key }
            .toMutableList()

        return this
    }


    val isEmpty: Boolean
        /**
         * Returns whether this query parameters is empty or not.
         * 
         * @return
         * Whether this query parameters is empty or not.
         */
        get() = params.isEmpty()


    /**
     * Returns a string representation of the `query` parameters in this
     * collection.
     * 
     * @return
     * A string representation of the `query` parameters, formatted
     * as `"key1=value1&key2=value2"`.
     */
    override fun toString(): String {
        return params.joinToString("&") { obj: QueryParam? -> obj.toString() }
    }

    companion object {
        /**
         * Parses the input string to create a new [QueryParams] object.
         * 
         * 
         * 
         * This method processes an input string containing query parameters and constructs
         * a corresponding [QueryParams] instance. The input string should be
         * in the format of a query string, such as `"key1=value1&key2=value2"`.
         * Each key-value pair is separated by the `"&"` character, and each key
         * is separated from its value by the `"="` character.
         * 
         * 
         * 
         * 
         * If the input string is `null`, this method returns `null`. If a segment
         * of the input string does not contain an `"="` character, that segment
         * is interpreted as a key with a `null` value. If the same key appears
         * multiple times with different values (e.g., `"key1=value1&key1=value2"`),
         * each occurrence is added as a separate [QueryParam] object in the
         * [QueryParams] object.
         * 
         * 
         * @param queryParams
         * The input string representation of query parameters, formatted as `"key1=value1&key2=value2"`. It can be `null`, in which case
         * the method returns `null`.
         * 
         * @return
         * A new [QueryParams] object representing the parsed parameters.
         * Returns `null` if the input string is `null`.
         */
        @JvmStatic
        fun parse(queryParams: String?): QueryParams? {
            if (queryParams == null) {
                return null
            }

            // Convert the input string to a list of QueryPrams.
            val params: MutableList<QueryParam?> =
                queryParams.split("&".toRegex()).toTypedArray()
                    .map { e: String? -> QueryParam.parse(e!!) }
                    .toMutableList()

            // Create a Query object.
            return QueryParams(params)
        }
    }
}
