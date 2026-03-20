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

import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * A class representing a collection of `path` segments in a URI reference.
 * 
 * 
 * 
 * This class provides functionality to parse, add, and manage individual path segments
 * of a URI reference, handling them as a list of strings.
 * 
 * 
 * @author Hideki Ikeda
 */
internal class PathSegments
@JvmOverloads constructor(segments: MutableList<String?> = ArrayList(0)) {
    /**
     * Internal storage for path segments.
     */
    private val segments: MutableList<String?>?


    /**
     * Creates a [PathSegments] instance with a predefined list of segments.
     * 
     * 
     * 
     * This constructor allows for initializing the path segments from an existing
     * list.
     * 
     * 
     * @param segments
     * A list of strings representing path segments.
     * 
     * @throws NullPointerException
     * if the provided list of segments is `null`.
     */
    /**
     * Creates an empty [PathSegments] instance.
     * 
     * 
     * 
     * This constructor initializes the internal list of segments as empty.
     * 
     */
    init {
        if (segments == null) {
            throw Utils.newNPE("params must not be null.")
        }

        this.segments = segments
    }


    /**
     * Adds one or more path segments to the current [PathSegments] instance.
     * Each segment is appended in order to the internal list of segments.
     * 
     * @param segments
     * An array of strings representing additional path segments to be added.
     * 
     * @return
     * `this` object.
     * 
     * @throws NullPointerException
     * if the provided array of segments, or any individual segment, is
     * `null`.
     */
    fun add(segments: List<String?>?): PathSegments {
        if (segments == null) {
            throw Utils.newNPE("The segments must not be null.")
        }

        for (s in segments) {
            if (s == null) {
                throw Utils.newNPE("A segment must not be null.")
            }
        }

        // Add the given segments.
        this.segments?.addAll(segments)

        // Return this object.
        return this
    }


    /**
     * Returns a string representation of the path segments.
     * 
     * 
     * 
     * The segments are concatenated with "/" as a delimiter.
     * 
     * 
     * @return
     * A string representation of the path segments, or an empty string
     * if there are no segments.
     */
    override fun toString(): String {
        if (segments == null) {
            return ""
        }

        return segments.joinToString(separator = "/")
    }

    companion object {
        /**
         * Parses a string input into a [PathSegments] object.
         * 
         * 
         * 
         * The input string is split into segments based on `"/"` delimiters.
         * Each segment is treated as a separate path component.
         * 
         * 
         * @param pathSegments
         * The string input representing path segments.
         * 
         * @return
         * A new [PathSegments] object containing the parsed segments,
         * or `null` if the input is `null`.
         */
        @JvmStatic
        fun parse(pathSegments: String?): PathSegments? {
            if (pathSegments == null) {
                return null
            }
            else {
                // Convert the input string to a list of Strings.
                val segments: MutableList<String?> = ArrayList(
                    listOf(
                        *pathSegments.split("/".toRegex()).toTypedArray()
                    )
                )

                // Create a PathSegments object.
                return PathSegments(segments)
            }

        }
    }
}
