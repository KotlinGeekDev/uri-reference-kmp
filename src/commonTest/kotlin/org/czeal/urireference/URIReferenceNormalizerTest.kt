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

import com.fleeksoft.charset.Charsets
import org.czeal.urireference.TestUtils.assertThrowsISE
import org.czeal.urireference.URIReference.Companion.parse
import kotlin.test.Test
import kotlin.test.assertEquals


class URIReferenceNormalizerTest {
    @Test
    fun test_normalize() {
        val uriRef1 = URIReferenceNormalizer().normalize(parse("hTTp://example.com/", Charsets.UTF8))
        assertEquals("http://example.com/", uriRef1.toString())
        assertEquals(false, uriRef1.isRelativeReference)
        assertEquals("http", uriRef1.scheme)
        assertEquals(true, uriRef1.hasAuthority())
        assertEquals("example.com", uriRef1.authority.toString())
        assertEquals(null, uriRef1.userinfo)
        assertEquals("example.com", uriRef1.host.toString())
        assertEquals("example.com", uriRef1.host!!.value)
        assertEquals(HostType.REGNAME, uriRef1.host!!.type)
        assertEquals(-1, uriRef1.port)
        assertEquals("/", uriRef1.path)
        assertEquals(null, uriRef1.query)
        assertEquals(null, uriRef1.fragment)

        val uriRef2 = URIReferenceNormalizer().normalize(parse("http://example.com/", Charsets.UTF8))
        assertEquals(true, uriRef2.hasAuthority())
        assertEquals("example.com", uriRef2.authority.toString())
        assertEquals(null, uriRef2.userinfo)
        assertEquals("example.com", uriRef2.host.toString())
        assertEquals("example.com", uriRef2.host!!.value)
        assertEquals(HostType.REGNAME, uriRef2.host!!.type)
        assertEquals(-1, uriRef2.port)
        assertEquals(null, uriRef2.query)
        assertEquals(null, uriRef2.fragment)

        val uriRef3 = URIReferenceNormalizer().normalize(parse("http://%75ser@example.com/", Charsets.UTF8))
        assertEquals("http://user@example.com/", uriRef3.toString())
        assertEquals(false, uriRef3.isRelativeReference)
        assertEquals("http", uriRef3.scheme)
        assertEquals(true, uriRef3.hasAuthority())
        assertEquals("user@example.com", uriRef3.authority.toString())
        assertEquals("user", uriRef3.userinfo)
        assertEquals("example.com", uriRef3.host.toString())
        assertEquals("example.com", uriRef3.host!!.value)
        assertEquals(HostType.REGNAME, uriRef3.host!!.type)
        assertEquals(-1, uriRef3.port)
        assertEquals("/", uriRef3.path)
        assertEquals(null, uriRef3.query)
        assertEquals(null, uriRef3.fragment)

        val uriRef4 = URIReferenceNormalizer().normalize(
            parse(
                "http://%e3%83%a6%e3%83%bc%e3%82%b6%e3%83%bc@example.com/",
                Charsets.UTF8
            )
        )
        assertEquals("http://%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC@example.com/", uriRef4.toString())
        assertEquals(false, uriRef4.isRelativeReference)
        assertEquals("http", uriRef4.scheme)
        assertEquals(true, uriRef4.hasAuthority())
        assertEquals("%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC@example.com", uriRef4.authority.toString())
        assertEquals("%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC", uriRef4.userinfo)
        assertEquals("example.com", uriRef4.host.toString())
        assertEquals("example.com", uriRef4.host!!.value)
        assertEquals(HostType.REGNAME, uriRef4.host!!.type)
        assertEquals(-1, uriRef4.port)
        assertEquals("/", uriRef4.path)
        assertEquals(null, uriRef4.query)
        assertEquals(null, uriRef4.fragment)

        val uriRef5 =
            URIReferenceNormalizer().normalize(parse("http://%65%78%61%6D%70%6C%65.com/", Charsets.UTF8))
        assertEquals("http://example.com/", uriRef5.toString())
        assertEquals(false, uriRef5.isRelativeReference)
        assertEquals("http", uriRef5.scheme)
        assertEquals(true, uriRef5.hasAuthority())
        assertEquals("example.com", uriRef5.authority.toString())
        assertEquals(null, uriRef5.userinfo)
        assertEquals("example.com", uriRef5.host.toString())
        assertEquals("example.com", uriRef5.host!!.value)
        assertEquals(HostType.REGNAME, uriRef5.host!!.type)
        assertEquals(-1, uriRef5.port)
        assertEquals("/", uriRef5.path)
        assertEquals(null, uriRef5.query)
        assertEquals(null, uriRef5.fragment)

        val uriRef6 = URIReferenceNormalizer().normalize(parse("http://%e4%be%8b.com/", Charsets.UTF8))
        assertEquals("http://%E4%BE%8B.com/", uriRef6.toString())
        assertEquals(false, uriRef6.isRelativeReference)
        assertEquals("http", uriRef6.scheme)
        assertEquals(true, uriRef6.hasAuthority())
        assertEquals("%E4%BE%8B.com", uriRef6.authority.toString())
        assertEquals(null, uriRef6.userinfo)
        assertEquals("%E4%BE%8B.com", uriRef6.host.toString())
        assertEquals("%E4%BE%8B.com", uriRef6.host!!.value)
        assertEquals(HostType.REGNAME, uriRef6.host!!.type)
        assertEquals(-1, uriRef6.port)
        assertEquals("/", uriRef6.path)
        assertEquals(null, uriRef6.query)
        assertEquals(null, uriRef6.fragment)

        val uriRef7 = URIReferenceNormalizer().normalize(parse("http://LOCALhost/", Charsets.UTF8))
        assertEquals("http://localhost/", uriRef7.toString())
        assertEquals(false, uriRef7.isRelativeReference)
        assertEquals("http", uriRef7.scheme)
        assertEquals(true, uriRef7.hasAuthority())
        assertEquals("localhost", uriRef7.authority.toString())
        assertEquals(null, uriRef7.userinfo)
        assertEquals("localhost", uriRef7.host.toString())
        assertEquals("localhost", uriRef7.host!!.value)
        assertEquals(HostType.REGNAME, uriRef7.host!!.type)
        assertEquals(-1, uriRef7.port)
        assertEquals("/", uriRef7.path)
        assertEquals(null, uriRef7.query)
        assertEquals(null, uriRef7.fragment)

        val uriRef8 = URIReferenceNormalizer().normalize(parse("http://example.com", Charsets.UTF8))
        assertEquals("http://example.com/", uriRef8.toString())
        assertEquals(false, uriRef8.isRelativeReference)
        assertEquals("http", uriRef8.scheme)
        assertEquals(true, uriRef8.hasAuthority())
        assertEquals("example.com", uriRef8.authority.toString())
        assertEquals(null, uriRef8.userinfo)
        assertEquals("example.com", uriRef8.host.toString())
        assertEquals("example.com", uriRef8.host!!.value)
        assertEquals(HostType.REGNAME, uriRef8.host!!.type)
        assertEquals(-1, uriRef8.port)
        assertEquals("/", uriRef8.path)
        assertEquals(null, uriRef8.query)
        assertEquals(null, uriRef8.fragment)

        val uriRef9 =
            URIReferenceNormalizer().normalize(parse("http://example.com/%61/%62/%63/", Charsets.UTF8))
        assertEquals("http://example.com/a/b/c/", uriRef9.toString())
        assertEquals(false, uriRef9.isRelativeReference)
        assertEquals("http", uriRef9.scheme)
        assertEquals(true, uriRef9.hasAuthority())
        assertEquals("example.com", uriRef9.authority.toString())
        assertEquals(null, uriRef9.userinfo)
        assertEquals("example.com", uriRef9.host.toString())
        assertEquals("example.com", uriRef9.host!!.value)
        assertEquals(HostType.REGNAME, uriRef9.host!!.type)
        assertEquals(-1, uriRef9.port)
        assertEquals("/a/b/c/", uriRef9.path)
        assertEquals(null, uriRef9.query)
        assertEquals(null, uriRef9.fragment)

        val uriRef10 =
            URIReferenceNormalizer().normalize(parse("http://example.com/%e3%83%91%e3%82%b9/", Charsets.UTF8))
        assertEquals("http://example.com/%E3%83%91%E3%82%B9/", uriRef10.toString())
        assertEquals(false, uriRef10.isRelativeReference)
        assertEquals("http", uriRef10.scheme)
        assertEquals(true, uriRef10.hasAuthority())
        assertEquals("example.com", uriRef10.authority.toString())
        assertEquals(null, uriRef10.userinfo)
        assertEquals("example.com", uriRef10.host.toString())
        assertEquals("example.com", uriRef10.host!!.value)
        assertEquals(HostType.REGNAME, uriRef10.host!!.type)
        assertEquals(-1, uriRef10.port)
        assertEquals("/%E3%83%91%E3%82%B9/", uriRef10.path)
        assertEquals(null, uriRef10.query)
        assertEquals(null, uriRef10.fragment)

        val uriRef11 =
            URIReferenceNormalizer().normalize(parse("http://example.com/a/b/c/../d/", Charsets.UTF8))
        assertEquals("http://example.com/a/b/d/", uriRef11.toString())
        assertEquals(false, uriRef11.isRelativeReference)
        assertEquals("http", uriRef11.scheme)
        assertEquals(true, uriRef11.hasAuthority())
        assertEquals("example.com", uriRef11.authority.toString())
        assertEquals(null, uriRef11.userinfo)
        assertEquals("example.com", uriRef11.host.toString())
        assertEquals("example.com", uriRef11.host!!.value)
        assertEquals(HostType.REGNAME, uriRef11.host!!.type)
        assertEquals(-1, uriRef11.port)
        assertEquals("/a/b/d/", uriRef11.path)
        assertEquals(null, uriRef11.query)
        assertEquals(null, uriRef11.fragment)

        val uriRef12 = URIReferenceNormalizer().normalize(parse("http://example.com:80/", Charsets.UTF8))
        assertEquals("http://example.com/", uriRef12.toString())
        assertEquals(false, uriRef12.isRelativeReference)
        assertEquals("http", uriRef12.scheme)
        assertEquals(true, uriRef12.hasAuthority())
        assertEquals("example.com", uriRef12.authority.toString())
        assertEquals(null, uriRef12.userinfo)
        assertEquals("example.com", uriRef12.host.toString())
        assertEquals("example.com", uriRef12.host!!.value)
        assertEquals(HostType.REGNAME, uriRef12.host!!.type)
        assertEquals(-1, uriRef12.port)
        assertEquals("/", uriRef12.path)
        assertEquals(null, uriRef12.query)
        assertEquals(null, uriRef12.fragment)

        assertThrowsISE<Throwable>(
            "A relative references must be resolved before it can be normalized.",
            { URIReferenceNormalizer().normalize(parse("//example.com", Charsets.UTF8)) })
    }
}
