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

import org.czeal.urireference.TestUtils.assertThrowsIAE
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets


class URIReferenceParserTest {
    @Test
    fun test_parse() {
        val uriRef1 = URIReferenceParser().parse("http://example.com", StandardCharsets.UTF_8)
        Assertions.assertEquals("http://example.com", uriRef1.toString())
        Assertions.assertEquals(false, uriRef1.isRelativeReference)
        Assertions.assertEquals("http", uriRef1.scheme)
        Assertions.assertEquals(true, uriRef1.hasAuthority())
        Assertions.assertEquals("example.com", uriRef1.authority.toString())
        Assertions.assertEquals(null, uriRef1.userinfo)
        Assertions.assertEquals("example.com", uriRef1.host.toString())
        Assertions.assertEquals("example.com", uriRef1.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef1.host!!.type)
        Assertions.assertEquals(-1, uriRef1.port)
        Assertions.assertEquals("", uriRef1.path)
        Assertions.assertEquals(null, uriRef1.query)
        Assertions.assertEquals(null, uriRef1.fragment)

        val uriRef2 = URIReferenceParser().parse("hTTp://example.com", StandardCharsets.UTF_8)

        Assertions.assertEquals("hTTp://example.com", uriRef2.toString())
        Assertions.assertEquals(false, uriRef2.isRelativeReference)
        Assertions.assertEquals("hTTp", uriRef2.scheme)
        Assertions.assertEquals(true, uriRef2.hasAuthority())
        Assertions.assertEquals("example.com", uriRef2.authority.toString())
        Assertions.assertEquals(null, uriRef2.userinfo)
        Assertions.assertEquals("example.com", uriRef2.host.toString())
        Assertions.assertEquals("example.com", uriRef2.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef2.host!!.type)
        Assertions.assertEquals(-1, uriRef2.port)
        Assertions.assertEquals("", uriRef2.path)
        Assertions.assertEquals(null, uriRef2.query)
        Assertions.assertEquals(null, uriRef2.fragment)

        val uriRef3 = URIReferenceParser().parse("//example.com", StandardCharsets.UTF_8)

        Assertions.assertEquals("//example.com", uriRef3.toString())
        Assertions.assertEquals(true, uriRef3.isRelativeReference)
        Assertions.assertEquals(null, uriRef3.scheme)
        Assertions.assertEquals(true, uriRef3.hasAuthority())
        Assertions.assertEquals("example.com", uriRef3.authority.toString())
        Assertions.assertEquals(null, uriRef3.userinfo)
        Assertions.assertEquals("example.com", uriRef3.host.toString())
        Assertions.assertEquals("example.com", uriRef3.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef3.host!!.type)
        Assertions.assertEquals(-1, uriRef3.port)
        Assertions.assertEquals("", uriRef3.path)
        Assertions.assertEquals(null, uriRef3.query)
        Assertions.assertEquals(null, uriRef3.fragment)

        val uriRef4 = URIReferenceParser().parse("http:", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef4.isRelativeReference)
        Assertions.assertEquals("http", uriRef4.scheme)
        Assertions.assertEquals(false, uriRef4.hasAuthority())
        Assertions.assertEquals(null, uriRef4.authority)
        Assertions.assertEquals(null, uriRef4.userinfo)
        Assertions.assertEquals(null, uriRef4.host)
        Assertions.assertEquals(-1, uriRef4.port)
        Assertions.assertEquals("", uriRef4.path)
        Assertions.assertEquals(null, uriRef4.query)
        Assertions.assertEquals(null, uriRef4.fragment)

        val uriRef5 = URIReferenceParser().parse("http://john@example.com", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://john@example.com", uriRef5.toString())
        Assertions.assertEquals(false, uriRef5.isRelativeReference)
        Assertions.assertEquals("http", uriRef5.scheme)
        Assertions.assertEquals(true, uriRef5.hasAuthority())
        Assertions.assertEquals("john@example.com", uriRef5.authority.toString())
        Assertions.assertEquals("john", uriRef5.userinfo)
        Assertions.assertEquals("example.com", uriRef5.host.toString())
        Assertions.assertEquals("example.com", uriRef5.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef5.host!!.type)
        Assertions.assertEquals(-1, uriRef5.port)
        Assertions.assertEquals("", uriRef5.path)
        Assertions.assertEquals(null, uriRef5.query)
        Assertions.assertEquals(null, uriRef5.fragment)

        val uriRef6 = URIReferenceParser().parse("http://%6A%6F%68%6E@example.com", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://%6A%6F%68%6E@example.com", uriRef6.toString())
        Assertions.assertEquals(false, uriRef6.isRelativeReference)
        Assertions.assertEquals("http", uriRef6.scheme)
        Assertions.assertEquals(true, uriRef6.hasAuthority())
        Assertions.assertEquals("%6A%6F%68%6E@example.com", uriRef6.authority.toString())
        Assertions.assertEquals("%6A%6F%68%6E", uriRef6.userinfo)
        Assertions.assertEquals("example.com", uriRef6.host.toString())
        Assertions.assertEquals("example.com", uriRef6.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef6.host!!.type)
        Assertions.assertEquals(-1, uriRef6.port)
        Assertions.assertEquals("", uriRef6.path)
        Assertions.assertEquals(null, uriRef6.query)
        Assertions.assertEquals(null, uriRef6.fragment)

        val uriRef7 = URIReferenceParser().parse("http://101.102.103.104", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://101.102.103.104", uriRef7.toString())
        Assertions.assertEquals(false, uriRef7.isRelativeReference)
        Assertions.assertEquals("http", uriRef7.scheme)
        Assertions.assertEquals(true, uriRef7.hasAuthority())
        Assertions.assertEquals("101.102.103.104", uriRef7.authority.toString())
        Assertions.assertEquals(null, uriRef7.userinfo)
        Assertions.assertEquals("101.102.103.104", uriRef7.host.toString())
        Assertions.assertEquals("101.102.103.104", uriRef7.host!!.value)
        Assertions.assertEquals(HostType.IPV4, uriRef7.host!!.type)
        Assertions.assertEquals(-1, uriRef7.port)
        Assertions.assertEquals("", uriRef7.path)
        Assertions.assertEquals(null, uriRef7.query)
        Assertions.assertEquals(null, uriRef7.fragment)

        val uriRef8 =
            URIReferenceParser().parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.toString())
        Assertions.assertEquals(false, uriRef8.isRelativeReference)
        Assertions.assertEquals("http", uriRef8.scheme)
        Assertions.assertEquals(true, uriRef8.hasAuthority())
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.authority.toString())
        Assertions.assertEquals(null, uriRef8.authority!!.userinfo)
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.host.toString())
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.host!!.value)
        Assertions.assertEquals(HostType.IPV6, uriRef8.host!!.type)
        Assertions.assertEquals(-1, uriRef8.port)
        Assertions.assertEquals("", uriRef8.path)
        Assertions.assertEquals(null, uriRef8.query)
        Assertions.assertEquals(null, uriRef8.fragment)

