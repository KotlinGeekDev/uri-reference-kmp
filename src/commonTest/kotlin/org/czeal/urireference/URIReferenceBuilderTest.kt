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
import kotlin.test.Test
import kotlin.test.assertEquals


class URIReferenceBuilderTest {
    @Test
    fun test_fromURIReference_with_string() {
        val uriRef1 = fromURIReference("http://example.com").build()
        assertEquals("http://example.com", uriRef1.toString())
        assertEquals(false, uriRef1.isRelativeReference)
        assertEquals(true, uriRef1.hasAuthority())
        assertEquals("http", uriRef1.scheme)
        val authority1 = uriRef1.authority
        assertEquals("example.com", authority1.toString())
        assertEquals(null, authority1?.userinfo)
        val host1 = authority1?.host
        assertEquals("example.com", host1.toString())
        assertEquals("example.com", host1?.value)
        assertEquals(HostType.REGNAME, host1?.type)
        assertEquals(-1, authority1?.port)
        assertEquals("", uriRef1.path)
        assertEquals(null, uriRef1.query)
        assertEquals(null, uriRef1.fragment)

        val uriRef2 = fromURIReference("hTTp://example.com").build()

        assertEquals("hTTp://example.com", uriRef2.toString())
        assertEquals(false, uriRef2.isRelativeReference)
        assertEquals(true, uriRef2.hasAuthority())
        assertEquals("hTTp", uriRef2.scheme)
        val authority2 = uriRef2.authority
        assertEquals("example.com", authority2.toString())
        assertEquals(null, authority2?.userinfo)
        val host2 = authority2?.host
        assertEquals("example.com", host2.toString())
        assertEquals("example.com", host2?.value)
        assertEquals(HostType.REGNAME, host2?.type)
        assertEquals(-1, authority2?.port)
        assertEquals("", uriRef2.path)
        assertEquals(null, uriRef2.query)
        assertEquals(null, uriRef2.fragment)

        val uriRef3 = fromURIReference("//example.com").build()

        assertEquals("//example.com", uriRef3.toString())
        assertEquals(true, uriRef3.isRelativeReference)
        assertEquals(true, uriRef3.hasAuthority())
        assertEquals(null, uriRef3.scheme)
        val authority = uriRef3.authority
        assertEquals("example.com", authority.toString())
        assertEquals(null, authority?.userinfo)
        val host = authority?.host
        assertEquals("example.com", host.toString())
        assertEquals("example.com", host?.value)
        assertEquals(HostType.REGNAME, host?.type)
        assertEquals(-1, authority?.port)
        assertEquals("", uriRef3.path)
        assertEquals(null, uriRef3.query)
        assertEquals(null, uriRef3.fragment)

        val uriRef4 = fromURIReference("http:").setAuthorityRequired(false).build()

        assertEquals(false, uriRef4.isRelativeReference)
        assertEquals(false, uriRef4.hasAuthority())
        assertEquals("http", uriRef4.scheme)
        assertEquals(null, uriRef4.authority)
        assertEquals("", uriRef4.path)
        assertEquals(null, uriRef4.query)
        assertEquals(null, uriRef4.fragment)

        val uriRef5 = fromURIReference("http://john@example.com").build()

        assertEquals("http://john@example.com", uriRef5.toString())
        assertEquals(false, uriRef5.isRelativeReference)
        assertEquals(true, uriRef5.hasAuthority())
        assertEquals("http", uriRef5.scheme)
        val authority5 = uriRef5.authority
        assertEquals("john@example.com", authority5.toString())
        assertEquals("john", authority5!!.userinfo)
        val host5 = authority5.host
        assertEquals("example.com", host5.toString())
        assertEquals("example.com", host5!!.value)
        assertEquals(HostType.REGNAME, host5.type)
        assertEquals(-1, authority5.port)
        assertEquals("", uriRef5.path)
        assertEquals(null, uriRef5.query)
        assertEquals(null, uriRef5.fragment)

        val uriRef6 = fromURIReference("http://%6A%6F%68%6E@example.com").build()

        assertEquals("http://%6A%6F%68%6E@example.com", uriRef6.toString())
        assertEquals(false, uriRef6.isRelativeReference)
        assertEquals(true, uriRef6.hasAuthority())
        assertEquals("http", uriRef6.scheme)
        val authority6 = uriRef6.authority
        assertEquals("%6A%6F%68%6E@example.com", authority6.toString())
        assertEquals("%6A%6F%68%6E", authority6!!.userinfo)
        val host6 = authority6.host
        assertEquals("example.com", host6.toString())
        assertEquals("example.com", host6!!.value)
        assertEquals(HostType.REGNAME, host6.type)
        assertEquals(-1, authority6.port)
        assertEquals("", uriRef6.path)
        assertEquals(null, uriRef6.query)
        assertEquals(null, uriRef6.fragment)

        val uriRef7 = fromURIReference("http://101.102.103.104").build()

        assertEquals("http://101.102.103.104", uriRef7.toString())
        assertEquals(false, uriRef7.isRelativeReference)
        assertEquals(true, uriRef7.hasAuthority())
        assertEquals("http", uriRef7.scheme)
        val authority7 = uriRef7.authority
        assertEquals("101.102.103.104", authority7.toString())
        assertEquals(null, authority7!!.userinfo)
        val host7 = authority7.host
        assertEquals("101.102.103.104", host7.toString())
        assertEquals("101.102.103.104", host7!!.value)
        assertEquals(HostType.IPV4, host7.type)
        assertEquals(-1, authority7.port)
        assertEquals("", uriRef7.path)
        assertEquals(null, uriRef7.query)
        assertEquals(null, uriRef7.fragment)

        val uriRef8 = fromURIReference("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").build()

        assertEquals("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.toString())
        assertEquals(false, uriRef8.isRelativeReference)
        assertEquals(true, uriRef8.hasAuthority())
        assertEquals("http", uriRef8.scheme)
        val authority8 = uriRef8.authority
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority8.toString())
        assertEquals(null, authority8!!.userinfo)
        val host8 = authority8.host
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8.toString())
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8!!.value)
        assertEquals(HostType.IPV6, host8.type)
        assertEquals(-1, authority8.port)
        assertEquals("", uriRef8.path)
        assertEquals(null, uriRef8.query)
        assertEquals(null, uriRef8.fragment)

        val uriRef9 = fromURIReference("http://[2001:db8:0:1:1:1:1:1]").build()

        assertEquals("http://[2001:db8:0:1:1:1:1:1]", uriRef9.toString())
        assertEquals(false, uriRef9.isRelativeReference)
        assertEquals(true, uriRef9.hasAuthority())
        assertEquals("http", uriRef9.scheme)
        val authority9 = uriRef9.authority
        assertEquals("[2001:db8:0:1:1:1:1:1]", authority9.toString())
        assertEquals(null, authority9!!.userinfo)
        val host9 = authority9.host
        assertEquals("[2001:db8:0:1:1:1:1:1]", host9.toString())
        assertEquals("[2001:db8:0:1:1:1:1:1]", host9!!.value)
        assertEquals(HostType.IPV6, host9.type)
        assertEquals(-1, authority9.port)
        assertEquals("", uriRef9.path)
        assertEquals(null, uriRef9.query)
        assertEquals(null, uriRef9.fragment)

        val uriRef10 = fromURIReference("http://[2001:0:9d38:6abd:0:0:0:42]").build()

        assertEquals("http://[2001:0:9d38:6abd:0:0:0:42]", uriRef10.toString())
        assertEquals(false, uriRef10.isRelativeReference)
        assertEquals(true, uriRef10.hasAuthority())
        assertEquals("http", uriRef10.scheme)
        val authority10 = uriRef10.authority
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", authority10.toString())
        assertEquals(null, authority10!!.userinfo)
        val host10 = authority10.host
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10.toString())
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10!!.value)
        assertEquals(HostType.IPV6, host10.type)
        assertEquals(-1, authority10.port)
        assertEquals("", uriRef10.path)
        assertEquals(null, uriRef10.query)
        assertEquals(null, uriRef10.fragment)

        val uriRef11 = fromURIReference("http://[fe80::1]").build()

        assertEquals("http://[fe80::1]", uriRef11.toString())
        assertEquals(false, uriRef11.isRelativeReference)
        assertEquals(true, uriRef11.hasAuthority())
        assertEquals("http", uriRef11.scheme)
        val authority11 = uriRef11.authority
        assertEquals("[fe80::1]", authority11.toString())
        assertEquals(null, authority11!!.userinfo)
        val host11 = authority11.host
        assertEquals("[fe80::1]", host11.toString())
        assertEquals("[fe80::1]", host11!!.value)
        assertEquals(HostType.IPV6, host11.type)
        assertEquals(-1, authority11.port)
        assertEquals("", uriRef11.path)
        assertEquals(null, uriRef11.query)
        assertEquals(null, uriRef11.fragment)

        val uriRef12 = fromURIReference("http://[2001:0:3238:DFE1:63::FEFB]").build()

        assertEquals("http://[2001:0:3238:DFE1:63::FEFB]", uriRef12.toString())
        assertEquals(false, uriRef12.isRelativeReference)
        assertEquals(true, uriRef12.hasAuthority())
        assertEquals("http", uriRef12.scheme)
        val authority12 = uriRef12.authority
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", authority12.toString())
        assertEquals(null, authority12!!.userinfo)
        val host12 = authority12.host
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12.toString())
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12!!.value)
        assertEquals(HostType.IPV6, host12.type)
        assertEquals(-1, authority12.port)
        assertEquals("", uriRef12.path)
        assertEquals(null, uriRef12.query)
        assertEquals(null, uriRef12.fragment)

        val uriRef13 = fromURIReference("http://[v1.fe80::a+en1]").build()

        assertEquals("http://[v1.fe80::a+en1]", uriRef13.toString())
        assertEquals(false, uriRef13.isRelativeReference)
        assertEquals(true, uriRef13.hasAuthority())
        assertEquals("http", uriRef13.scheme)
        val authority13 = uriRef13.authority
        assertEquals("[v1.fe80::a+en1]", authority13.toString())
        assertEquals(null, authority13!!.userinfo)
        val host13 = authority13.host
        assertEquals("[v1.fe80::a+en1]", host13.toString())
        assertEquals("[v1.fe80::a+en1]", host13!!.value)
        assertEquals(HostType.IPVFUTURE, host13.type)
        assertEquals(-1, authority13.port)
        assertEquals("", uriRef13.path)
        assertEquals(null, uriRef13.query)
        assertEquals(null, uriRef13.fragment)

        val uriRef14 = fromURIReference("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").build()

        assertEquals("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.toString())
        assertEquals(false, uriRef14.isRelativeReference)
        assertEquals(true, uriRef14.hasAuthority())
        assertEquals("http", uriRef14.scheme)
        val authority14 = uriRef14.authority
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", authority14.toString())
        assertEquals(null, authority14!!.userinfo)
        val host14 = authority14.host
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14.toString())
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14!!.value)
        assertEquals(HostType.REGNAME, host14.type)
        assertEquals(-1, authority14.port)
        assertEquals("", uriRef14.path)
        assertEquals(null, uriRef14.query)
        assertEquals(null, uriRef14.fragment)

        val uriRef15 = fromURIReference("http://").build()

        assertEquals(false, uriRef15.isRelativeReference)
        assertEquals(true, uriRef15.hasAuthority())
        assertEquals("http", uriRef15.scheme)
        val authority15 = uriRef15.authority
        assertEquals(null, authority15!!.userinfo)
        val host15 = authority15.host
        assertEquals("", host15!!.value)
        assertEquals("", host15.toString())
        assertEquals(HostType.REGNAME, host15.type)
        assertEquals(-1, authority15.port)
        assertEquals("", uriRef15.path)
        assertEquals(null, uriRef15.query)
        assertEquals(null, uriRef15.fragment)

        val uriRef16 = fromURIReference("http:///a").build()

        assertEquals(false, uriRef16.isRelativeReference)
        assertEquals(true, uriRef16.hasAuthority())
        assertEquals("http", uriRef16.scheme)
        val authority16 = uriRef16.authority
        assertEquals(null, authority16!!.userinfo)
        val host16 = authority16.host
        assertEquals("", host16.toString())
        assertEquals("", host16!!.value)
        assertEquals(HostType.REGNAME, host16.type)
        assertEquals(-1, authority16.port)
        assertEquals("/a", uriRef16.path)
        assertEquals(null, uriRef16.query)
        assertEquals(null, uriRef16.fragment)

        val uriRef17 = fromURIReference("http://example.com:80").build()

        assertEquals(false, uriRef17.isRelativeReference)
        assertEquals(true, uriRef17.hasAuthority())
        assertEquals("http", uriRef17.scheme)
        val authority17 = uriRef17.authority
        assertEquals(null, authority17!!.userinfo)
        val host17 = authority17.host
        assertEquals("example.com", host17.toString())
        assertEquals("example.com", host17!!.value)
        assertEquals(HostType.REGNAME, host17.type)
        assertEquals(80, authority17.port)
        assertEquals("", uriRef17.path)
        assertEquals(null, uriRef17.query)
        assertEquals(null, uriRef17.fragment)

        val uriRef18 = fromURIReference("http://example.com:").build()

        assertEquals(false, uriRef18.isRelativeReference)
        assertEquals(true, uriRef18.hasAuthority())
        assertEquals("http", uriRef18.scheme)
        val authority18 = uriRef18.authority
        assertEquals(null, authority18!!.userinfo)
        val host18 = authority18.host
        assertEquals("example.com", host18.toString())
        assertEquals("example.com", host18!!.value)
        assertEquals(HostType.REGNAME, host18.type)
        assertEquals(-1, authority18.port)
        assertEquals("", uriRef18.path)
        assertEquals(null, uriRef18.query)
        assertEquals(null, uriRef18.fragment)

        val uriRef19 = fromURIReference("http://example.com:001").build()

        assertEquals(false, uriRef19.isRelativeReference)
        assertEquals(true, uriRef19.hasAuthority())
        assertEquals("http", uriRef19.scheme)
        val authority19 = uriRef19.authority
        assertEquals(null, authority19!!.userinfo)
        val host19 = authority19.host
        assertEquals("example.com", host19.toString())
        assertEquals("example.com", host19!!.value)
        assertEquals(HostType.REGNAME, host19.type)
        assertEquals(1, authority19.port)
        assertEquals("", uriRef19.path)
        assertEquals(null, uriRef19.query)
        assertEquals(null, uriRef19.fragment)

        val uriRef20 = fromURIReference("http://example.com/a/b/c").build()

        assertEquals("http://example.com/a/b/c", uriRef20.toString())
        assertEquals(false, uriRef20.isRelativeReference)
        assertEquals(true, uriRef20.hasAuthority())
        assertEquals("http", uriRef20.scheme)
        val authority20 = uriRef20.authority
        assertEquals("example.com", authority20.toString())
        assertEquals(null, authority20!!.userinfo)
        val host20 = authority20.host
        assertEquals("example.com", host20.toString())
        assertEquals("example.com", host20!!.value)
        assertEquals(HostType.REGNAME, host20.type)
        assertEquals(-1, authority20.port)
        assertEquals("/a/b/c", uriRef20.path)
        assertEquals(null, uriRef20.query)
        assertEquals(null, uriRef20.fragment)

        val uriRef21 = fromURIReference("http://example.com/%61/%62/%63").build()

        assertEquals("http://example.com/%61/%62/%63", uriRef21.toString())
        assertEquals(false, uriRef21.isRelativeReference)
        assertEquals(true, uriRef21.hasAuthority())
        assertEquals("http", uriRef21.scheme)
        val authority21 = uriRef21.authority
        assertEquals("example.com", authority21.toString())
        assertEquals(null, authority21!!.userinfo)
        val host21 = authority21.host
        assertEquals("example.com", host21.toString())
        assertEquals("example.com", host21!!.value)
        assertEquals(HostType.REGNAME, host21.type)
        assertEquals(-1, authority21.port)
        assertEquals("/%61/%62/%63", uriRef21.path)
        assertEquals(null, uriRef21.query)
        assertEquals(null, uriRef21.fragment)

        val uriRef22 = fromURIReference("http:/a").setAuthorityRequired(false).build()

        assertEquals(false, uriRef22.isRelativeReference)
        assertEquals(false, uriRef22.hasAuthority())
        assertEquals("http", uriRef22.scheme)
        assertEquals(null, uriRef22.authority)
        assertEquals("/a", uriRef22.path)
        assertEquals(null, uriRef22.query)
        assertEquals(null, uriRef22.fragment)

        val uriRef23 = fromURIReference("http:a").setAuthorityRequired(false).build()

        assertEquals(false, uriRef23.isRelativeReference)
        assertEquals(false, uriRef23.hasAuthority())
        assertEquals("http", uriRef23.scheme)
        assertEquals(null, uriRef23.authority)
        assertEquals("a", uriRef23.path)
        assertEquals(null, uriRef23.query)
        assertEquals(null, uriRef23.fragment)

        val uriRef24 = fromURIReference("//").build()

        assertEquals(true, uriRef24.isRelativeReference)
        assertEquals(true, uriRef24.hasAuthority())
        assertEquals(null, uriRef24.scheme)
        val authority24 = uriRef24.authority
        assertEquals("", authority24.toString())
        assertEquals(null, authority24!!.userinfo)
        val host24 = authority24.host
        assertEquals("", host24.toString())
        assertEquals("", host24!!.value)
        assertEquals(HostType.REGNAME, host24.type)
        assertEquals(-1, authority24.port)
        assertEquals("", uriRef24.path)
        assertEquals(null, uriRef24.query)
        assertEquals(null, uriRef24.fragment)

        assertThrowsNPE<Throwable>(
            "The input string must not be null.",
            { fromURIReference(null as String).build() })
    }


    @Test
    fun test_fromURIReference_with_uriReference() {
        val uriRef1 = fromURIReference(parse("http://example.com").toString()).build()

        assertEquals("http://example.com", uriRef1.toString())
        assertEquals(false, uriRef1.isRelativeReference)
        assertEquals(true, uriRef1.hasAuthority())
        assertEquals("http", uriRef1.scheme)
        val authority1 = uriRef1.authority
        assertEquals("example.com", authority1.toString())
        assertEquals(null, authority1!!.userinfo)
        val host1 = authority1.host
        assertEquals("example.com", host1.toString())
        assertEquals("example.com", host1!!.value)
        assertEquals(HostType.REGNAME, host1.type)
        assertEquals(-1, authority1.port)
        assertEquals("", uriRef1.path)
        assertEquals(null, uriRef1.query)
        assertEquals(null, uriRef1.fragment)

        val uriRef2 = fromURIReference(parse("hTTp://example.com").toString()).build()
        assertEquals("hTTp://example.com", uriRef2.toString())
        assertEquals(false, uriRef2.isRelativeReference)
        assertEquals(true, uriRef2.hasAuthority())
        assertEquals("hTTp", uriRef2.scheme)
        val authority2 = uriRef2.authority
        assertEquals("example.com", authority2.toString())
        assertEquals(null, authority2!!.userinfo)
        val host2 = authority2.host
        assertEquals("example.com", host2.toString())
        assertEquals("example.com", host2!!.value)
        assertEquals(HostType.REGNAME, host2.type)
        assertEquals(-1, authority2.port)
        assertEquals("", uriRef2.path)
        assertEquals(null, uriRef2.query)
        assertEquals(null, uriRef2.fragment)

        val uriRef3 = fromURIReference(parse("//example.com").toString()).build()
        assertEquals("//example.com", uriRef3.toString())
        assertEquals(true, uriRef3.isRelativeReference)
        assertEquals(true, uriRef3.hasAuthority())
        assertEquals(null, uriRef3.scheme)
        val authority = uriRef3.authority
        assertEquals("example.com", authority.toString())
        assertEquals(null, authority!!.userinfo)
        val host = authority.host
        assertEquals("example.com", host.toString())
        assertEquals("example.com", host!!.value)
        assertEquals(HostType.REGNAME, host.type)
        assertEquals(-1, authority.port)
        assertEquals("", uriRef3.path)
        assertEquals(null, uriRef3.query)
        assertEquals(null, uriRef3.fragment)

        val uriRef4 = fromURIReference(parse("http:").toString()).setAuthorityRequired(false).build()
        assertEquals(false, uriRef4.isRelativeReference)
        assertEquals(false, uriRef4.hasAuthority())
        assertEquals("http", uriRef4.scheme)
        assertEquals(false, uriRef4.hasAuthority())
        assertEquals(null, uriRef4.authority)
        assertEquals("", uriRef4.path)
        assertEquals(null, uriRef4.query)
        assertEquals(null, uriRef4.fragment)

        val uriRef5 = fromURIReference(parse("http://john@example.com").toString()).build()
        assertEquals("http://john@example.com", uriRef5.toString())
        assertEquals(false, uriRef5.isRelativeReference)
        assertEquals(true, uriRef5.hasAuthority())
        assertEquals("http", uriRef5.scheme)
        val authority5 = uriRef5.authority
        assertEquals("john@example.com", authority5.toString())
        assertEquals("john", authority5!!.userinfo)
        val host5 = authority5.host
        assertEquals("example.com", host5.toString())
        assertEquals("example.com", host5!!.value)
        assertEquals(HostType.REGNAME, host5.type)
        assertEquals(-1, authority5.port)
        assertEquals("", uriRef5.path)
        assertEquals(null, uriRef5.query)
        assertEquals(null, uriRef5.fragment)

        val uriRef6 = fromURIReference(parse("http://%6A%6F%68%6E@example.com").toString()).build()
        assertEquals("http://%6A%6F%68%6E@example.com", uriRef6.toString())
        assertEquals(false, uriRef6.isRelativeReference)
        assertEquals(true, uriRef6.hasAuthority())
        assertEquals("http", uriRef6.scheme)
        val authority6 = uriRef6.authority
        assertEquals("%6A%6F%68%6E@example.com", authority6.toString())
        assertEquals("%6A%6F%68%6E", authority6!!.userinfo)
        val host6 = authority6.host
        assertEquals("example.com", host6.toString())
        assertEquals("example.com", host6!!.value)
        assertEquals(HostType.REGNAME, host6.type)
        assertEquals(-1, authority6.port)
        assertEquals("", uriRef6.path)
        assertEquals(null, uriRef6.query)
        assertEquals(null, uriRef6.fragment)

        val uriRef7 = fromURIReference(parse("http://101.102.103.104").toString()).build()
        assertEquals("http://101.102.103.104", uriRef7.toString())
        assertEquals(false, uriRef7.isRelativeReference)
        assertEquals(true, uriRef7.hasAuthority())
        assertEquals("http", uriRef7.scheme)
        val authority7 = uriRef7.authority
        assertEquals("101.102.103.104", authority7.toString())
        assertEquals(null, authority7!!.userinfo)
        val host7 = authority7.host
        assertEquals("101.102.103.104", host7.toString())
        assertEquals("101.102.103.104", host7!!.value)
        assertEquals(HostType.IPV4, host7.type)
        assertEquals(-1, authority7.port)
        assertEquals("", uriRef7.path)
        assertEquals(null, uriRef7.query)
        assertEquals(null, uriRef7.fragment)

        val uriRef8 = fromURIReference(parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]").toString()).build()
        assertEquals("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef8.toString())
        assertEquals(false, uriRef8.isRelativeReference)
        assertEquals(true, uriRef8.hasAuthority())
        assertEquals("http", uriRef8.scheme)
        val authority8 = uriRef8.authority
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", authority8.toString())
        assertEquals(null, authority8!!.userinfo)
        val host8 = authority8.host
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8.toString())
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", host8!!.value)
        assertEquals(HostType.IPV6, host8.type)
        assertEquals(-1, authority8.port)
        assertEquals("", uriRef8.path)
        assertEquals(null, uriRef8.query)
        assertEquals(null, uriRef8.fragment)

        val uriRef9 = fromURIReference(parse("http://[2001:db8:0:1:1:1:1:1]").toString()).build()
        assertEquals("http://[2001:db8:0:1:1:1:1:1]", uriRef9.toString())
        assertEquals(false, uriRef9.isRelativeReference)
        assertEquals(true, uriRef9.hasAuthority())
        assertEquals("http", uriRef9.scheme)
        val authority9 = uriRef9.authority
        assertEquals("[2001:db8:0:1:1:1:1:1]", authority9.toString())
        assertEquals(null, authority9!!.userinfo)
        val host9 = authority9.host
        assertEquals("[2001:db8:0:1:1:1:1:1]", host9.toString())
        assertEquals("[2001:db8:0:1:1:1:1:1]", host9!!.value)
        assertEquals(HostType.IPV6, host9.type)
        assertEquals(-1, authority9.port)
        assertEquals("", uriRef9.path)
        assertEquals(null, uriRef9.query)
        assertEquals(null, uriRef9.fragment)

        val uriRef10 = fromURIReference(parse("http://[2001:0:9d38:6abd:0:0:0:42]").toString()).build()
        assertEquals("http://[2001:0:9d38:6abd:0:0:0:42]", uriRef10.toString())
        assertEquals(false, uriRef10.isRelativeReference)
        assertEquals(true, uriRef10.hasAuthority())
        assertEquals("http", uriRef10.scheme)
        val authority10 = uriRef10.authority
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", authority10.toString())
        assertEquals(null, authority10!!.userinfo)
        val host10 = authority10.host
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10.toString())
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", host10!!.value)
        assertEquals(HostType.IPV6, host10.type)
        assertEquals(-1, authority10.port)
        assertEquals("", uriRef10.path)
        assertEquals(null, uriRef10.query)
        assertEquals(null, uriRef10.fragment)

        val uriRef11 = fromURIReference(parse("http://[fe80::1]").toString()).build()
        assertEquals("http://[fe80::1]", uriRef11.toString())
        assertEquals(false, uriRef11.isRelativeReference)
        assertEquals(true, uriRef11.hasAuthority())
        assertEquals("http", uriRef11.scheme)
        val authority11 = uriRef11.authority
        assertEquals("[fe80::1]", authority11.toString())
        assertEquals(null, authority11!!.userinfo)
        val host11 = authority11.host
        assertEquals("[fe80::1]", host11.toString())
        assertEquals("[fe80::1]", host11!!.value)
        assertEquals(HostType.IPV6, host11.type)
        assertEquals(-1, authority11.port)
        assertEquals("", uriRef11.path)
        assertEquals(null, uriRef11.query)
        assertEquals(null, uriRef11.fragment)

        val uriRef12 = fromURIReference(parse("http://[2001:0:3238:DFE1:63::FEFB]").toString()).build()
        assertEquals("http://[2001:0:3238:DFE1:63::FEFB]", uriRef12.toString())
        assertEquals(false, uriRef12.isRelativeReference)
        assertEquals(true, uriRef12.hasAuthority())
        assertEquals("http", uriRef12.scheme)
        val authority12 = uriRef12.authority
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", authority12.toString())
        assertEquals(null, authority12!!.userinfo)
        val host12 = authority12.host
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12.toString())
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", host12!!.value)
        assertEquals(HostType.IPV6, host12.type)
        assertEquals(-1, authority12.port)
        assertEquals("", uriRef12.path)
        assertEquals(null, uriRef12.query)
        assertEquals(null, uriRef12.fragment)

        val uriRef13 = fromURIReference(parse("http://[v1.fe80::a+en1]").toString()).build()
        assertEquals("http://[v1.fe80::a+en1]", uriRef13.toString())
        assertEquals(false, uriRef13.isRelativeReference)
        assertEquals(true, uriRef13.hasAuthority())
        assertEquals("http", uriRef13.scheme)
        val authority13 = uriRef13.authority
        assertEquals("[v1.fe80::a+en1]", authority13.toString())
        assertEquals(null, authority13!!.userinfo)
        val host13 = authority13.host
        assertEquals("[v1.fe80::a+en1]", host13.toString())
        assertEquals("[v1.fe80::a+en1]", host13!!.value)
        assertEquals(HostType.IPVFUTURE, host13.type)
        assertEquals(-1, authority13.port)
        assertEquals("", uriRef13.path)
        assertEquals(null, uriRef13.query)
        assertEquals(null, uriRef13.fragment)

        val uriRef14 = fromURIReference(parse("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D").toString()).build()
        assertEquals("http://%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef14.toString())
        assertEquals(false, uriRef14.isRelativeReference)
        assertEquals(true, uriRef14.hasAuthority())
        assertEquals("http", uriRef14.scheme)
        val authority14 = uriRef14.authority
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", authority14.toString())
        assertEquals(null, authority14!!.userinfo)
        val host14 = authority14.host
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14.toString())
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", host14!!.value)
        assertEquals(HostType.REGNAME, host14.type)
        assertEquals(-1, authority14.port)
        assertEquals("", uriRef14.path)
        assertEquals(null, uriRef14.query)
        assertEquals(null, uriRef14.fragment)

        val uriRef15 = fromURIReference(parse("http://").toString()).build()
        assertEquals(false, uriRef15.isRelativeReference)
        assertEquals(true, uriRef15.hasAuthority())
        assertEquals("http", uriRef15.scheme)
        val authority15 = uriRef15.authority
        assertEquals(null, authority15!!.userinfo)
        val host15 = authority15.host
        assertEquals("", host15?.value)
        assertEquals("", host15.toString())
        assertEquals(HostType.REGNAME, host15?.type)
        assertEquals(-1, authority15.port)
        assertEquals("", uriRef15.path)
        assertEquals(null, uriRef15.query)
        assertEquals(null, uriRef15.fragment)

        val uriRef16 = fromURIReference(parse("http:///a").toString()).build()
        assertEquals(false, uriRef16.isRelativeReference)
        assertEquals(true, uriRef16.hasAuthority())
        assertEquals("http", uriRef16.scheme)
        val authority16 = uriRef16.authority
        assertEquals(null, authority16!!.userinfo)
        val host16 = authority16.host
        assertEquals("", host16.toString())
        assertEquals("", host16!!.value)
        assertEquals(HostType.REGNAME, host16.type)
        assertEquals(-1, authority16.port)
        assertEquals("/a", uriRef16.path)
        assertEquals(null, uriRef16.query)
        assertEquals(null, uriRef16.fragment)

        val uriRef17 = fromURIReference(parse("http://example.com:80").toString()).build()
        assertEquals(false, uriRef17.isRelativeReference)
        assertEquals(true, uriRef17.hasAuthority())
        assertEquals("http", uriRef17.scheme)
        val authority17 = uriRef17.authority
        assertEquals(null, authority17!!.userinfo)
        val host17 = authority17.host
        assertEquals("example.com", host17.toString())
        assertEquals("example.com", host17!!.value)
        assertEquals(HostType.REGNAME, host17.type)
        assertEquals(80, authority17.port)
        assertEquals("", uriRef17.path)
        assertEquals(null, uriRef17.query)
        assertEquals(null, uriRef17.fragment)

        val uriRef18 = fromURIReference(parse("http://example.com:").toString()).build()
        assertEquals(false, uriRef18.isRelativeReference)
        assertEquals(true, uriRef18.hasAuthority())
        assertEquals("http", uriRef18.scheme)
        val authority18 = uriRef18.authority
        assertEquals(null, authority18!!.userinfo)
        val host18 = authority18.host
        assertEquals("example.com", host18.toString())
        assertEquals("example.com", host18!!.value)
        assertEquals(HostType.REGNAME, host18.type)
        assertEquals(-1, authority18.port)
        assertEquals("", uriRef18.path)
        assertEquals(null, uriRef18.query)
        assertEquals(null, uriRef18.fragment)

        val uriRef19 = fromURIReference(parse("http://example.com:001").toString()).build()
        assertEquals(false, uriRef19.isRelativeReference)
        assertEquals(true, uriRef19.hasAuthority())
        assertEquals("http", uriRef19.scheme)
        val authority19 = uriRef19.authority
        assertEquals(null, authority19!!.userinfo)
        val host19 = authority19.host
        assertEquals("example.com", host19.toString())
        assertEquals("example.com", host19!!.value)
        assertEquals(HostType.REGNAME, host19.type)
        assertEquals(1, authority19.port)
        assertEquals("", uriRef19.path)
        assertEquals(null, uriRef19.query)
        assertEquals(null, uriRef19.fragment)

        val uriRef20 = fromURIReference(parse("http://example.com/a/b/c").toString()).build()
        assertEquals("http://example.com/a/b/c", uriRef20.toString())
        assertEquals(false, uriRef20.isRelativeReference)
        assertEquals(true, uriRef20.hasAuthority())
        assertEquals("http", uriRef20.scheme)
        val authority20 = uriRef20.authority
        assertEquals("example.com", authority20.toString())
        assertEquals(null, authority20!!.userinfo)
        val host20 = authority20.host
        assertEquals("example.com", host20.toString())
        assertEquals("example.com", host20!!.value)
        assertEquals(HostType.REGNAME, host20.type)
        assertEquals(-1, authority20.port)
        assertEquals("/a/b/c", uriRef20.path)
        assertEquals(null, uriRef20.query)
        assertEquals(null, uriRef20.fragment)

        val uriRef21 = fromURIReference(parse("http://example.com/%61/%62/%63").toString()).build()
        assertEquals("http://example.com/%61/%62/%63", uriRef21.toString())
        assertEquals(false, uriRef21.isRelativeReference)
        assertEquals(true, uriRef21.hasAuthority())
        assertEquals("http", uriRef21.scheme)
        val authority21 = uriRef21.authority
        assertEquals("example.com", authority21.toString())
        assertEquals(null, authority21!!.userinfo)
        val host21 = authority21.host
        assertEquals("example.com", host21.toString())
        assertEquals("example.com", host21!!.value)
        assertEquals(HostType.REGNAME, host21.type)
        assertEquals(-1, authority21.port)
        assertEquals("/%61/%62/%63", uriRef21.path)
        assertEquals(null, uriRef21.query)
        assertEquals(null, uriRef21.fragment)

        val uriRef22 = fromURIReference(parse("http:/a").toString()).setAuthorityRequired(false).build()
        assertEquals(false, uriRef22.isRelativeReference)
        assertEquals(false, uriRef22.hasAuthority())
        assertEquals("http", uriRef22.scheme)
        assertEquals(null, uriRef22.authority)
        assertEquals("/a", uriRef22.path)
        assertEquals(null, uriRef22.query)
        assertEquals(null, uriRef22.fragment)

        val uriRef23 = fromURIReference(parse("http:a").toString()).setAuthorityRequired(false).build()
        assertEquals(false, uriRef23.isRelativeReference)
        assertEquals(false, uriRef23.hasAuthority())
        assertEquals("http", uriRef23.scheme)
        assertEquals(null, uriRef23.authority)
        assertEquals("a", uriRef23.path)
        assertEquals(null, uriRef23.query)
        assertEquals(null, uriRef23.fragment)

        val uriRef24 = fromURIReference(parse("//").toString()).build()
        assertEquals(true, uriRef24.isRelativeReference)
        assertEquals(true, uriRef24.hasAuthority())
        assertEquals(null, uriRef24.scheme)
        val authority24 = uriRef24.authority
        assertEquals("", authority24.toString())
        assertEquals(null, authority24?.userinfo)
        val host24 = authority24?.host
        assertEquals("", host24.toString())
        assertEquals("", host24?.value)
        assertEquals(HostType.REGNAME, host24?.type)
        assertEquals(-1, authority24?.port)
        assertEquals("", uriRef24.path)
        assertEquals(null, uriRef24.query)
        assertEquals(null, uriRef24.fragment)

        assertThrowsNPE<Throwable>(
            "The input string must not be null.",
            { fromURIReference(parse(null)) })
    }


    @Test
    fun test_setAuthorityRequired() {
        val uriRef1 = fromURIReference("http://example.com")
            .setAuthorityRequired(true)
            .build()
        assertEquals("http://example.com", uriRef1.toString())
        assertEquals(false, uriRef1.isRelativeReference)
        assertEquals(true, uriRef1.hasAuthority())
        assertEquals("http", uriRef1.scheme)
        val authority1 = uriRef1.authority
        assertEquals("example.com", authority1.toString())
        assertEquals(null, authority1?.userinfo)
        val host1 = authority1?.host
        assertEquals("example.com", host1.toString())
        assertEquals("example.com", host1?.value)
        assertEquals(HostType.REGNAME, host1?.type)
        assertEquals(-1, authority1?.port)
        assertEquals("", uriRef1.path)
        assertEquals(null, uriRef1.query)
        assertEquals(null, uriRef1.fragment)

        val uriRef2 = fromURIReference("http://example.com")
            .setAuthorityRequired(false)
            .build()
        assertEquals("http:", uriRef2.toString())
        assertEquals(false, uriRef2.isRelativeReference)
        assertEquals(false, uriRef2.hasAuthority())
        assertEquals("http", uriRef2.scheme)
        assertEquals(null, uriRef2.authority)
        assertEquals("", uriRef1.path)
        assertEquals(null, uriRef1.query)
        assertEquals(null, uriRef1.fragment)
    }


    @Test
    fun test_setScheme() {
        val uriRef1 = fromURIReference("http://example.com")
            .setScheme("ftp")
            .build()
        assertEquals("ftp", uriRef1.scheme)

        val uriRef2 = fromURIReference("http://example.com")
            .setScheme("https")
            .build()
        assertEquals("https", uriRef2.scheme)

        val uriRef3 = fromURIReference("http://example.com")
            .setScheme(null)
            .build()
        assertEquals(null, uriRef3.scheme)
    }


    @Test
    fun test_setHost() {
        val uriRef1 = fromURIReference("http://example.com")
            .setHost("example2.com")
            .build()
        assertEquals(HostType.REGNAME, uriRef1.host?.type)
        assertEquals("example2.com", uriRef1.host?.value)

        val uriRef2 = fromURIReference("http://example.com")
            .setHost("101.102.103.104")
            .build()
        assertEquals(HostType.IPV4, uriRef2.host?.type)
        assertEquals("101.102.103.104", uriRef2.host?.value)

        val uriRef3 = fromURIReference("http://example.com")
            .setHost("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]")
            .build()
        assertEquals(HostType.IPV6, uriRef3.host?.type)
        assertEquals("[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]", uriRef3.host?.value)

        val uriRef4 = fromURIReference("http://example.com")
            .setHost("[2001:db8:0:1:1:1:1:1]")
            .build()
        assertEquals(HostType.IPV6, uriRef4.host?.type)
        assertEquals("[2001:db8:0:1:1:1:1:1]", uriRef4.host?.value)

        val uriRef5 = fromURIReference("http://example.com")
            .setHost("[2001:0:9d38:6abd:0:0:0:42]")
            .build()
        assertEquals(HostType.IPV6, uriRef5.host?.type)
        assertEquals("[2001:0:9d38:6abd:0:0:0:42]", uriRef5.host?.value)

        val uriRef6 = fromURIReference("http://example.com")
            .setHost("[fe80::1]")
            .build()
        assertEquals(HostType.IPV6, uriRef6.host?.type)
        assertEquals("[fe80::1]", uriRef6.host?.value)

        val uriRef7 = fromURIReference("http://example.com")
            .setHost("[2001:0:3238:DFE1:63::FEFB]")
            .build()
        assertEquals(HostType.IPV6, uriRef7.host?.type)
        assertEquals("[2001:0:3238:DFE1:63::FEFB]", uriRef7.host?.value)

        val uriRef8 = fromURIReference("http://example.com")
            .setHost("[v1.fe80::a+en1]")
            .build()
        assertEquals(HostType.IPVFUTURE, uriRef8.host?.type)
        assertEquals("[v1.fe80::a+en1]", uriRef8.host?.value)

        val uriRef9 = fromURIReference("http://example.com")
            .setHost("%65%78%61%6D%70%6C%65%2E%63%6F%6D")
            .build()
        assertEquals(HostType.REGNAME, uriRef9.host?.type)
        assertEquals("%65%78%61%6D%70%6C%65%2E%63%6F%6D", uriRef9.host?.value)

        val uriRef10 = fromURIReference("http://example.com")
            .setHost("")
            .build()
        assertEquals(HostType.REGNAME, uriRef10.host?.type)
        assertEquals("", uriRef10.host?.value)

        val uriRef11 = fromURIReference("http://example.com")
            .setHost(null)
            .build()
        assertEquals(HostType.REGNAME, uriRef11.host?.type)
        assertEquals(null, uriRef11.host?.value)
    }


    @Test
    fun test_setPath() {
        val uriRef1 = fromURIReference("http://example.com")
            .setPath("/a")
            .build()
        assertEquals("/a", uriRef1.path)

        val uriRef2 = fromURIReference("http://example.com")
            .setPath("/a/b")
            .build()
        assertEquals("/a/b", uriRef2.path)

        val uriRef3 = fromURIReference("http://example.com")
            .setPath("/")
            .build()
        assertEquals("/", uriRef3.path)

        val uriRef4 = fromURIReference("http://example.com")
            .setPath("")
            .build()
        assertEquals("", uriRef4.path)

        val uriRef5 = fromURIReference("http://example.com")
            .setPath(null)
            .build()
        assertEquals(null, uriRef5.path)
    }


    @Test
    fun test_setPathSegments() {
        val uriRef1 = fromURIReference("http://example.com")
            .appendPathSegments(listOf("a", "b", "c"))
            .build()
        assertEquals("/a/b/c", uriRef1.path)

        val uriRef2 = fromURIReference("http://example.com")
            .appendPathSegments(listOf(""))
            .build()
        assertEquals("/", uriRef2.path)

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
        assertEquals("k=v", uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com")
            .appendQueryParam("k1", "v1")
            .appendQueryParam("k2", "v2")
            .build()
        assertEquals("k1=v1&k2=v2", uriRef2.query)

        val uriRef3 = fromURIReference("http://example.com")
            .appendQueryParam("", "")
            .build()
        assertEquals("=", uriRef3.query)

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
        assertEquals("k=w", uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com?k=v")
            .replaceQueryParam("k", null)
            .build()
        assertEquals("k", uriRef2.query)

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
        assertEquals(null, uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com?k=v")
            .removeQueryParam(null)
            .build()
        assertEquals("k=v", uriRef2.query)
    }


    @Test
    fun test_setQuery() {
        val uriRef1 = fromURIReference("http://example.com").setQuery("k=v").build()
        assertEquals("k=v", uriRef1.query)

        val uriRef2 = fromURIReference("http://example.com").setQuery("k=").build()
        assertEquals("k=", uriRef2.query)

        val uriRef3 = fromURIReference("http://example.com").setQuery("k").build()
        assertEquals("k", uriRef3.query)

        val uriRef4 = fromURIReference("http://example.com").setQuery("").build()
        assertEquals("", uriRef4.query)

        val uriRef5 = fromURIReference("http://example.com").setQuery(null).build()
        assertEquals(null, uriRef5.query)
    }


    @Test
    fun test_setFragment() {
        val uriRef1 = fromURIReference("http://example.com").setFragment("section1").build()
        assertEquals("section1", uriRef1.fragment)

        val uriRef2 = fromURIReference("http://example.com").setFragment("fig%20A").build()
        assertEquals("fig%20A", uriRef2.fragment)

        val uriRef3 = fromURIReference("http://example.com").setFragment("2.3").build()
        assertEquals("2.3", uriRef3.fragment)

        val uriRef4 = fromURIReference("http://example.com").setFragment("").build()
        assertEquals("", uriRef4.fragment)

        val uriRef5 = fromURIReference("http://example.com").setFragment(null).build()
        assertEquals(null, uriRef5.fragment)
    }
}
