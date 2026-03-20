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
 * A builder class for constructing [org.czeal.urireference.Authority] objects.
 * 
 * 
 * 
 * This class provides a fluent API to build an [org.czeal.urireference.Authority] object incrementally
 * by setting its various components such as `userinfo`, `host`, and
 * `port`.
 * 
 * 
 * @see [RFC 3986 - Uniform Resource
 * Identifier
 * @author Hideki Ikeda
](https://www.rfc-editor.org/rfc/rfc3986) */
internal class AuthorityBuilder {
    /**
     * The charset used for percent-encoding some characters (e.g. reserved characters)
     * contained in the resultant `authority` component.
     */
    private var charset: Charset? = null


    /**
     * The `userinfo` component of the resultant `authority` component.
     */
    private var userinfo: String? = null


    /**
     * The `host` component of the resultant `authority` component.
     */
    private var host: String? = null


    /**
     * The `port` component of the resultant `authority` component.
     */
    private var port = -1


    /**
     * Sets the charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the resultant `authority` component.
     * 
     * @param charset
     * The charset used for percent-encoding some characters (e.g. reserved
     * characters) contained in the resultant `authority` component.
     * 
     * @return
     * `this` object.
     */
    fun setCharset(charset: Charset?): AuthorityBuilder {
        this.charset = charset

        return this
    }


    /**
     * Sets the `userinfo` of the resultant `authority` component.
     * 
     * @param userinfo
     * The `userinfo` component of the resultant `authority`
     * component.
     * 
     * @return
     * `this` object.
     */
    fun setUserinfo(userinfo: String?): AuthorityBuilder {
        this.userinfo = userinfo

        return this
    }


    /**
     * Sets the `host` of the resultant `authority` component.
     * 
     * @param host
     * The `host` component of the resultant `authority` component.
     * 
     * @return
     * `this` object.
     */
    fun setHost(host: String?): AuthorityBuilder {
        this.host = host

        return this
    }


    /**
     * Sets the `port` of the resultant `authority` component.
     * 
     * @param port
     * The `port` component of the resultant `authority` component.
     * 
     * @return
     * `this` object.
     */
    fun setPort(port: Int): AuthorityBuilder {
        this.port = port

        return this
    }


    /**
     * Builds an [org.czeal.urireference.Authority] object.
     * 
     * @return
     * An [org.czeal.urireference.Authority] object representing the resultant `authority`
     * component.
     */
    fun build(): Authority {
        // The resultant Authority.
        val res = Authority.ProcessResult()

        // Process the userinfo.
        processUserinfo(res)

        // Process the host.
        processHost(res)

        // Process the port.
        processPort(res)

        // Build an Authority instance.
        return res.toAuthority()
    }


    private fun processUserinfo(res: Authority.ProcessResult) {
        // Validate the userinfo.
        UserinfoValidator().validate(userinfo, charset)

        // Set the userinfo.
        res.userinfo = userinfo
    }


    private fun processHost(res: Authority.ProcessResult) {
        // Set the host value.
        res.host = Host.parse(host, charset)
    }


    private fun processPort(res: Authority.ProcessResult) {
        // Validate the port.
        PortValidator().validate(port)

        // Set the port value.
        res.port = port
    }
}
