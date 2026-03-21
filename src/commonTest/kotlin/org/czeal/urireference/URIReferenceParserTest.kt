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
import org.czeal.urireference.TestUtils.assertThrowsIAE
import kotlin.test.Test
import kotlin.test.assertEquals


class URIReferenceParserTest {
    @Test
    fun test_parse() {
        val uriRef1 = URIReferenceParser().parse("http://example.com", Charsets.UTF8)
        assertEquals("http://example.com", uriRef1.toString())
        assertEquals(false, uriRef1.isRelativeReference)
        assertEquals("http", uriRef1.scheme)
        assertEquals(true, uriRef1.hasAuthority())
        assertEquals("example.com", uriRef1.authority.toString())
        assertEquals(null, uriRef1.userinfo)
        assertEquals("example.com", uriRef1.host.toString())
        assertEquals("example.com", uriRef1.host!!.value)
        assertEquals(HostType.REGNAME, uriRef1.host!!.type)
        assertEquals(-1, uriRef1.port)
        assertEquals("", uriRef1.path)
        assertEquals(null, uriRef1.query)
        assertEquals(null, uriRef1.fragment)

        val uriRef2 = URIReferenceParser().parse("hTTp://example.com", Charsets.UTF8)

        assertEquals("hTTp://example.com", uriRef2.toString())
        assertEquals(false, uriRef2.isRelativeReference)
        assertEquals("hTTp", uriRef2.scheme)
        assertEquals(true, uriRef2.hasAuthority())
        assertEquals("example.com", uriRef2.authority.toString())
        assertEquals(null, uriRef2.userinfo)
        assertEquals("example.com", uriRef2.host.toString())
        assertEquals("example.com", uriRef2.host!!.value)
        assertEquals(HostType.REGNAME, uriRef2.host!!.type)
        assertEquals(-1, uriRef2.port)
        assertEquals("", uriRef2.path)
        assertEquals(null, uriRef2.query)
        assertEquals(null, uriRef2.fragment)

        val uriRef3 = URIReferenceParser().parse("//example.com", Charsets.UTF8)

        assertEquals("//example.com", uriRef3.toString())
        assertEquals(true, uriRef3.isRelativeReference)
        assertEquals(null, uriRef3.scheme)
        assertEquals(true, uriRef3.hasAuthority())
        assertEquals("example.com", uriRef3.authority.toString())
        assertEquals(null, uriRef3.userinfo)
        assertEquals("example.com", uriRef3.host.toString())
        assertEquals("example.com", uriRef3.host!!.value)
        assertEquals(HostType.REGNAME, uriRef3.host!!.type)
        assertEquals(-1, uriRef3.port)
        assertEquals("", uriRef3.path)
        assertEquals(null, uriRef3.query)
        assertEquals(null, uriRef3.fragment)

        val uriRef4 = URIReferenceParser().parse("http:", Charsets.UTF8)

        assertEquals(false, uriRef4.isRelativeReference)
        assertEquals("http", uriRef4.scheme)
        assertEquals(false, uriRef4.hasAuthority())
        assertEquals(null, uriRef4.authority)
        assertEquals(null, uriRef4.userinfo)
        assertEquals(null, uriRef4.host)
        assertEquals(-1, uriRef4.port)
        assertEquals("", uriRef4.path)
        assertEquals(null, uriRef4.query)
        assertEquals(null, uriRef4.fragment)

        val uriRef5 = URIReferenceParser().parse("http://john@example.com", Charsets.UTF8)

        assertEquals("http://john@example.com", uriRef5.toString())
        assertEquals(false, uriRef5.isRelativeReference)
        assertEquals("http", uriRef5.scheme)
        assertEquals(true, uriRef5.hasAuthority())
        assertEquals("john@example.com", uriRef5.authority.toString())
        assertEquals("john", uriRef5.userinfo)
        assertEquals("example.com", uriRef5.host.toString())
        assertEquals("example.com", uriRef5.host!!.value)
        assertEquals(HostType.REGNAME, uriRef5.host!!.type)
        assertEquals(-1, uriRef5.port)
        assertEquals("", uriRef5.path)
        assertEquals(null, uriRef5.query)
        assertEquals(null, uriRef5.fragment)

        val uriRef6 = URIReferenceParser().parse("http://%6A%6F%68%6E@example.com", Charsets.UTF8)

        assertEquals("http://%6A%6F%68%6E@example.com", uriRef6.toString())
        assertEquals(false, uriRef6.isRelativeReference)
        assertEquals("http", uriRef6.scheme)
        assertEquals(true, uriRef6.hasAuthority())
        assertEquals("%6A%6F%68%6E@example.com", uriRef6.authority.toString())
        assertEquals("%6A%6F%68%6E", uriRef6.userinfo)
        assertEquals("example.com", uriRef6.host.toString())
        assertEquals("example.com", uriRef6.host!!.value)
        assertEquals(HostType.REGNAME, uriRef6.host!!.type)
        assertEquals(-1, uriRef6.port)
        assertEquals("", uriRef6.path)
        assertEquals(null, uriRef6.query)
        assertEquals(null, uriRef6.fragment)

        val uriRef7 = URIReferenceParser().parse("http://101.102.103.104", Charsets.UTF8)

        assertEquals("http://101.102.103.104", uriRef7.toString())
        assertEquals(false, uriRef7.isRelativeReference)
        assertEquals("http", uriRef7.scheme)
        assertEquals(true, uriRef7.hasAuthority())
        assertEquals("101.102.103.104", uriRef7.authority.toString())
        assertEquals(null, uriRef7.userinfo)
        assertEquals("101.102.103.104", uriRef7.host.toString())
        assertEquals("101.102.103.104", uriRef7.host!!.value)
        assertEquals(HostType.IPV4, uriRef7.host!!.type)
        assertEquals(-1, uriRef7.port)
        assertEquals("", uriRef7.path)
        assertEquals(null, uriRef7.query)
        assertEquals(null, uriRef7.fragment)

        val uriRef8 =
            URIReferenceParser().parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", Charsets.UTF8)

        assertEquals("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.toString())
        assertEquals(false, uriRef8.isRelativeReference)
        assertEquals("http", uriRef8.scheme)
        assertEquals(true, uriRef8.hasAuthority())
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.authority.toString())
        assertEquals(null, uriRef8.authority!!.userinfo)
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.host.toString())
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.host!!.value)
        assertEquals(HostType.IPV6, uriRef8.host!!.type)
        assertEquals(-1, uriRef8.port)
        assertEquals("", uriRef8.path)
        assertEquals(null, uriRef8.query)
        assertEquals(null, uriRef8.fragment)

        val uriRef9 = URIReferenceParser().parse("http://[2001:db8:0:1:1:1:1:1]", Charsets.UTF8)

        assertEquals("http://[2001:db8:0:1:1:1:1:1]", uriRef9.toString())
        assertEquals(false, uriRef9.isRelativeReference)
        assertEquals("http", uriRef9.scheme)
        assertEquals(true, uriRef9.hasAuthority())
        assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef9.authority.toString())
        assertEquals(null, uriRef9.userinfo)
        assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef9.host.toString())
        assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef9.host!!.value)
        assertEquals(HostType.IPV6, uriRef9.host!!.type)
        assertEquals(-1, uriRef9.port)
        assertEquals("", uriRef9.path)
        assertEquals(null, uriRef9.query)
        assertEquals(null, uriRef9.fragment)

        val uriRef10 = URIReferenceParser().parse("http://[2001:0:9d38:6abd:0:0:0:42]", Charsets.UTF8)

        assertEquals("http://[2001:0:9d38:6abd:0:0:0:42]", uriRef10.toString())
        assertEquals(false, uriRef10.isRelativeReference)
        assertEquals("http", uriRef10.scheme)
        assertEquals(true, uriRef10.hasAuthority())
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef10.authority.toString())
        assertEquals(null, uriRef10.userinfo)
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef10.host.toString())
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef10.host!!.value)
        assertEquals(HostType.IPV6, uriRef10.host!!.type)
        assertEquals(-1, uriRef10.port)
        assertEquals("", uriRef10.path)
        assertEquals(null, uriRef10.query)
        assertEquals(null, uriRef10.fragment)

        val uriRef11 = URIReferenceParser().parse("http://[fe80::1]", Charsets.UTF8)

        assertEquals("http://[fe80::1]", uriRef11.toString())
        assertEquals(false, uriRef11.isRelativeReference)
        assertEquals("http", uriRef11.scheme)
        assertEquals(true, uriRef11.hasAuthority())
        assertEquals("[fe80::1]", uriRef11.authority.toString())
        assertEquals(null, uriRef11.userinfo)
        assertEquals("[fe80::1]", uriRef11.host.toString())
        assertEquals("[fe80::1]", uriRef11.host!!.value)
        assertEquals(HostType.IPV6, uriRef11.host!!.type)
        assertEquals(-1, uriRef11.port)
        assertEquals("", uriRef11.path)
        assertEquals(null, uriRef11.query)
        assertEquals(null, uriRef11.fragment)

        val uriRef12 = URIReferenceParser().parse("http://[2001:0:3238:DFE1:63::FEFB]", Charsets.UTF8)

        assertEquals("http://[2001:0:3238:DFE1:63::FEFB]", uriRef12.toString())
        assertEquals(false, uriRef12.isRelativeReference)
        assertEquals("http", uriRef12.scheme)
        assertEquals(true, uriRef12.hasAuthority())
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef12.authority.toString())
        assertEquals(null, uriRef12.userinfo)
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef12.host.toString())
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef12.host!!.value)
        assertEquals(HostType.IPV6, uriRef12.host!!.type)
        assertEquals(-1, uriRef12.port)
        assertEquals("", uriRef12.path)
        assertEquals(null, uriRef12.query)
        assertEquals(null, uriRef12.fragment)

        val uriRef13 = URIReferenceParser().parse("http://[v1.fe80::a+en1]", Charsets.UTF8)

        assertEquals("http://[v1.fe80::a+en1]", uriRef13.toString())
        assertEquals(false, uriRef13.isRelativeReference)
        assertEquals("http", uriRef13.scheme)
        assertEquals(true, uriRef13.hasAuthority())
        assertEquals("[v1.fe80::a+en1]", uriRef13.authority.toString())
        assertEquals(null, uriRef13.userinfo)
        assertEquals("[v1.fe80::a+en1]", uriRef13.host.toString())
        assertEquals("[v1.fe80::a+en1]", uriRef13.host!!.value)
        assertEquals(HostType.IPVFUTURE, uriRef13.host!!.type)
        assertEquals(-1, uriRef13.port)
        assertEquals("", uriRef13.path)
        assertEquals(null, uriRef13.query)
        assertEquals(null, uriRef13.fragment)

        val uriRef14 = URIReferenceParser().parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", Charsets.UTF8)

        assertEquals("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.toString())
        assertEquals(false, uriRef14.isRelativeReference)
        assertEquals("http", uriRef14.scheme)
        assertEquals(true, uriRef14.hasAuthority())
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.authority.toString())
        assertEquals(null, uriRef14.userinfo)
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.host.toString())
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.host!!.value)
        assertEquals(HostType.REGNAME, uriRef14.host!!.type)
        assertEquals(-1, uriRef14.port)
        assertEquals("", uriRef14.path)
        assertEquals(null, uriRef14.query)
        assertEquals(null, uriRef14.fragment)

        val uriRef15 = URIReferenceParser().parse("http://", Charsets.UTF8)

        assertEquals(false, uriRef15.isRelativeReference)
        assertEquals("http", uriRef15.scheme)
        assertEquals(true, uriRef15.hasAuthority())
        assertEquals("", uriRef15.authority.toString())
        assertEquals(null, uriRef15.userinfo)
        assertEquals("", uriRef15.host.toString())
        assertEquals("", uriRef15.host!!.value)
        assertEquals(HostType.REGNAME, uriRef15.host!!.type)
        assertEquals(-1, uriRef15.port)
        assertEquals("", uriRef15.path)
        assertEquals(null, uriRef15.query)
        assertEquals(null, uriRef15.fragment)

        val uriRef16 = URIReferenceParser().parse("http:///a", Charsets.UTF8)

        assertEquals(false, uriRef16.isRelativeReference)
        assertEquals("http", uriRef16.scheme)
        assertEquals(true, uriRef16.hasAuthority())
        assertEquals("", uriRef16.authority.toString())
        assertEquals(null, uriRef16.userinfo)
        assertEquals("", uriRef16.host.toString())
        assertEquals("", uriRef16.host!!.value)
        assertEquals(HostType.REGNAME, uriRef16.host!!.type)
        assertEquals(-1, uriRef16.port)
        assertEquals("/a", uriRef16.path)
        assertEquals(null, uriRef16.query)
        assertEquals(null, uriRef16.fragment)

        val uriRef17 = URIReferenceParser().parse("http://example.com:80", Charsets.UTF8)

        assertEquals(false, uriRef17.isRelativeReference)
        assertEquals("http", uriRef17.scheme)
        assertEquals(true, uriRef17.hasAuthority())
        assertEquals("example.com:80", uriRef17.authority.toString())
        assertEquals(null, uriRef17.userinfo)
        assertEquals("example.com", uriRef17.host.toString())
        assertEquals("example.com", uriRef17.host!!.value)
        assertEquals(HostType.REGNAME, uriRef17.host!!.type)
        assertEquals(80, uriRef17.port)
        assertEquals("", uriRef17.path)
        assertEquals(null, uriRef17.query)
        assertEquals(null, uriRef17.fragment)

        val uriRef18 = URIReferenceParser().parse("http://example.com:", Charsets.UTF8)

        assertEquals(false, uriRef18.isRelativeReference)
        assertEquals("http", uriRef18.scheme)
        assertEquals(true, uriRef18.hasAuthority())
        assertEquals("example.com", uriRef18.authority.toString())
        assertEquals(null, uriRef18.userinfo)
        assertEquals("example.com", uriRef18.host.toString())
        assertEquals("example.com", uriRef18.host!!.value)
        assertEquals(HostType.REGNAME, uriRef18.host!!.type)
        assertEquals(-1, uriRef18.port)
        assertEquals("", uriRef18.path)
        assertEquals(null, uriRef18.query)
        assertEquals(null, uriRef18.fragment)

        val uriRef19 = URIReferenceParser().parse("http://example.com:001", Charsets.UTF8)

        assertEquals(false, uriRef19.isRelativeReference)
        assertEquals("http", uriRef19.scheme)
        assertEquals(true, uriRef19.hasAuthority())
        assertEquals("example.com:1", uriRef19.authority.toString())
        assertEquals(null, uriRef19.authority!!.userinfo)
        assertEquals("example.com", uriRef19.host.toString())
        assertEquals("example.com", uriRef19.host!!.value)
        assertEquals(HostType.REGNAME, uriRef19.host!!.type)
        assertEquals(1, uriRef19.port)
        assertEquals("", uriRef19.path)
        assertEquals(null, uriRef19.query)
        assertEquals(null, uriRef19.fragment)

        val uriRef20 = URIReferenceParser().parse("http://example.com/a/b/c", Charsets.UTF8)

        assertEquals("http://example.com/a/b/c", uriRef20.toString())
        assertEquals(false, uriRef20.isRelativeReference)
        assertEquals("http", uriRef20.scheme)
        assertEquals(true, uriRef20.hasAuthority())
        assertEquals("example.com", uriRef20.authority.toString())
        assertEquals(null, uriRef20.authority!!.userinfo)
        assertEquals("example.com", uriRef20.host.toString())
        assertEquals("example.com", uriRef20.host!!.value)
        assertEquals(HostType.REGNAME, uriRef20.host!!.type)
        assertEquals(-1, uriRef20.port)
        assertEquals("/a/b/c", uriRef20.path)
        assertEquals(null, uriRef20.query)
        assertEquals(null, uriRef20.fragment)

        val uriRef21 = URIReferenceParser().parse("http://example.com/%61/%62/%63", Charsets.UTF8)

        assertEquals("http://example.com/%61/%62/%63", uriRef21.toString())
        assertEquals(false, uriRef21.isRelativeReference)
        assertEquals("http", uriRef21.scheme)
        assertEquals(true, uriRef21.hasAuthority())
        assertEquals("example.com", uriRef21.authority.toString())
        assertEquals(null, uriRef21.userinfo)
        assertEquals("example.com", uriRef21.host.toString())
        assertEquals("example.com", uriRef21.host!!.value)
        assertEquals(HostType.REGNAME, uriRef21.host!!.type)
        assertEquals(-1, uriRef21.port)
        assertEquals("/%61/%62/%63", uriRef21.path)
        assertEquals(null, uriRef21.query)
        assertEquals(null, uriRef21.fragment)

        val uriRef22 = URIReferenceParser().parse("http:/a", Charsets.UTF8)

        assertEquals(false, uriRef22.isRelativeReference)
        assertEquals("http", uriRef22.scheme)
        assertEquals(false, uriRef22.hasAuthority())
        assertEquals(null, uriRef22.authority)
        assertEquals(null, uriRef22.userinfo)
        assertEquals(null, uriRef22.host)
        assertEquals(-1, uriRef22.port)
        assertEquals("/a", uriRef22.path)
        assertEquals(null, uriRef22.query)
        assertEquals(null, uriRef22.fragment)

        val uriRef23 = URIReferenceParser().parse("http:a", Charsets.UTF8)

        assertEquals(false, uriRef23.isRelativeReference)
        assertEquals("http", uriRef23.scheme)
        assertEquals(false, uriRef23.hasAuthority())
        assertEquals(null, uriRef23.authority)
        assertEquals(null, uriRef23.userinfo)
        assertEquals(null, uriRef23.host)
        assertEquals(-1, uriRef23.port)
        assertEquals("a", uriRef23.path)
        assertEquals(null, uriRef23.query)
        assertEquals(null, uriRef23.fragment)

        val uriRef24 = URIReferenceParser().parse("//", Charsets.UTF8)

        assertEquals(true, uriRef24.isRelativeReference)
        assertEquals(null, uriRef24.scheme)
        assertEquals(true, uriRef24.hasAuthority())
        assertEquals("", uriRef24.authority.toString())
        assertEquals(null, uriRef24.userinfo)
        assertEquals("", uriRef24.host.toString())
        assertEquals("", uriRef24.host!!.value)
        assertEquals(HostType.REGNAME, uriRef24.host!!.type)
        assertEquals(-1, uriRef24.port)
        assertEquals("", uriRef24.path)
        assertEquals(null, uriRef24.query)
        assertEquals(null, uriRef24.fragment)

        val uriRef25 = URIReferenceParser().parse("http://example.com?q", Charsets.UTF8)

        assertEquals(false, uriRef25.isRelativeReference)
        assertEquals("http", uriRef25.scheme)
        assertEquals(true, uriRef25.hasAuthority())
        assertEquals("example.com", uriRef25.authority.toString())
        assertEquals(null, uriRef25.userinfo)
        assertEquals("example.com", uriRef25.host.toString())
        assertEquals("example.com", uriRef25.host!!.value)
        assertEquals(HostType.REGNAME, uriRef25.host!!.type)
        assertEquals(-1, uriRef25.port)
        assertEquals("", uriRef25.path)
        assertEquals("q", uriRef25.query)
        assertEquals(null, uriRef25.fragment)

        val uriRef26 = URIReferenceParser().parse("http://example.com?", Charsets.UTF8)

        assertEquals(false, uriRef26.isRelativeReference)
        assertEquals("http", uriRef26.scheme)
        assertEquals(true, uriRef26.hasAuthority())
        assertEquals("example.com", uriRef26.authority.toString())
        assertEquals(null, uriRef26.userinfo)
        assertEquals("example.com", uriRef26.host.toString())
        assertEquals("example.com", uriRef26.host!!.value)
        assertEquals(HostType.REGNAME, uriRef26.host!!.type)
        assertEquals(-1, uriRef26.port)
        assertEquals("", uriRef26.path)
        assertEquals("", uriRef26.query)
        assertEquals(null, uriRef26.fragment)

        val uriRef27 = URIReferenceParser().parse("http://example.com#f", Charsets.UTF8)

        assertEquals(false, uriRef27.isRelativeReference)
        assertEquals("http", uriRef27.scheme)
        assertEquals(true, uriRef27.hasAuthority())
        assertEquals("example.com", uriRef27.authority.toString())
        assertEquals(null, uriRef27.userinfo)
        assertEquals("example.com", uriRef27.host.toString())
        assertEquals("example.com", uriRef27.host!!.value)
        assertEquals(HostType.REGNAME, uriRef27.host!!.type)
        assertEquals(-1, uriRef27.port)
        assertEquals("", uriRef27.path)
        assertEquals(null, uriRef27.query)
        assertEquals("f", uriRef27.fragment)

        val uriRef28 = URIReferenceParser().parse("http://example.com#", Charsets.UTF8)

        assertEquals(false, uriRef28.isRelativeReference)
        assertEquals("http", uriRef28.scheme)
        assertEquals(true, uriRef28.hasAuthority())
        assertEquals("example.com", uriRef28.authority.toString())
        assertEquals(null, uriRef28.userinfo)
        assertEquals("example.com", uriRef28.host.toString())
        assertEquals("example.com", uriRef28.host!!.value)
        assertEquals(HostType.REGNAME, uriRef28.host!!.type)
        assertEquals(-1, uriRef28.port)
        assertEquals("", uriRef28.path)
        assertEquals(null, uriRef28.query)
        assertEquals("", uriRef28.fragment)

        val uriRef29 = URIReferenceParser().parse("", Charsets.UTF8)

        assertEquals(true, uriRef29.isRelativeReference)
        assertEquals(null, uriRef29.scheme)
        assertEquals(false, uriRef29.hasAuthority())
        assertEquals(null, uriRef29.authority)
        assertEquals(null, uriRef29.userinfo)
        assertEquals(null, uriRef29.host)
        assertEquals(-1, uriRef29.port)
        assertEquals("", uriRef29.path)
        assertEquals(null, uriRef29.query)
        assertEquals(null, uriRef29.fragment)

        assertThrowsIAE<Throwable>(
            "The path value is invalid.",
            { URIReferenceParser().parse("1invalid://example.com", Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The host value \"v@w\" has an invalid character \"@\" at the index 1.",
            { URIReferenceParser().parse("http://u@v@w", Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The port value \"1:2:3\" has an invalid character \":\" at the index 1.",
            { URIReferenceParser().parse("http://example.com:1:2:3", Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The query value \"[invalid_query]\" has an invalid character \"[\" at the index 0.",
            { URIReferenceParser().parse("http://example.com?[invalid_query]", Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The fragment value \"[invalid_fragment]\" has an invalid character \"[\" at the index 0.",
            { URIReferenceParser().parse("http://example.com#[invalid_fragment]", Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The port value \"b\" has an invalid character \"b\" at the index 0.",
            { URIReferenceParser().parse("//a:b", Charsets.UTF8) })

        assertThrowsIAE<Throwable>(
            "The port value \":\" has an invalid character \":\" at the index 0.",
            { URIReferenceParser().parse("//::", Charsets.UTF8) })
    }
}