        val uriRef9 = URIReferenceParser().parse("http://[2001:db8:0:1:1:1:1:1]", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://[2001:db8:0:1:1:1:1:1]", uriRef9.toString())
        Assertions.assertEquals(false, uriRef9.isRelativeReference)
        Assertions.assertEquals("http", uriRef9.scheme)
        Assertions.assertEquals(true, uriRef9.hasAuthority())
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef9.authority.toString())
        Assertions.assertEquals(null, uriRef9.userinfo)
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef9.host.toString())
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef9.host!!.value)
        Assertions.assertEquals(HostType.IPV6, uriRef9.host!!.type)
        Assertions.assertEquals(-1, uriRef9.port)
        Assertions.assertEquals("", uriRef9.path)
        Assertions.assertEquals(null, uriRef9.query)
        Assertions.assertEquals(null, uriRef9.fragment)

        val uriRef10 = URIReferenceParser().parse("http://[2001:0:9d38:6abd:0:0:0:42]", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://[2001:0:9d38:6abd:0:0:0:42]", uriRef10.toString())
        Assertions.assertEquals(false, uriRef10.isRelativeReference)
        Assertions.assertEquals("http", uriRef10.scheme)
        Assertions.assertEquals(true, uriRef10.hasAuthority())
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef10.authority.toString())
        Assertions.assertEquals(null, uriRef10.userinfo)
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef10.host.toString())
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef10.host!!.value)
        Assertions.assertEquals(HostType.IPV6, uriRef10.host!!.type)
        Assertions.assertEquals(-1, uriRef10.port)
        Assertions.assertEquals("", uriRef10.path)
        Assertions.assertEquals(null, uriRef10.query)
        Assertions.assertEquals(null, uriRef10.fragment)

        val uriRef11 = URIReferenceParser().parse("http://[fe80::1]", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://[fe80::1]", uriRef11.toString())
        Assertions.assertEquals(false, uriRef11.isRelativeReference)
        Assertions.assertEquals("http", uriRef11.scheme)
        Assertions.assertEquals(true, uriRef11.hasAuthority())
        Assertions.assertEquals("[fe80::1]", uriRef11.authority.toString())
        Assertions.assertEquals(null, uriRef11.userinfo)
        Assertions.assertEquals("[fe80::1]", uriRef11.host.toString())
        Assertions.assertEquals("[fe80::1]", uriRef11.host!!.value)
        Assertions.assertEquals(HostType.IPV6, uriRef11.host!!.type)
        Assertions.assertEquals(-1, uriRef11.port)
        Assertions.assertEquals("", uriRef11.path)
        Assertions.assertEquals(null, uriRef11.query)
        Assertions.assertEquals(null, uriRef11.fragment)

        val uriRef12 = URIReferenceParser().parse("http://[2001:0:3238:DFE1:63::FEFB]", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://[2001:0:3238:DFE1:63::FEFB]", uriRef12.toString())
        Assertions.assertEquals(false, uriRef12.isRelativeReference)
        Assertions.assertEquals("http", uriRef12.scheme)
        Assertions.assertEquals(true, uriRef12.hasAuthority())
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef12.authority.toString())
        Assertions.assertEquals(null, uriRef12.userinfo)
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef12.host.toString())
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef12.host!!.value)
        Assertions.assertEquals(HostType.IPV6, uriRef12.host!!.type)
        Assertions.assertEquals(-1, uriRef12.port)
        Assertions.assertEquals("", uriRef12.path)
        Assertions.assertEquals(null, uriRef12.query)
        Assertions.assertEquals(null, uriRef12.fragment)

        val uriRef13 = URIReferenceParser().parse("http://[v1.fe80::a+en1]", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://[v1.fe80::a+en1]", uriRef13.toString())
        Assertions.assertEquals(false, uriRef13.isRelativeReference)
        Assertions.assertEquals("http", uriRef13.scheme)
        Assertions.assertEquals(true, uriRef13.hasAuthority())
        Assertions.assertEquals("[v1.fe80::a+en1]", uriRef13.authority.toString())
        Assertions.assertEquals(null, uriRef13.userinfo)
        Assertions.assertEquals("[v1.fe80::a+en1]", uriRef13.host.toString())
        Assertions.assertEquals("[v1.fe80::a+en1]", uriRef13.host!!.value)
        Assertions.assertEquals(HostType.IPVFUTURE, uriRef13.host!!.type)
        Assertions.assertEquals(-1, uriRef13.port)
        Assertions.assertEquals("", uriRef13.path)
        Assertions.assertEquals(null, uriRef13.query)
        Assertions.assertEquals(null, uriRef13.fragment)

        val uriRef14 = URIReferenceParser().parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.toString())
        Assertions.assertEquals(false, uriRef14.isRelativeReference)
        Assertions.assertEquals("http", uriRef14.scheme)
        Assertions.assertEquals(true, uriRef14.hasAuthority())
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.authority.toString())
        Assertions.assertEquals(null, uriRef14.userinfo)
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.host.toString())
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef14.host!!.type)
        Assertions.assertEquals(-1, uriRef14.port)
        Assertions.assertEquals("", uriRef14.path)
        Assertions.assertEquals(null, uriRef14.query)
        Assertions.assertEquals(null, uriRef14.fragment)

        val uriRef15 = URIReferenceParser().parse("http://", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef15.isRelativeReference)
        Assertions.assertEquals("http", uriRef15.scheme)
        Assertions.assertEquals(true, uriRef15.hasAuthority())
        Assertions.assertEquals("", uriRef15.authority.toString())
        Assertions.assertEquals(null, uriRef15.userinfo)
        Assertions.assertEquals("", uriRef15.host.toString())
        Assertions.assertEquals("", uriRef15.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef15.host!!.type)
        Assertions.assertEquals(-1, uriRef15.port)
        Assertions.assertEquals("", uriRef15.path)
        Assertions.assertEquals(null, uriRef15.query)
        Assertions.assertEquals(null, uriRef15.fragment)

        val uriRef16 = URIReferenceParser().parse("http:///a", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef16.isRelativeReference)
        Assertions.assertEquals("http", uriRef16.scheme)
        Assertions.assertEquals(true, uriRef16.hasAuthority())
        Assertions.assertEquals("", uriRef16.authority.toString())
        Assertions.assertEquals(null, uriRef16.userinfo)
        Assertions.assertEquals("", uriRef16.host.toString())
        Assertions.assertEquals("", uriRef16.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef16.host!!.type)
        Assertions.assertEquals(-1, uriRef16.port)
        Assertions.assertEquals("/a", uriRef16.path)
        Assertions.assertEquals(null, uriRef16.query)
        Assertions.assertEquals(null, uriRef16.fragment)

        val uriRef17 = URIReferenceParser().parse("http://example.com:80", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef17.isRelativeReference)
        Assertions.assertEquals("http", uriRef17.scheme)
        Assertions.assertEquals(true, uriRef17.hasAuthority())
        Assertions.assertEquals("example.com:80", uriRef17.authority.toString())
        Assertions.assertEquals(null, uriRef17.userinfo)
        Assertions.assertEquals("example.com", uriRef17.host.toString())
        Assertions.assertEquals("example.com", uriRef17.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef17.host!!.type)
        Assertions.assertEquals(80, uriRef17.port)
        Assertions.assertEquals("", uriRef17.path)
        Assertions.assertEquals(null, uriRef17.query)
        Assertions.assertEquals(null, uriRef17.fragment)

        val uriRef18 = URIReferenceParser().parse("http://example.com:", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef18.isRelativeReference)
        Assertions.assertEquals("http", uriRef18.scheme)
        Assertions.assertEquals(true, uriRef18.hasAuthority())
        Assertions.assertEquals("example.com", uriRef18.authority.toString())
        Assertions.assertEquals(null, uriRef18.userinfo)
        Assertions.assertEquals("example.com", uriRef18.host.toString())
        Assertions.assertEquals("example.com", uriRef18.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef18.host!!.type)
        Assertions.assertEquals(-1, uriRef18.port)
        Assertions.assertEquals("", uriRef18.path)
        Assertions.assertEquals(null, uriRef18.query)
        Assertions.assertEquals(null, uriRef18.fragment)

        val uriRef19 = URIReferenceParser().parse("http://example.com:001", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef19.isRelativeReference)
        Assertions.assertEquals("http", uriRef19.scheme)
        Assertions.assertEquals(true, uriRef19.hasAuthority())
        Assertions.assertEquals("example.com:1", uriRef19.authority.toString())
        Assertions.assertEquals(null, uriRef19.authority!!.userinfo)
        Assertions.assertEquals("example.com", uriRef19.host.toString())
        Assertions.assertEquals("example.com", uriRef19.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef19.host!!.type)
        Assertions.assertEquals(1, uriRef19.port)
        Assertions.assertEquals("", uriRef19.path)
        Assertions.assertEquals(null, uriRef19.query)
        Assertions.assertEquals(null, uriRef19.fragment)

        val uriRef20 = URIReferenceParser().parse("http://example.com/a/b/c", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://example.com/a/b/c", uriRef20.toString())
        Assertions.assertEquals(false, uriRef20.isRelativeReference)
        Assertions.assertEquals("http", uriRef20.scheme)
        Assertions.assertEquals(true, uriRef20.hasAuthority())
        Assertions.assertEquals("example.com", uriRef20.authority.toString())
        Assertions.assertEquals(null, uriRef20.authority!!.userinfo)
        Assertions.assertEquals("example.com", uriRef20.host.toString())
        Assertions.assertEquals("example.com", uriRef20.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef20.host!!.type)
        Assertions.assertEquals(-1, uriRef20.port)
        Assertions.assertEquals("/a/b/c", uriRef20.path)
        Assertions.assertEquals(null, uriRef20.query)
        Assertions.assertEquals(null, uriRef20.fragment)

        val uriRef21 = URIReferenceParser().parse("http://example.com/%61/%62/%63", StandardCharsets.UTF_8)

        Assertions.assertEquals("http://example.com/%61/%62/%63", uriRef21.toString())
        Assertions.assertEquals(false, uriRef21.isRelativeReference)
        Assertions.assertEquals("http", uriRef21.scheme)
        Assertions.assertEquals(true, uriRef21.hasAuthority())
        Assertions.assertEquals("example.com", uriRef21.authority.toString())
        Assertions.assertEquals(null, uriRef21.userinfo)
        Assertions.assertEquals("example.com", uriRef21.host.toString())
        Assertions.assertEquals("example.com", uriRef21.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef21.host!!.type)
        Assertions.assertEquals(-1, uriRef21.port)
        Assertions.assertEquals("/%61/%62/%63", uriRef21.path)
        Assertions.assertEquals(null, uriRef21.query)
        Assertions.assertEquals(null, uriRef21.fragment)

        val uriRef22 = URIReferenceParser().parse("http:/a", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef22.isRelativeReference)
        Assertions.assertEquals("http", uriRef22.scheme)
        Assertions.assertEquals(false, uriRef22.hasAuthority())
        Assertions.assertEquals(null, uriRef22.authority)
        Assertions.assertEquals(null, uriRef22.userinfo)
        Assertions.assertEquals(null, uriRef22.host)
        Assertions.assertEquals(-1, uriRef22.port)
        Assertions.assertEquals("/a", uriRef22.path)
        Assertions.assertEquals(null, uriRef22.query)
        Assertions.assertEquals(null, uriRef22.fragment)

        val uriRef23 = URIReferenceParser().parse("http:a", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef23.isRelativeReference)
        Assertions.assertEquals("http", uriRef23.scheme)
        Assertions.assertEquals(false, uriRef23.hasAuthority())
        Assertions.assertEquals(null, uriRef23.authority)
        Assertions.assertEquals(null, uriRef23.userinfo)
        Assertions.assertEquals(null, uriRef23.host)
        Assertions.assertEquals(-1, uriRef23.port)
        Assertions.assertEquals("a", uriRef23.path)
        Assertions.assertEquals(null, uriRef23.query)
        Assertions.assertEquals(null, uriRef23.fragment)

        val uriRef24 = URIReferenceParser().parse("//", StandardCharsets.UTF_8)

        Assertions.assertEquals(true, uriRef24.isRelativeReference)
        Assertions.assertEquals(null, uriRef24.scheme)
        Assertions.assertEquals(true, uriRef24.hasAuthority())
        Assertions.assertEquals("", uriRef24.authority.toString())
        Assertions.assertEquals(null, uriRef24.userinfo)
        Assertions.assertEquals("", uriRef24.host.toString())
        Assertions.assertEquals("", uriRef24.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef24.host!!.type)
        Assertions.assertEquals(-1, uriRef24.port)
        Assertions.assertEquals("", uriRef24.path)
        Assertions.assertEquals(null, uriRef24.query)
        Assertions.assertEquals(null, uriRef24.fragment)

        val uriRef25 = URIReferenceParser().parse("http://example.com?q", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef25.isRelativeReference)
        Assertions.assertEquals("http", uriRef25.scheme)
        Assertions.assertEquals(true, uriRef25.hasAuthority())
        Assertions.assertEquals("example.com", uriRef25.authority.toString())
        Assertions.assertEquals(null, uriRef25.userinfo)
        Assertions.assertEquals("example.com", uriRef25.host.toString())
        Assertions.assertEquals("example.com", uriRef25.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef25.host!!.type)
        Assertions.assertEquals(-1, uriRef25.port)
        Assertions.assertEquals("", uriRef25.path)
        Assertions.assertEquals("q", uriRef25.query)
        Assertions.assertEquals(null, uriRef25.fragment)

        val uriRef26 = URIReferenceParser().parse("http://example.com?", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef26.isRelativeReference)
        Assertions.assertEquals("http", uriRef26.scheme)
        Assertions.assertEquals(true, uriRef26.hasAuthority())
        Assertions.assertEquals("example.com", uriRef26.authority.toString())
        Assertions.assertEquals(null, uriRef26.userinfo)
        Assertions.assertEquals("example.com", uriRef26.host.toString())
        Assertions.assertEquals("example.com", uriRef26.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef26.host!!.type)
        Assertions.assertEquals(-1, uriRef26.port)
        Assertions.assertEquals("", uriRef26.path)
        Assertions.assertEquals("", uriRef26.query)
        Assertions.assertEquals(null, uriRef26.fragment)

        val uriRef27 = URIReferenceParser().parse("http://example.com#f", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef27.isRelativeReference)
        Assertions.assertEquals("http", uriRef27.scheme)
        Assertions.assertEquals(true, uriRef27.hasAuthority())
        Assertions.assertEquals("example.com", uriRef27.authority.toString())
        Assertions.assertEquals(null, uriRef27.userinfo)
        Assertions.assertEquals("example.com", uriRef27.host.toString())
        Assertions.assertEquals("example.com", uriRef27.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef27.host!!.type)
        Assertions.assertEquals(-1, uriRef27.port)
        Assertions.assertEquals("", uriRef27.path)
        Assertions.assertEquals(null, uriRef27.query)
        Assertions.assertEquals("f", uriRef27.fragment)

        val uriRef28 = URIReferenceParser().parse("http://example.com#", StandardCharsets.UTF_8)

        Assertions.assertEquals(false, uriRef28.isRelativeReference)
        Assertions.assertEquals("http", uriRef28.scheme)
        Assertions.assertEquals(true, uriRef28.hasAuthority())
        Assertions.assertEquals("example.com", uriRef28.authority.toString())
        Assertions.assertEquals(null, uriRef28.userinfo)
        Assertions.assertEquals("example.com", uriRef28.host.toString())
        Assertions.assertEquals("example.com", uriRef28.host!!.value)
        Assertions.assertEquals(HostType.REGNAME, uriRef28.host!!.type)
        Assertions.assertEquals(-1, uriRef28.port)
        Assertions.assertEquals("", uriRef28.path)
        Assertions.assertEquals(null, uriRef28.query)
        Assertions.assertEquals("", uriRef28.fragment)

        val uriRef29 = URIReferenceParser().parse("", StandardCharsets.UTF_8)

        Assertions.assertEquals(true, uriRef29.isRelativeReference)
        Assertions.assertEquals(null, uriRef29.scheme)
        Assertions.assertEquals(false, uriRef29.hasAuthority())
        Assertions.assertEquals(null, uriRef29.authority)
        Assertions.assertEquals(null, uriRef29.userinfo)
        Assertions.assertEquals(null, uriRef29.host)
        Assertions.assertEquals(-1, uriRef29.port)
        Assertions.assertEquals("", uriRef29.path)
        Assertions.assertEquals(null, uriRef29.query)
        Assertions.assertEquals(null, uriRef29.fragment)

        assertThrowsIAE<Throwable>(
            "The path value is invalid.",
            { URIReferenceParser().parse("1invalid://example.com", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The host value \"v@w\" has an invalid character \"@\" at the index 1.",
            { URIReferenceParser().parse("http://u@v@w", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The port value \"1:2:3\" has an invalid character \":\" at the index 1.",
            { URIReferenceParser().parse("http://example.com:1:2:3", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The query value \"[invalid_query]\" has an invalid character \"[\" at the index 0.",
            { URIReferenceParser().parse("http://example.com?[invalid_query]", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The fragment value \"[invalid_fragment]\" has an invalid character \"[\" at the index 0.",
            { URIReferenceParser().parse("http://example.com#[invalid_fragment]", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The port value \"b\" has an invalid character \"b\" at the index 0.",
            { URIReferenceParser().parse("//a:b", StandardCharsets.UTF_8) })

        assertThrowsIAE<Throwable>(
            "The port value \":\" has an invalid character \":\" at the index 0.",
            { URIReferenceParser().parse("//::", StandardCharsets.UTF_8) })
    }
}
