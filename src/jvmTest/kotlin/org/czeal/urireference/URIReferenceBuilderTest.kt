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

import org.czeal.urireference.TestUtils.assertThrowsNPE
import org.czeal.urireference.URIReference.Companion.parse
import org.czeal.urireference.URIReferenceBuilder.Companion.fromURIReference
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class URIReferenceBuilderTest {
    @Test
    fun test_fromURIReference_with_string() {
        val uriRef1 = fromURIReference("http://example.com").build()
        Assertions.assertEquals("http://example.com", uriRef1.toString())
        Assertions.assertEquals(false, uriRef1.isRelativeReference)
        Assertions.assertEquals(true, uriRef1.hasAuthority())
        Assertions.assertEquals("http", uriRef1.scheme)
        val authority1 = uriRef1.authority
        Assertions.assertEquals("example.com", authority1.toString())
        Assertions.assertEquals(null, authority1?.userinfo)
        val host1 = authority1?.host
        Assertions.assertEquals("example.com", host1.toString())
        Assertions.assertEquals("example.com", host1?.value)
        Assertions.assertEquals(HostType.REGNAME, host1?.type)
        Assertions.assertEquals(-1, authority1?.port)
        Assertions.assertEquals("", uriRef1.path)
        Assertions.assertEquals(null, uriRef1.query)
        Assertions.assertEquals(null, uriRef1.fragment)

        val uriRef2 = fromURIReference("hTTp://example.com").build()

        Assertions.assertEquals("hTTp://example.com", uriRef2.toString())
        Assertions.assertEquals(false, uriRef2.isRelativeReference)
        Assertions.assertEquals(true, uriRef2.hasAuthority())
        Assertions.assertEquals("hTTp", uriRef2.scheme)
        val authority2 = uriRef2.authority
        Assertions.assertEquals("example.com", authority2.toString())
        Assertions.assertEquals(null, authority2?.userinfo)
        val host2 = authority2?.host
        Assertions.assertEquals("example.com", host2.toString())
        Assertions.assertEquals("example.com", host2?.value)
        Assertions.assertEquals(HostType.REGNAME, host2?.type)
        Assertions.assertEquals(-1, authority2?.port)
        Assertions.assertEquals("", uriRef2.path)
        Assertions.assertEquals(null, uriRef2.query)
        Assertions.assertEquals(null, uriRef2.fragment)

        val uriRef3 = fromURIReference("//example.com").build()

        Assertions.assertEquals("//example.com", uriRef3.toString())
        Assertions.assertEquals(true, uriRef3.isRelativeReference)
        Assertions.assertEquals(true, uriRef3.hasAuthority())
        Assertions.assertEquals(null, uriRef3.scheme)
        val authority = uriRef3.authority
        Assertions.assertEquals("example.com", authority.toString())
        Assertions.assertEquals(null, authority?.userinfo)
        val host = authority?.host
        Assertions.assertEquals("example.com", host.toString())
        Assertions.assertEquals("example.com", host?.value)
        Assertions.assertEquals(HostType.REGNAME, host?.type)
        Assertions.assertEquals(-1, authority?.port)
        Assertions.assertEquals("", uriRef3.path)
        Assertions.assertEquals(null, uriRef3.query)
        Assertions.assertEquals(null, uriRef3.fragment)

        val uriRef4 = fromURIReference("http:").setAuthorityRequired(false).build()

        Assertions.assertEquals(false, uriRef4.isRelativeReference)
        Assertions.assertEquals(false, uriRef4.hasAuthority())
        Assertions.assertEquals("http", uriRef4.scheme)
        Assertions.assertEquals(null, uriRef4.authority)
        Assertions.assertEquals("", uriRef4.path)
        Assertions.assertEquals(null, uriRef4.query)
        Assertions.assertEquals(null, uriRef4.fragment)

        val uriRef5 = fromURIReference("http://john@example.com").build()

        Assertions.assertEquals("http://john@example.com", uriRef5.toString())
        Assertions.assertEquals(false, uriRef5.isRelativeReference)
        Assertions.assertEquals(true, uriRef5.hasAuthority())
        Assertions.assertEquals("http", uriRef5.scheme)
        val authority5 = uriRef5.authority
        Assertions.assertEquals("john@example.com", authority5.toString())
        Assertions.assertEquals("john", authority5!!.userinfo)
        val host5 = authority5.host
        Assertions.assertEquals("example.com", host5.toString())
        Assertions.assertEquals("example.com", host5!!.value)
        Assertions.assertEquals(HostType.REGNAME, host5.type)
        Assertions.assertEquals(-1, authority5.port)
        Assertions.assertEquals("", uriRef5.path)
        Assertions.assertEquals(null, uriRef5.query)
        Assertions.assertEquals(null, uriRef5.fragment)

        val uriRef6 = fromURIReference("http://%6A%6F%68%6E@example.com").build()

        Assertions.assertEquals("http://%6A%6F%68%6E@example.com", uriRef6.toString())
        Assertions.assertEquals(false, uriRef6.isRelativeReference)
        Assertions.assertEquals(true, uriRef6.hasAuthority())
        Assertions.assertEquals("http", uriRef6.scheme)
        val authority6 = uriRef6.authority
        Assertions.assertEquals("%6A%6F%68%6E@example.com", authority6.toString())
        Assertions.assertEquals("%6A%6F%68%6E", authority6!!.userinfo)
        val host6 = authority6.host
        Assertions.assertEquals("example.com", host6.toString())
        Assertions.assertEquals("example.com", host6!!.value)
        Assertions.assertEquals(HostType.REGNAME, host6.type)
        Assertions.assertEquals(-1, authority6.port)
        Assertions.assertEquals("", uriRef6.path)
        Assertions.assertEquals(null, uriRef6.query)
        Assertions.assertEquals(null, uriRef6.fragment)

        val uriRef7 = fromURIReference("http://101.102.103.104").build()

        Assertions.assertEquals("http://101.102.103.104", uriRef7.toString())
        Assertions.assertEquals(false, uriRef7.isRelativeReference)
        Assertions.assertEquals(true, uriRef7.hasAuthority())
        Assertions.assertEquals("http", uriRef7.scheme)
        val authority7 = uriRef7.authority
        Assertions.assertEquals("101.102.103.104", authority7.toString())
        Assertions.assertEquals(null, authority7!!.userinfo)
        val host7 = authority7.host
        Assertions.assertEquals("101.102.103.104", host7.toString())
        Assertions.assertEquals("101.102.103.104", host7!!.value)
        Assertions.assertEquals(HostType.IPV4, host7.type)
        Assertions.assertEquals(-1, authority7.port)
        Assertions.assertEquals("", uriRef7.path)
        Assertions.assertEquals(null, uriRef7.query)
        Assertions.assertEquals(null, uriRef7.fragment)

        val uriRef8 = fromURIReference("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").build()

        Assertions.assertEquals("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.toString())
        Assertions.assertEquals(false, uriRef8.isRelativeReference)
        Assertions.assertEquals(true, uriRef8.hasAuthority())
        Assertions.assertEquals("http", uriRef8.scheme)
        val authority8 = uriRef8.authority
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority8.toString())
        Assertions.assertEquals(null, authority8!!.userinfo)
        val host8 = authority8.host
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8.toString())
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8!!.value)
        Assertions.assertEquals(HostType.IPV6, host8.type)
        Assertions.assertEquals(-1, authority8.port)
        Assertions.assertEquals("", uriRef8.path)
        Assertions.assertEquals(null, uriRef8.query)
        Assertions.assertEquals(null, uriRef8.fragment)

        val uriRef9 = fromURIReference("http://[2001:db8:0:1:1:1:1:1]").build()

        Assertions.assertEquals("http://[2001:db8:0:1:1:1:1:1]", uriRef9.toString())
        Assertions.assertEquals(false, uriRef9.isRelativeReference)
        Assertions.assertEquals(true, uriRef9.hasAuthority())
        Assertions.assertEquals("http", uriRef9.scheme)
        val authority9 = uriRef9.authority
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", authority9.toString())
        Assertions.assertEquals(null, authority9!!.userinfo)
        val host9 = authority9.host
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", host9.toString())
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", host9!!.value)
        Assertions.assertEquals(HostType.IPV6, host9.type)
        Assertions.assertEquals(-1, authority9.port)
        Assertions.assertEquals("", uriRef9.path)
        Assertions.assertEquals(null, uriRef9.query)
        Assertions.assertEquals(null, uriRef9.fragment)

        val uriRef10 = fromURIReference("http://[2001:0:9d38:6abd:0:0:0:42]").build()

        Assertions.assertEquals("http://[2001:0:9d38:6abd:0:0:0:42]", uriRef10.toString())
        Assertions.assertEquals(false, uriRef10.isRelativeReference)
        Assertions.assertEquals(true, uriRef10.hasAuthority())
        Assertions.assertEquals("http", uriRef10.scheme)
        val authority10 = uriRef10.authority
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", authority10.toString())
        Assertions.assertEquals(null, authority10!!.userinfo)
        val host10 = authority10.host
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10.toString())
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10!!.value)
        Assertions.assertEquals(HostType.IPV6, host10.type)
        Assertions.assertEquals(-1, authority10.port)
        Assertions.assertEquals("", uriRef10.path)
        Assertions.assertEquals(null, uriRef10.query)
        Assertions.assertEquals(null, uriRef10.fragment)

        val uriRef11 = fromURIReference("http://[fe80::1]").build()

        Assertions.assertEquals("http://[fe80::1]", uriRef11.toString())
        Assertions.assertEquals(false, uriRef11.isRelativeReference)
        Assertions.assertEquals(true, uriRef11.hasAuthority())
        Assertions.assertEquals("http", uriRef11.scheme)
        val authority11 = uriRef11.authority
        Assertions.assertEquals("[fe80::1]", authority11.toString())
        Assertions.assertEquals(null, authority11!!.userinfo)
        val host11 = authority11.host
        Assertions.assertEquals("[fe80::1]", host11.toString())
        Assertions.assertEquals("[fe80::1]", host11!!.value)
        Assertions.assertEquals(HostType.IPV6, host11.type)
        Assertions.assertEquals(-1, authority11.port)
        Assertions.assertEquals("", uriRef11.path)
        Assertions.assertEquals(null, uriRef11.query)
        Assertions.assertEquals(null, uriRef11.fragment)

        val uriRef12 = fromURIReference("http://[2001:0:3238:DFE1:63::FEFB]").build()

        Assertions.assertEquals("http://[2001:0:3238:DFE1:63::FEFB]", uriRef12.toString())
        Assertions.assertEquals(false, uriRef12.isRelativeReference)
        Assertions.assertEquals(true, uriRef12.hasAuthority())
        Assertions.assertEquals("http", uriRef12.scheme)
        val authority12 = uriRef12.authority
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", authority12.toString())
        Assertions.assertEquals(null, authority12!!.userinfo)
        val host12 = authority12.host
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12.toString())
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12!!.value)
        Assertions.assertEquals(HostType.IPV6, host12.type)
        Assertions.assertEquals(-1, authority12.port)
        Assertions.assertEquals("", uriRef12.path)
        Assertions.assertEquals(null, uriRef12.query)
        Assertions.assertEquals(null, uriRef12.fragment)

        val uriRef13 = fromURIReference("http://[v1.fe80::a+en1]").build()

        Assertions.assertEquals("http://[v1.fe80::a+en1]", uriRef13.toString())
        Assertions.assertEquals(false, uriRef13.isRelativeReference)
        Assertions.assertEquals(true, uriRef13.hasAuthority())
        Assertions.assertEquals("http", uriRef13.scheme)
        val authority13 = uriRef13.authority
        Assertions.assertEquals("[v1.fe80::a+en1]", authority13.toString())
        Assertions.assertEquals(null, authority13!!.userinfo)
        val host13 = authority13.host
        Assertions.assertEquals("[v1.fe80::a+en1]", host13.toString())
        Assertions.assertEquals("[v1.fe80::a+en1]", host13!!.value)
        Assertions.assertEquals(HostType.IPVFUTURE, host13.type)
        Assertions.assertEquals(-1, authority13.port)
        Assertions.assertEquals("", uriRef13.path)
        Assertions.assertEquals(null, uriRef13.query)
        Assertions.assertEquals(null, uriRef13.fragment)

        val uriRef14 = fromURIReference("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").build()

        Assertions.assertEquals("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.toString())
        Assertions.assertEquals(false, uriRef14.isRelativeReference)
        Assertions.assertEquals(true, uriRef14.hasAuthority())
        Assertions.assertEquals("http", uriRef14.scheme)
        val authority14 = uriRef14.authority
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", authority14.toString())
        Assertions.assertEquals(null, authority14!!.userinfo)
        val host14 = authority14.host
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14.toString())
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14!!.value)
        Assertions.assertEquals(HostType.REGNAME, host14.type)
        Assertions.assertEquals(-1, authority14.port)
        Assertions.assertEquals("", uriRef14.path)
        Assertions.assertEquals(null, uriRef14.query)
        Assertions.assertEquals(null, uriRef14.fragment)

        val uriRef15 = fromURIReference("http://").build()

        Assertions.assertEquals(false, uriRef15.isRelativeReference)
        Assertions.assertEquals(true, uriRef15.hasAuthority())
        Assertions.assertEquals("http", uriRef15.scheme)
        val authority15 = uriRef15.authority
        Assertions.assertEquals(null, authority15!!.userinfo)
        val host15 = authority15.host
        Assertions.assertEquals("", host15!!.value)
        Assertions.assertEquals("", host15.toString())
        Assertions.assertEquals(HostType.REGNAME, host15.type)
        Assertions.assertEquals(-1, authority15.port)
        Assertions.assertEquals("", uriRef15.path)
        Assertions.assertEquals(null, uriRef15.query)
        Assertions.assertEquals(null, uriRef15.fragment)

        val uriRef16 = fromURIReference("http:///a").build()

        Assertions.assertEquals(false, uriRef16.isRelativeReference)
        Assertions.assertEquals(true, uriRef16.hasAuthority())
        Assertions.assertEquals("http", uriRef16.scheme)
        val authority16 = uriRef16.authority
        Assertions.assertEquals(null, authority16!!.userinfo)
        val host16 = authority16.host
        Assertions.assertEquals("", host16.toString())
        Assertions.assertEquals("", host16!!.value)
        Assertions.assertEquals(HostType.REGNAME, host16.type)
        Assertions.assertEquals(-1, authority16.port)
        Assertions.assertEquals("/a", uriRef16.path)
        Assertions.assertEquals(null, uriRef16.query)
        Assertions.assertEquals(null, uriRef16.fragment)

        val uriRef17 = fromURIReference("http://example.com:80").build()

        Assertions.assertEquals(false, uriRef17.isRelativeReference)
        Assertions.assertEquals(true, uriRef17.hasAuthority())
        Assertions.assertEquals("http", uriRef17.scheme)
        val authority17 = uriRef17.authority
        Assertions.assertEquals(null, authority17!!.userinfo)
        val host17 = authority17.host
        Assertions.assertEquals("example.com", host17.toString())
        Assertions.assertEquals("example.com", host17!!.value)
        Assertions.assertEquals(HostType.REGNAME, host17.type)
        Assertions.assertEquals(80, authority17.port)
        Assertions.assertEquals("", uriRef17.path)
        Assertions.assertEquals(null, uriRef17.query)
        Assertions.assertEquals(null, uriRef17.fragment)

        val uriRef18 = fromURIReference("http://example.com:").build()

        Assertions.assertEquals(false, uriRef18.isRelativeReference)
        Assertions.assertEquals(true, uriRef18.hasAuthority())
        Assertions.assertEquals("http", uriRef18.scheme)
        val authority18 = uriRef18.authority
        Assertions.assertEquals(null, authority18!!.userinfo)
        val host18 = authority18.host
        Assertions.assertEquals("example.com", host18.toString())
        Assertions.assertEquals("example.com", host18!!.value)
        Assertions.assertEquals(HostType.REGNAME, host18.type)
        Assertions.assertEquals(-1, authority18.port)
        Assertions.assertEquals("", uriRef18.path)
        Assertions.assertEquals(null, uriRef18.query)
        Assertions.assertEquals(null, uriRef18.fragment)

        val uriRef19 = fromURIReference("http://example.com:001").build()

        Assertions.assertEquals(false, uriRef19.isRelativeReference)
        Assertions.assertEquals(true, uriRef19.hasAuthority())
        Assertions.assertEquals("http", uriRef19.scheme)
        val authority19 = uriRef19.authority
        Assertions.assertEquals(null, authority19!!.userinfo)
        val host19 = authority19.host
        Assertions.assertEquals("example.com", host19.toString())
        Assertions.assertEquals("example.com", host19!!.value)
        Assertions.assertEquals(HostType.REGNAME, host19.type)
        Assertions.assertEquals(1, authority19.port)
        Assertions.assertEquals("", uriRef19.path)
        Assertions.assertEquals(null, uriRef19.query)
        Assertions.assertEquals(null, uriRef19.fragment)

        val uriRef20 = fromURIReference("http://example.com/a/b/c").build()

        Assertions.assertEquals("http://example.com/a/b/c", uriRef20.toString())
        Assertions.assertEquals(false, uriRef20.isRelativeReference)
        Assertions.assertEquals(true, uriRef20.hasAuthority())
        Assertions.assertEquals("http", uriRef20.scheme)
        val authority20 = uriRef20.authority
        Assertions.assertEquals("example.com", authority20.toString())
        Assertions.assertEquals(null, authority20!!.userinfo)
        val host20 = authority20.host
        Assertions.assertEquals("example.com", host20.toString())
        Assertions.assertEquals("example.com", host20!!.value)
        Assertions.assertEquals(HostType.REGNAME, host20.type)
        Assertions.assertEquals(-1, authority20.port)
        Assertions.assertEquals("/a/b/c", uriRef20.path)
        Assertions.assertEquals(null, uriRef20.query)
        Assertions.assertEquals(null, uriRef20.fragment)

        val uriRef21 = fromURIReference("http://example.com/%61/%62/%63").build()

        Assertions.assertEquals("http://example.com/%61/%62/%63", uriRef21.toString())
        Assertions.assertEquals(false, uriRef21.isRelativeReference)
        Assertions.assertEquals(true, uriRef21.hasAuthority())
        Assertions.assertEquals("http", uriRef21.scheme)
        val authority21 = uriRef21.authority
        Assertions.assertEquals("example.com", authority21.toString())
        Assertions.assertEquals(null, authority21!!.userinfo)
        val host21 = authority21.host
        Assertions.assertEquals("example.com", host21.toString())
        Assertions.assertEquals("example.com", host21!!.value)
        Assertions.assertEquals(HostType.REGNAME, host21.type)
        Assertions.assertEquals(-1, authority21.port)
        Assertions.assertEquals("/%61/%62/%63", uriRef21.path)
        Assertions.assertEquals(null, uriRef21.query)
        Assertions.assertEquals(null, uriRef21.fragment)

        val uriRef22 = fromURIReference("http:/a").setAuthorityRequired(false).build()

        Assertions.assertEquals(false, uriRef22.isRelativeReference)
        Assertions.assertEquals(false, uriRef22.hasAuthority())
        Assertions.assertEquals("http", uriRef22.scheme)
        Assertions.assertEquals(null, uriRef22.authority)
        Assertions.assertEquals("/a", uriRef22.path)
        Assertions.assertEquals(null, uriRef22.query)
        Assertions.assertEquals(null, uriRef22.fragment)

        val uriRef23 = fromURIReference("http:a").setAuthorityRequired(false).build()

        Assertions.assertEquals(false, uriRef23.isRelativeReference)
        Assertions.assertEquals(false, uriRef23.hasAuthority())
        Assertions.assertEquals("http", uriRef23.scheme)
        Assertions.assertEquals(null, uriRef23.authority)
        Assertions.assertEquals("a", uriRef23.path)
        Assertions.assertEquals(null, uriRef23.query)
        Assertions.assertEquals(null, uriRef23.fragment)

        val uriRef24 = fromURIReference("//").build()

        Assertions.assertEquals(true, uriRef24.isRelativeReference)
        Assertions.assertEquals(true, uriRef24.hasAuthority())
        Assertions.assertEquals(null, uriRef24.scheme)
        val authority24 = uriRef24.authority
        Assertions.assertEquals("", authority24.toString())
        Assertions.assertEquals(null, authority24!!.userinfo)
        val host24 = authority24.host
        Assertions.assertEquals("", host24.toString())
        Assertions.assertEquals("", host24!!.value)
        Assertions.assertEquals(HostType.REGNAME, host24.type)
        Assertions.assertEquals(-1, authority24.port)
        Assertions.assertEquals("", uriRef24.path)
        Assertions.assertEquals(null, uriRef24.query)
        Assertions.assertEquals(null, uriRef24.fragment)

        assertThrowsNPE<Throwable>(
            "The input string must not be null.",
            { URIReferenceBuilder.fromURIReference(null).build() })
    }


    @Test
    fun test_fromURIReference_with_uriReference() {
        val uriRef1 = fromURIReference(parse("http://example.com").toString()).build()

        Assertions.assertEquals("http://example.com", uriRef1.toString())
        Assertions.assertEquals(false, uriRef1.isRelativeReference)
        Assertions.assertEquals(true, uriRef1.hasAuthority())
        Assertions.assertEquals("http", uriRef1.scheme)
        val authority1 = uriRef1.authority
        Assertions.assertEquals("example.com", authority1.toString())
        Assertions.assertEquals(null, authority1!!.userinfo)
        val host1 = authority1.host
        Assertions.assertEquals("example.com", host1.toString())
        Assertions.assertEquals("example.com", host1!!.value)
        Assertions.assertEquals(HostType.REGNAME, host1.type)
        Assertions.assertEquals(-1, authority1.port)
        Assertions.assertEquals("", uriRef1.path)
        Assertions.assertEquals(null, uriRef1.query)
        Assertions.assertEquals(null, uriRef1.fragment)

        val uriRef2 = fromURIReference(parse("hTTp://example.com").toString()).build()
        Assertions.assertEquals("hTTp://example.com", uriRef2.toString())
        Assertions.assertEquals(false, uriRef2.isRelativeReference)
        Assertions.assertEquals(true, uriRef2.hasAuthority())
        Assertions.assertEquals("hTTp", uriRef2.scheme)
        val authority2 = uriRef2.authority
        Assertions.assertEquals("example.com", authority2.toString())
        Assertions.assertEquals(null, authority2!!.userinfo)
        val host2 = authority2.host
        Assertions.assertEquals("example.com", host2.toString())
        Assertions.assertEquals("example.com", host2!!.value)
        Assertions.assertEquals(HostType.REGNAME, host2.type)
        Assertions.assertEquals(-1, authority2.port)
        Assertions.assertEquals("", uriRef2.path)
        Assertions.assertEquals(null, uriRef2.query)
        Assertions.assertEquals(null, uriRef2.fragment)

        val uriRef3 = fromURIReference(parse("//example.com").toString()).build()
        Assertions.assertEquals("//example.com", uriRef3.toString())
        Assertions.assertEquals(true, uriRef3.isRelativeReference)
        Assertions.assertEquals(true, uriRef3.hasAuthority())
        Assertions.assertEquals(null, uriRef3.scheme)
        val authority = uriRef3.authority
        Assertions.assertEquals("example.com", authority.toString())
        Assertions.assertEquals(null, authority!!.userinfo)
        val host = authority.host
        Assertions.assertEquals("example.com", host.toString())
        Assertions.assertEquals("example.com", host!!.value)
        Assertions.assertEquals(HostType.REGNAME, host.type)
        Assertions.assertEquals(-1, authority.port)
        Assertions.assertEquals("", uriRef3.path)
        Assertions.assertEquals(null, uriRef3.query)
        Assertions.assertEquals(null, uriRef3.fragment)

        val uriRef4 = fromURIReference(parse("http:").toString()).setAuthorityRequired(false).build()
        Assertions.assertEquals(false, uriRef4.isRelativeReference)
        Assertions.assertEquals(false, uriRef4.hasAuthority())
        Assertions.assertEquals("http", uriRef4.scheme)
        Assertions.assertEquals(false, uriRef4.hasAuthority())
        Assertions.assertEquals(null, uriRef4.authority)
        Assertions.assertEquals("", uriRef4.path)
        Assertions.assertEquals(null, uriRef4.query)
        Assertions.assertEquals(null, uriRef4.fragment)

        val uriRef5 = fromURIReference(parse("http://john@example.com").toString()).build()
        Assertions.assertEquals("http://john@example.com", uriRef5.toString())
        Assertions.assertEquals(false, uriRef5.isRelativeReference)
        Assertions.assertEquals(true, uriRef5.hasAuthority())
        Assertions.assertEquals("http", uriRef5.scheme)
        val authority5 = uriRef5.authority
        Assertions.assertEquals("john@example.com", authority5.toString())
        Assertions.assertEquals("john", authority5!!.userinfo)
        val host5 = authority5.host
        Assertions.assertEquals("example.com", host5.toString())
        Assertions.assertEquals("example.com", host5!!.value)
        Assertions.assertEquals(HostType.REGNAME, host5.type)
        Assertions.assertEquals(-1, authority5.port)
        Assertions.assertEquals("", uriRef5.path)
        Assertions.assertEquals(null, uriRef5.query)
        Assertions.assertEquals(null, uriRef5.fragment)

        val uriRef6 = fromURIReference(parse("http://%6A%6F%68%6E@example.com").toString()).build()
        Assertions.assertEquals("http://%6A%6F%68%6E@example.com", uriRef6.toString())
        Assertions.assertEquals(false, uriRef6.isRelativeReference)
        Assertions.assertEquals(true, uriRef6.hasAuthority())
        Assertions.assertEquals("http", uriRef6.scheme)
        val authority6 = uriRef6.authority
        Assertions.assertEquals("%6A%6F%68%6E@example.com", authority6.toString())
        Assertions.assertEquals("%6A%6F%68%6E", authority6!!.userinfo)
        val host6 = authority6.host
        Assertions.assertEquals("example.com", host6.toString())
        Assertions.assertEquals("example.com", host6!!.value)
        Assertions.assertEquals(HostType.REGNAME, host6.type)
        Assertions.assertEquals(-1, authority6.port)
        Assertions.assertEquals("", uriRef6.path)
        Assertions.assertEquals(null, uriRef6.query)
        Assertions.assertEquals(null, uriRef6.fragment)

        val uriRef7 = fromURIReference(parse("http://101.102.103.104").toString()).build()
        Assertions.assertEquals("http://101.102.103.104", uriRef7.toString())
        Assertions.assertEquals(false, uriRef7.isRelativeReference)
        Assertions.assertEquals(true, uriRef7.hasAuthority())
        Assertions.assertEquals("http", uriRef7.scheme)
        val authority7 = uriRef7.authority
        Assertions.assertEquals("101.102.103.104", authority7.toString())
        Assertions.assertEquals(null, authority7!!.userinfo)
        val host7 = authority7.host
        Assertions.assertEquals("101.102.103.104", host7.toString())
        Assertions.assertEquals("101.102.103.104", host7!!.value)
        Assertions.assertEquals(HostType.IPV4, host7.type)
        Assertions.assertEquals(-1, authority7.port)
        Assertions.assertEquals("", uriRef7.path)
        Assertions.assertEquals(null, uriRef7.query)
        Assertions.assertEquals(null, uriRef7.fragment)

        val uriRef8 = fromURIReference(parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").toString()).build()
        Assertions.assertEquals("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.toString())
        Assertions.assertEquals(false, uriRef8.isRelativeReference)
        Assertions.assertEquals(true, uriRef8.hasAuthority())
        Assertions.assertEquals("http", uriRef8.scheme)
        val authority8 = uriRef8.authority
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority8.toString())
        Assertions.assertEquals(null, authority8!!.userinfo)
        val host8 = authority8.host
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8.toString())
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8!!.value)
        Assertions.assertEquals(HostType.IPV6, host8.type)
        Assertions.assertEquals(-1, authority8.port)
        Assertions.assertEquals("", uriRef8.path)
        Assertions.assertEquals(null, uriRef8.query)
        Assertions.assertEquals(null, uriRef8.fragment)

        val uriRef9 = fromURIReference(parse("http://[2001:db8:0:1:1:1:1:1]").toString()).build()
        Assertions.assertEquals("http://[2001:db8:0:1:1:1:1:1]", uriRef9.toString())
        Assertions.assertEquals(false, uriRef9.isRelativeReference)
        Assertions.assertEquals(true, uriRef9.hasAuthority())
        Assertions.assertEquals("http", uriRef9.scheme)
        val authority9 = uriRef9.authority
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", authority9.toString())
        Assertions.assertEquals(null, authority9!!.userinfo)
        val host9 = authority9.host
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", host9.toString())
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", host9!!.value)
        Assertions.assertEquals(HostType.IPV6, host9.type)
        Assertions.assertEquals(-1, authority9.port)
        Assertions.assertEquals("", uriRef9.path)
        Assertions.assertEquals(null, uriRef9.query)
        Assertions.assertEquals(null, uriRef9.fragment)

        val uriRef10 = fromURIReference(parse("http://[2001:0:9d38:6abd:0:0:0:42]").toString()).build()
        Assertions.assertEquals("http://[2001:0:9d38:6abd:0:0:0:42]", uriRef10.toString())
        Assertions.assertEquals(false, uriRef10.isRelativeReference)
        Assertions.assertEquals(true, uriRef10.hasAuthority())
        Assertions.assertEquals("http", uriRef10.scheme)
        val authority10 = uriRef10.authority
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", authority10.toString())
        Assertions.assertEquals(null, authority10!!.userinfo)
        val host10 = authority10.host
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10.toString())
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10!!.value)
        Assertions.assertEquals(HostType.IPV6, host10.type)
        Assertions.assertEquals(-1, authority10.port)
        Assertions.assertEquals("", uriRef10.path)
        Assertions.assertEquals(null, uriRef10.query)
        Assertions.assertEquals(null, uriRef10.fragment)

        val uriRef11 = fromURIReference(parse("http://[fe80::1]").toString()).build()
        Assertions.assertEquals("http://[fe80::1]", uriRef11.toString())
        Assertions.assertEquals(false, uriRef11.isRelativeReference)
        Assertions.assertEquals(true, uriRef11.hasAuthority())
        Assertions.assertEquals("http", uriRef11.scheme)
        val authority11 = uriRef11.authority
        Assertions.assertEquals("[fe80::1]", authority11.toString())
        Assertions.assertEquals(null, authority11!!.userinfo)
        val host11 = authority11.host
        Assertions.assertEquals("[fe80::1]", host11.toString())
        Assertions.assertEquals("[fe80::1]", host11!!.value)
        Assertions.assertEquals(HostType.IPV6, host11.type)
        Assertions.assertEquals(-1, authority11.port)
        Assertions.assertEquals("", uriRef11.path)
        Assertions.assertEquals(null, uriRef11.query)
        Assertions.assertEquals(null, uriRef11.fragment)

        val uriRef12 = fromURIReference(parse("http://[2001:0:3238:DFE1:63::FEFB]").toString()).build()
        Assertions.assertEquals("http://[2001:0:3238:DFE1:63::FEFB]", uriRef12.toString())
        Assertions.assertEquals(false, uriRef12.isRelativeReference)
        Assertions.assertEquals(true, uriRef12.hasAuthority())
        Assertions.assertEquals("http", uriRef12.scheme)
        val authority12 = uriRef12.authority
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", authority12.toString())
        Assertions.assertEquals(null, authority12!!.userinfo)
        val host12 = authority12.host
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12.toString())
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12!!.value)
        Assertions.assertEquals(HostType.IPV6, host12.type)
        Assertions.assertEquals(-1, authority12.port)
        Assertions.assertEquals("", uriRef12.path)
        Assertions.assertEquals(null, uriRef12.query)
        Assertions.assertEquals(null, uriRef12.fragment)

        val uriRef13 = fromURIReference(parse("http://[v1.fe80::a+en1]").toString()).build()
        Assertions.assertEquals("http://[v1.fe80::a+en1]", uriRef13.toString())
        Assertions.assertEquals(false, uriRef13.isRelativeReference)
        Assertions.assertEquals(true, uriRef13.hasAuthority())
        Assertions.assertEquals("http", uriRef13.scheme)
        val authority13 = uriRef13.authority
        Assertions.assertEquals("[v1.fe80::a+en1]", authority13.toString())
        Assertions.assertEquals(null, authority13!!.userinfo)
        val host13 = authority13.host
        Assertions.assertEquals("[v1.fe80::a+en1]", host13.toString())
        Assertions.assertEquals("[v1.fe80::a+en1]", host13!!.value)
        Assertions.assertEquals(HostType.IPVFUTURE, host13.type)
        Assertions.assertEquals(-1, authority13.port)
        Assertions.assertEquals("", uriRef13.path)
        Assertions.assertEquals(null, uriRef13.query)
        Assertions.assertEquals(null, uriRef13.fragment)

        val uriRef14 = fromURIReference(parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").toString()).build()
        Assertions.assertEquals("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.toString())
        Assertions.assertEquals(false, uriRef14.isRelativeReference)
        Assertions.assertEquals(true, uriRef14.hasAuthority())
        Assertions.assertEquals("http", uriRef14.scheme)
        val authority14 = uriRef14.authority
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", authority14.toString())
        Assertions.assertEquals(null, authority14!!.userinfo)
        val host14 = authority14.host
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14.toString())
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14!!.value)
        Assertions.assertEquals(HostType.REGNAME, host14.type)
        Assertions.assertEquals(-1, authority14.port)
        Assertions.assertEquals("", uriRef14.path)
        Assertions.assertEquals(null, uriRef14.query)
        Assertions.assertEquals(null, uriRef14.fragment)

        val uriRef15 = fromURIReference(parse("http://").toString()).build()
        Assertions.assertEquals(false, uriRef15.isRelativeReference)
        Assertions.assertEquals(true, uriRef15.hasAuthority())
        Assertions.assertEquals("http", uriRef15.scheme)
        val authority15 = uriRef15.authority
        Assertions.assertEquals(null, authority15!!.userinfo)
        val host15 = authority15.host
        Assertions.assertEquals("", host15?.value)
        Assertions.assertEquals("", host15.toString())
        Assertions.assertEquals(HostType.REGNAME, host15?.type)
        Assertions.assertEquals(-1, authority15.port)
        Assertions.assertEquals("", uriRef15.path)
        Assertions.assertEquals(null, uriRef15.query)
        Assertions.assertEquals(null, uriRef15.fragment)

        val uriRef16 = fromURIReference(parse("http:///a").toString()).build()
        Assertions.assertEquals(false, uriRef16.isRelativeReference)
        Assertions.assertEquals(true, uriRef16.hasAuthority())
        Assertions.assertEquals("http", uriRef16.scheme)
        val authority16 = uriRef16.authority
        Assertions.assertEquals(null, authority16!!.userinfo)
        val host16 = authority16.host
        Assertions.assertEquals("", host16.toString())
        Assertions.assertEquals("", host16!!.value)
        Assertions.assertEquals(HostType.REGNAME, host16.type)
        Assertions.assertEquals(-1, authority16.port)
        Assertions.assertEquals("/a", uriRef16.path)
        Assertions.assertEquals(null, uriRef16.query)
        Assertions.assertEquals(null, uriRef16.fragment)

        val uriRef17 = fromURIReference(parse("http://example.com:80").toString()).build()
        Assertions.assertEquals(false, uriRef17.isRelativeReference)
        Assertions.assertEquals(true, uriRef17.hasAuthority())
        Assertions.assertEquals("http", uriRef17.scheme)
        val authority17 = uriRef17.authority
        Assertions.assertEquals(null, authority17!!.userinfo)
        val host17 = authority17.host
        Assertions.assertEquals("example.com", host17.toString())
        Assertions.assertEquals("example.com", host17!!.value)
        Assertions.assertEquals(HostType.REGNAME, host17.type)
        Assertions.assertEquals(80, authority17.port)
        Assertions.assertEquals("", uriRef17.path)
        Assertions.assertEquals(null, uriRef17.query)
        Assertions.assertEquals(null, uriRef17.fragment)

        val uriRef18 = fromURIReference(parse("http://example.com:").toString()).build()
        Assertions.assertEquals(false, uriRef18.isRelativeReference)
        Assertions.assertEquals(true, uriRef18.hasAuthority())
        Assertions.assertEquals("http", uriRef18.scheme)
        val authority18 = uriRef18.authority
        Assertions.assertEquals(null, authority18!!.userinfo)
        val host18 = authority18.host
        Assertions.assertEquals("example.com", host18.toString())
        Assertions.assertEquals("example.com", host18!!.value)
        Assertions.assertEquals(HostType.REGNAME, host18.type)
        Assertions.assertEquals(-1, authority18.port)
        Assertions.assertEquals("", uriRef18.path)
        Assertions.assertEquals(null, uriRef18.query)
        Assertions.assertEquals(null, uriRef18.fragment)

        val uriRef19 = fromURIReference(parse("http://example.com:001").toString()).build()
        Assertions.assertEquals(false, uriRef19.isRelativeReference)
        Assertions.assertEquals(true, uriRef19.hasAuthority())
        Assertions.assertEquals("http", uriRef19.scheme)
        val authority19 = uriRef19.authority
        Assertions.assertEquals(null, authority19!!.userinfo)
        val host19 = authority19.host
        Assertions.assertEquals("example.com", host19.toString())
        Assertions.assertEquals("example.com", host19!!.value)
        Assertions.assertEquals(HostType.REGNAME, host19.type)
        Assertions.assertEquals(1, authority19.port)
        Assertions.assertEquals("", uriRef19.path)
        Assertions.assertEquals(null, uriRef19.query)
        Assertions.assertEquals(null, uriRef19.fragment)

        val uriRef20 = fromURIReference(parse("http://example.com/a/b/c").toString()).build()
        Assertions.assertEquals("http://example.com/a/b/c", uriRef20.toString())
        Assertions.assertEquals(false, uriRef20.isRelativeReference)
        Assertions.assertEquals(true, uriRef20.hasAuthority())
        Assertions.assertEquals("http", uriRef20.scheme)
        val authority20 = uriRef20.authority
        Assertions.assertEquals("example.com", authority20.toString())
        Assertions.assertEquals(null, authority20!!.userinfo)
        val host20 = authority20.host
        Assertions.assertEquals("example.com", host20.toString())
        Assertions.assertEquals("example.com", host20!!.value)
        Assertions.assertEquals(HostType.REGNAME, host20.type)
        Assertions.assertEquals(-1, authority20.port)
        Assertions.assertEquals("/a/b/c", uriRef20.path)
        Assertions.assertEquals(null, uriRef20.query)
        Assertions.assertEquals(null, uriRef20.fragment)

        val uriRef21 = fromURIReference(parse("http://example.com/%61/%62/%63").toString()).build()
        Assertions.assertEquals("http://example.com/%61/%62/%63", uriRef21.toString())
        Assertions.assertEquals(false, uriRef21.isRelativeReference)
        Assertions.assertEquals(true, uriRef21.hasAuthority())
        Assertions.assertEquals("http", uriRef21.scheme)
        val authority21 = uriRef21.authority
        Assertions.assertEquals("example.com", authority21.toString())
        Assertions.assertEquals(null, authority21!!.userinfo)
        val host21 = authority21.host
        Assertions.assertEquals("example.com", host21.toString())
        Assertions.assertEquals("example.com", host21!!.value)
        Assertions.assertEquals(HostType.REGNAME, host21.type)
        Assertions.assertEquals(-1, authority21.port)
        Assertions.assertEquals("/%61/%62/%63", uriRef21.path)
        Assertions.assertEquals(null, uriRef21.query)
        Assertions.assertEquals(null, uriRef21.fragment)

        val uriRef22 = fromURIReference(parse("http:/a").toString()).setAuthorityRequired(false).build()
        Assertions.assertEquals(false, uriRef22.isRelativeReference)
        Assertions.assertEquals(false, uriRef22.hasAuthority())
        Assertions.assertEquals("http", uriRef22.scheme)
        Assertions.assertEquals(null, uriRef22.authority)
        Assertions.assertEquals("/a", uriRef22.path)
        Assertions.assertEquals(null, uriRef22.query)
        Assertions.assertEquals(null, uriRef22.fragment)

        val uriRef23 = fromURIReference(parse("http:a").toString()).setAuthorityRequired(false).build()
        Assertions.assertEquals(false, uriRef23.isRelativeReference)
        Assertions.assertEquals(false, uriRef23.hasAuthority())
        Assertions.assertEquals("http", uriRef23.scheme)
        Assertions.assertEquals(null, uriRef23.authority)
        Assertions.assertEquals("a", uriRef23.path)
        Assertions.assertEquals(null, uriRef23.query)
        Assertions.assertEquals(null, uriRef23.fragment)

        val uriRef24 = fromURIReference(parse("//").toString()).build()
        Assertions.assertEquals(true, uriRef24.isRelativeReference)
        Assertions.assertEquals(true, uriRef24.hasAuthority())
        Assertions.assertEquals(null, uriRef24.scheme)
        val authority24 = uriRef24.authority
        Assertions.assertEquals("", authority24.toString())
        Assertions.assertEquals(null, authority24?.userinfo)
        val host24 = authority24?.host
        Assertions.assertEquals("", host24.toString())
        Assertions.assertEquals("", host24?.value)
        Assertions.assertEquals(HostType.REGNAME, host24?.type)
        Assertions.assertEquals(-1, authority24?.port)
        Assertions.assertEquals("", uriRef24.path)
        Assertions.assertEquals(null, uriRef24.query)
        Assertions.assertEquals(null, uriRef24.fragment)

        assertThrowsNPE<Throwable>(
            "The input string must not be null.",
            { fromURIReference(URIReference.parse(null)) })
    }


    @Test
    fun test_setAuthorityRequired() {
        val uriRef1 = fromURIReference("http://example.com")
            .setAuthorityRequired(true)
            .build()
        Assertions.assertEquals("http://example.com", uriRef1.toString())
        Assertions.assertEquals(false, uriRef1.isRelativeReference)
        Assertions.assertEquals(true, uriRef1.hasAuthority())
        Assertions.assertEquals("http", uriRef1.scheme)
        val authority1 = uriRef1.authority
        Assertions.assertEquals("example.com", authority1.toString())
        Assertions.assertEquals(null, authority1?.userinfo)
        val host1 = authority1?.host
        Assertions.assertEquals("example.com", host1.toString())
        Assertions.assertEquals("example.com", host1?.value)
        Assertions.assertEquals(HostType.REGNAME, host1?.type)
        Assertions.assertEquals(-1, authority1?.port)
        Assertions.assertEquals("", uriRef1.path)
        Assertions.assertEquals(null, uriRef1.query)
        Assertions.assertEquals(null, uriRef1.fragment)

        val uriRef2 = fromURIReference("http://example.com")
            .setAuthorityRequired(false)
            .build()
        Assertions.assertEquals("http:", uriRef2.toString())
        Assertions.assertEquals(false, uriRef2.isRelativeReference)
        Assertions.assertEquals(false, uriRef2.hasAuthority())
        Assertions.assertEquals("http", uriRef2.scheme)
        Assertions.assertEquals(null, uriRef2.authority)
        Assertions.assertEquals("", uriRef1.path)
        Assertions.assertEquals(null, uriRef1.query)
        Assertions.assertEquals(null, uriRef1.fragment)
    }


    @Test
    fun test_setScheme() {
        val uriRef1 = fromURIReference("http://example.com")
            .setScheme("ftp")
            .build()
        Assertions.assertEquals("ftp", uriRef1.scheme)

        val uriRef2 = fromURIReference("http://example.com")
            .setScheme("https")
            .build()
        Assertions.assertEquals("https", uriRef2.scheme)

        val uriRef3 = fromURIReference("http://example.com")
            .setScheme(null)
            .build()
        Assertions.assertEquals(null, uriRef3.scheme)
    }


    @Test
    fun test_setHost() {
        val uriRef1 = fromURIReference("http://example.com")
            .setHost("example2.com")
            .build()
        Assertions.assertEquals(HostType.REGNAME, uriRef1.host?.type)
        Assertions.assertEquals("example2.com", uriRef1.host?.value)

        val uriRef2 = fromURIReference("http://example.com")
            .setHost("101.102.103.104")
            .build()
        Assertions.assertEquals(HostType.IPV4, uriRef2.host?.type)
        Assertions.assertEquals("101.102.103.104", uriRef2.host?.value)

        val uriRef3 = fromURIReference("http://example.com")
            .setHost("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")
            .build()
        Assertions.assertEquals(HostType.IPV6, uriRef3.host?.type)
        Assertions.assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef3.host?.value)

        val uriRef4 = fromURIReference("http://example.com")
            .setHost("[2001:db8:0:1:1:1:1:1]")
            .build()
        Assertions.assertEquals(HostType.IPV6, uriRef4.host?.type)
        Assertions.assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef4.host?.value)

        val uriRef5 = fromURIReference("http://example.com")
            .setHost("[2001:0:9d38:6abd:0:0:0:42]")
            .build()
        Assertions.assertEquals(HostType.IPV6, uriRef5.host?.type)
        Assertions.assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef5.host?.value)

        val uriRef6 = fromURIReference("http://example.com")
            .setHost("[fe80::1]")
            .build()
        Assertions.assertEquals(HostType.IPV6, uriRef6.host?.type)
        Assertions.assertEquals("[fe80::1]", uriRef6.host?.value)

        val uriRef7 = fromURIReference("http://example.com")
            .setHost("[2001:0:3238:DFE1:63::FEFB]")
            .build()
        Assertions.assertEquals(HostType.IPV6, uriRef7.host?.type)
        Assertions.assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef7.host?.value)

        val uriRef8 = fromURIReference("http://example.com")
            .setHost("[v1.fe80::a+en1]")
            .build()
        Assertions.assertEquals(HostType.IPVFUTURE, uriRef8.host?.type)
        Assertions.assertEquals("[v1.fe80::a+en1]", uriRef8.host?.value)

        val uriRef9 = fromURIReference("http://example.com")
            .setHost("%65%78%61%6D%70%6C%65%2E%63%6F%6D")
            .build()
        Assertions.assertEquals(HostType.REGNAME, uriRef9.host?.type)
        Assertions.assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef9.host?.value)

        val uriRef10 = fromURIReference("http://example.com")
            .setHost("")
            .build()
        Assertions.assertEquals(HostType.REGNAME, uriRef10.host?.type)
        Assertions.assertEquals("", uriRef10.host?.value)

        val uriRef11 = fromURIReference("http://example.com")
            .setHost(null)
            .build()
        Assertions.assertEquals(HostType.REGNAME, uriRef11.host?.type)
        Assertions.assertEquals(null, uriRef11.host?.value)
    }


    @Test
    fun test_setPath() {
        val uriRef1 = fromURIReference("http://example.com")
            .setPath("/a")
            .build()
        Assertions.assertEquals("/a", uriRef1.path)

        val uriRef2 = fromURIReference("http://example.com")
            .setPath("/a/b")
            .build()
        Assertions.assertEquals("/a/b", uriRef2.path)

        val uriRef3 = fromURIReference("http://example.com")
            .setPath("/")
            .build()
        Assertions.assertEquals("/", uriRef3.path)

        val uriRef4 = fromURIReference("http://example.com")
            .setPath("")
            .build()
        Assertions.assertEquals("", uriRef4.path)

        val uriRef5 = fromURIReference("http://example.com")
            .setPath(null)
            .build()
        Assertions.assertEquals(null, uriRef5.path)
    }


    @Test
    fun test_setPathSegments() {
        val uriRef1 = fromURIReference("http://example.com")
            .appendPathSegments(listOf("a", "b", "c"))
            .build()
        Assertions.assertEquals("/a/b/c", uriRef1.path)

        val uriRef2 = fromURIReference("http://example.com")
            .appendPathSegments(listOf(""))
            .build()
        Assertions.assertEquals("/", uriRef2.path)

        assertThrowsNPE<Throwable>("A segment must not be null.", {
            fromURIReference("http://example.com")
                .appendPathSegments(listOf(null))
                .build()
        })
    }


    @Test
    fun test_appendQueryParam() {
        val uriRef1 = fromURIReference("http://example.com")
            .appendQueryParam("k", "v")
            .build()
        Assertions.assertEquals("k=v", uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com")
            .appendQueryParam("k1", "v1")
            .appendQueryParam("k2", "v2")
            .build()
        Assertions.assertEquals("k1=v1&k2=v2", uriRef2.query)

        val uriRef3 = fromURIReference("http://example.com")
            .appendQueryParam("", "")
            .build()
        Assertions.assertEquals("=", uriRef3.query)

        assertThrowsNPE<Throwable>("The key must not be null.", {
            fromURIReference("http://example.com")
                .appendQueryParam(null, null)
        })
    }


    @Test
    fun test_replaceQueryParam() {
        val uriRef1 = fromURIReference("http://example.com?k=v")
            .replaceQueryParam("k", "w")
            .build()
        Assertions.assertEquals("k=w", uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com?k=v")
            .replaceQueryParam("k", null)
            .build()
        Assertions.assertEquals("k", uriRef2.query)

        assertThrowsNPE<Throwable>("The key must not be null.", {
            fromURIReference("http://example.com?k=v")
                .appendQueryParam(null, "w")
        })
    }


    @Test
    fun test_removeQueryParam() {
        val uriRef1 = fromURIReference("http://example.com?k=v")
            .removeQueryParam("k")
            .build()
        Assertions.assertEquals(null, uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com?k=v")
            .removeQueryParam(null)
            .build()
        Assertions.assertEquals("k=v", uriRef2.query)
    }


    @Test
    fun test_setQuery() {
        val uriRef1 = fromURIReference("http://example.com").setQuery("k=v").build()
        Assertions.assertEquals("k=v", uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com").setQuery("k=").build()
        Assertions.assertEquals("k=", uriRef2.query)

        val uriRef3 = fromURIReference("http://example.com").setQuery("k").build()
        Assertions.assertEquals("k", uriRef3.query)

        val uriRef4 = fromURIReference("http://example.com").setQuery("").build()
        Assertions.assertEquals("", uriRef4.query)

        val uriRef5 = fromURIReference("http://example.com").setQuery(null).build()
        Assertions.assertEquals(null, uriRef5.query)
    }


    @Test
    fun test_setFragment() {
        val uriRef1 = fromURIReference("http://example.com").setFragment("section1").build()
        Assertions.assertEquals("section1", uriRef1.fragment)

        val uriRef2 = fromURIReference("http://example.com").setFragment("fig%20A").build()
        Assertions.assertEquals("fig%20A", uriRef2.fragment)

        val uriRef3 = fromURIReference("http://example.com").setFragment("2.3").build()
        Assertions.assertEquals("2.3", uriRef3.fragment)

        val uriRef4 = fromURIReference("http://example.com").setFragment("").build()
        Assertions.assertEquals("", uriRef4.fragment)

        val uriRef5 = fromURIReference("http://example.com").setFragment(null).build()
        Assertions.assertEquals(null, uriRef5.fragment)
    }
}
