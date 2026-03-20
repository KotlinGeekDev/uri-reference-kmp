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

import org.czeal.urireference.TestUtils.assertThrowsISE
import org.czeal.urireference.URIReference.Companion.parse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class URIReferenceNormalizerTest {
    @Test
    fun test_normalize() {
        val uriRef1 = URIReferenceNormalizer().normalize(parse("hTTp://example.com/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/", uriRef1.toString())
        Assertions.assertEquals(false, uriRef1!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef1.scheme)
        Assertions.assertEquals(true, uriRef1.hasAuthority())
        Assertions.assertEquals("example.com", uriRef1.authority.toString())
        Assertions.assertEquals(null, uriRef1.userinfo)
        Assertions.assertEquals("example.com", uriRef1.host.toString())
        Assertions.assertEquals("example.com", uriRef1.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef1.host!!.type)
        Assertions.assertEquals(-1, uriRef1.port)
        Assertions.assertEquals("/", uriRef1.path)
        Assertions.assertEquals(null, uriRef1.query)
        Assertions.assertEquals(null, uriRef1.fragment)

        val uriRef2 = URIReferenceNormalizer().normalize(parse("http://example.com/", StandardCharsets.UTF_8))
        Assertions.assertEquals(true, uriRef2!!.hasAuthority())
        Assertions.assertEquals("example.com", uriRef2.authority.toString())
        Assertions.assertEquals(null, uriRef2.userinfo)
        Assertions.assertEquals("example.com", uriRef2.host.toString())
        Assertions.assertEquals("example.com", uriRef2.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef2.host!!.type)
        Assertions.assertEquals(-1, uriRef2.port)
        Assertions.assertEquals(null, uriRef2.query)
        Assertions.assertEquals(null, uriRef2.fragment)

        val uriRef3 = URIReferenceNormalizer().normalize(parse("http://%75ser@example.com/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://user@example.com/", uriRef3.toString())
        Assertions.assertEquals(false, uriRef3!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef3.scheme)
        Assertions.assertEquals(true, uriRef3.hasAuthority())
        Assertions.assertEquals("user@example.com", uriRef3.authority.toString())
        Assertions.assertEquals("user", uriRef3.userinfo)
        Assertions.assertEquals("example.com", uriRef3.host.toString())
        Assertions.assertEquals("example.com", uriRef3.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef3.host!!.type)
        Assertions.assertEquals(-1, uriRef3.port)
        Assertions.assertEquals("/", uriRef3.path)
        Assertions.assertEquals(null, uriRef3.query)
        Assertions.assertEquals(null, uriRef3.fragment)

        val uriRef4 = URIReferenceNormalizer().normalize(
            parse(
                "http://%e3%83%a6%e3%83%bc%e3%82%b6%e3%83%bc@example.com/",
                StandardCharsets.UTF_8
            )
        )
        Assertions.assertEquals("http://%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC@example.com/", uriRef4.toString())
        Assertions.assertEquals(false, uriRef4!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef4.scheme)
        Assertions.assertEquals(true, uriRef4.hasAuthority())
        Assertions.assertEquals("%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC@example.com", uriRef4.authority.toString())
        Assertions.assertEquals("%E3%83%A6%E3%83%BC%E3%82%B6%E3%83%BC", uriRef4.userinfo)
        Assertions.assertEquals("example.com", uriRef4.host.toString())
        Assertions.assertEquals("example.com", uriRef4.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef4.host!!.type)
        Assertions.assertEquals(-1, uriRef4.port)
        Assertions.assertEquals("/", uriRef4.path)
        Assertions.assertEquals(null, uriRef4.query)
        Assertions.assertEquals(null, uriRef4.fragment)

        val uriRef5 =
            URIReferenceNormalizer().normalize(parse("http://%65%78%61%6D%70%6C%65.com/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/", uriRef5.toString())
        Assertions.assertEquals(false, uriRef5!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef5.scheme)
        Assertions.assertEquals(true, uriRef5.hasAuthority())
        Assertions.assertEquals("example.com", uriRef5.authority.toString())
        Assertions.assertEquals(null, uriRef5.userinfo)
        Assertions.assertEquals("example.com", uriRef5.host.toString())
        Assertions.assertEquals("example.com", uriRef5.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef5.host!!.type)
        Assertions.assertEquals(-1, uriRef5.port)
        Assertions.assertEquals("/", uriRef5.path)
        Assertions.assertEquals(null, uriRef5.query)
        Assertions.assertEquals(null, uriRef5.fragment)

        val uriRef6 = URIReferenceNormalizer().normalize(parse("http://%e4%be%8b.com/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://%E4%BE%8B.com/", uriRef6.toString())
        Assertions.assertEquals(false, uriRef6!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef6.scheme)
        Assertions.assertEquals(true, uriRef6.hasAuthority())
        Assertions.assertEquals("%E4%BE%8B.com", uriRef6.authority.toString())
        Assertions.assertEquals(null, uriRef6.userinfo)
        Assertions.assertEquals("%E4%BE%8B.com", uriRef6.host.toString())
        Assertions.assertEquals("%E4%BE%8B.com", uriRef6.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef6.host!!.type)
        Assertions.assertEquals(-1, uriRef6.port)
        Assertions.assertEquals("/", uriRef6.path)
        Assertions.assertEquals(null, uriRef6.query)
        Assertions.assertEquals(null, uriRef6.fragment)

        val uriRef7 = URIReferenceNormalizer().normalize(parse("http://LOCALhost/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://localhost/", uriRef7.toString())
        Assertions.assertEquals(false, uriRef7!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef7.scheme)
        Assertions.assertEquals(true, uriRef7.hasAuthority())
        Assertions.assertEquals("localhost", uriRef7.authority.toString())
        Assertions.assertEquals(null, uriRef7.userinfo)
        Assertions.assertEquals("localhost", uriRef7.host.toString())
        Assertions.assertEquals("localhost", uriRef7.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef7.host!!.type)
        Assertions.assertEquals(-1, uriRef7.port)
        Assertions.assertEquals("/", uriRef7.path)
        Assertions.assertEquals(null, uriRef7.query)
        Assertions.assertEquals(null, uriRef7.fragment)

        val uriRef8 = URIReferenceNormalizer().normalize(parse("http://example.com", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/", uriRef8.toString())
        Assertions.assertEquals(false, uriRef8!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef8.scheme)
        Assertions.assertEquals(true, uriRef8.hasAuthority())
        Assertions.assertEquals("example.com", uriRef8.authority.toString())
        Assertions.assertEquals(null, uriRef8.userinfo)
        Assertions.assertEquals("example.com", uriRef8.host.toString())
        Assertions.assertEquals("example.com", uriRef8.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef8.host!!.type)
        Assertions.assertEquals(-1, uriRef8.port)
        Assertions.assertEquals("/", uriRef8.path)
        Assertions.assertEquals(null, uriRef8.query)
        Assertions.assertEquals(null, uriRef8.fragment)

        val uriRef9 =
            URIReferenceNormalizer().normalize(parse("http://example.com/%61/%62/%63/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/a/b/c/", uriRef9.toString())
        Assertions.assertEquals(false, uriRef9!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef9.scheme)
        Assertions.assertEquals(true, uriRef9.hasAuthority())
        Assertions.assertEquals("example.com", uriRef9.authority.toString())
        Assertions.assertEquals(null, uriRef9.userinfo)
        Assertions.assertEquals("example.com", uriRef9.host.toString())
        Assertions.assertEquals("example.com", uriRef9.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef9.host!!.type)
        Assertions.assertEquals(-1, uriRef9.port)
        Assertions.assertEquals("/a/b/c/", uriRef9.path)
        Assertions.assertEquals(null, uriRef9.query)
        Assertions.assertEquals(null, uriRef9.fragment)

        val uriRef10 =
            URIReferenceNormalizer().normalize(parse("http://example.com/%e3%83%91%e3%82%b9/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/%E3%83%91%E3%82%B9/", uriRef10.toString())
        Assertions.assertEquals(false, uriRef10!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef10.scheme)
        Assertions.assertEquals(true, uriRef10.hasAuthority())
        Assertions.assertEquals("example.com", uriRef10.authority.toString())
        Assertions.assertEquals(null, uriRef10.userinfo)
        Assertions.assertEquals("example.com", uriRef10.host.toString())
        Assertions.assertEquals("example.com", uriRef10.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef10.host!!.type)
        Assertions.assertEquals(-1, uriRef10.port)
        Assertions.assertEquals("/%E3%83%91%E3%82%B9/", uriRef10.path)
        Assertions.assertEquals(null, uriRef10.query)
        Assertions.assertEquals(null, uriRef10.fragment)

        val uriRef11 =
            URIReferenceNormalizer().normalize(parse("http://example.com/a/b/c/../d/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/a/b/d/", uriRef11.toString())
        Assertions.assertEquals(false, uriRef11!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef11.scheme)
        Assertions.assertEquals(true, uriRef11.hasAuthority())
        Assertions.assertEquals("example.com", uriRef11.authority.toString())
        Assertions.assertEquals(null, uriRef11.userinfo)
        Assertions.assertEquals("example.com", uriRef11.host.toString())
        Assertions.assertEquals("example.com", uriRef11.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef11.host!!.type)
        Assertions.assertEquals(-1, uriRef11.port)
        Assertions.assertEquals("/a/b/d/", uriRef11.path)
        Assertions.assertEquals(null, uriRef11.query)
        Assertions.assertEquals(null, uriRef11.fragment)

        val uriRef12 = URIReferenceNormalizer().normalize(parse("http://example.com:80/", StandardCharsets.UTF_8))
        Assertions.assertEquals("http://example.com/", uriRef12.toString())
        Assertions.assertEquals(false, uriRef12!!.isRelativeReference)
        Assertions.assertEquals("http", uriRef12.scheme)
        Assertions.assertEquals(true, uriRef12.hasAuthority())
        Assertions.assertEquals("example.com", uriRef12.authority.toString())
        Assertions.assertEquals(null, uriRef12.userinfo)
        Assertions.assertEquals("example.com", uriRef12.host.toString())
        Assertions.assertEquals("example.com", uriRef12.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef12.host!!.type)
        Assertions.assertEquals(-1, uriRef12.port)
        Assertions.assertEquals("/", uriRef12.path)
        Assertions.assertEquals(null, uriRef12.query)
        Assertions.assertEquals(null, uriRef12.fragment)

        assertThrowsISE<Throwable>(
            "A relative references must be resolved before it can be normalized.",
            { URIReferenceNormalizer().normalize(parse("//example.com", StandardCharsets.UTF_8)) })
    }
}
