# Kotlin Multiplatform URI Library for RFC 3986

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-blue?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlingeekdev/uri-reference-kmp?color=blue)](https://search.maven.org/search?q=g:io.github.kotlingeekdev)

![badge-jvm](http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-linux](http://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat)
![badge-mac](http://img.shields.io/badge/platform-macos-111111.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)

## Overview

This is a Kotlin Multiplatform library for working with [RFC 3986](https://datatracker.ietf.org/doc/rfc3986/), 
and is a fork of the [Java library](https://github.com/hidebike712/uri-reference).

The original author describes some benefits of using the library over Java's URI library
(which could be of interest to JVM/Android consumers) in the [readme](https://github.com/hidebike712/uri-reference?tab=readme-ov-file#overview).

The library offers four key functionalities for robust URI management:

- [Parsing](#parsing)
- [Resolving](#resolving)
- [Normalizing](#normalizing)
- [Constructing](#constructing)

Each feature is designed to handle URIs accurately and effectively, ensuring reliable and precise management across various
application contexts.

## How to include the libary
You can include the library from either Maven Central or Jitpack.

### Maven
You can include the library in the common source set like this:
```kotlin
dependencies {
    implementation("io.github.kotlingeekdev:uri-reference-kmp:1.0")

}
```

### Jitpack
Inside your root-level `build.gradle(.kts)` file, you should add `jitpack`:
  ``` kotlin
// build.gradle.kts
allprojects {
    repositories {
        // ...
        maven { setUrl("https://jitpack.io") }
    }
    // ...
}
```

or

``` groovy
// build.gradle
allprojects {
    repositories {
        // ...
        maven { url "https://jitpack.io" }
    }
    // ...
}
```

In newer projects, you need to also update the `settings.gradle(.kts)` file's `dependencyResolutionManagement` block:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }  // <--
    }
}
```
then, in your module's `build.gradle(.kts)`, you need to add:
```kotlin
// build.gradle.kts
dependencies {
    //...
    implementation("com.github.KotlinGeekDev.uri-reference-kmp:uri-reference:1.0")
    
}

```
If you're including it in an Android app, you can just add:
```kotlin
// app/build.gradle.kts
dependencies {
    //...
    implementation("com.github.KotlinGeekDev.uri-reference-kmp:uri-reference-android:1.0")

}
```

## License

Apache License, Version 2.0

## Usage

<a id="parsing"></a>
### :white_check_mark: Parsing

To parse URI references, use `URIReference.parse(uriRef: String)` or `URIReference.parse(uriRef: String, charset: Charset)`. Below are some examples of using `URIReference.parse(uriRef: String)`.

#### Example 1: Parse Basic URI

```kotlin
val uriRef = URIReference.parse("http://example.com/a/b") // Parse.

println(uriRef.toString())                // "http://example.com/a/b"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "example.com"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "REGNAME"
println(uriRef.getHost().getValue())      // "example.com"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // "/a/b"
println(uriRef.getQuery())                // null
println(uriRef.getFragment())             // null
```

#### Example 2: Parse Relative Reference

```kotlin
val uriRef = URIReference.parse("//example.com/a/b") // Parse.

println(uriRef.toString())                // "//example.com/a/b"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // null
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "example.com"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "REGNAME"
println(uriRef.getHost().getValue())      // "example.com"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // "/a/b"
println(uriRef.getQuery())                // null
println(uriRef.getFragment())             // null
```

#### Example 3: Parse URI with IPV4 Host

```kotlin
val uriRef = URIReference.parse("http://101.102.103.104") // Parse.

println(uriRef.toString())                // "http://101.102.103.104"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "101.102.103.104"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "IPV4"
println(uriRef.getHost().getValue())      // "101.102.103.104"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // ""
println(uriRef.getQuery())                // null
println(uriRef.getFragment())             // null
```

#### Example 4: Parse URI with IPV6 Host

```kotlin
val uriRef = URIReference.parse("http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]") // Parse.

println(uriRef.toString())                // "http://[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "IPV6"
println(uriRef.getHost().getValue())      // "[2001:0db8:0001:0000:0000:0ab9:C0A8:0102]"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // ""
println(uriRef.getQuery())                // null
println(uriRef.getFragment())             // null
```

#### Example 5: Parse URI with IPvFuture Host

```kotlin
val uriRef = URIReference.parse("http://[v9.abc:def]") // Parse.

println(uriRef.toString())                // "http://[v9.abc:def]"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "[v9.abc:def]"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "IPVFUTURE"
println(uriRef.getHost().getValue())      // "[v9.abc:def]"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // ""
println(uriRef.getQuery())                // null
println(uriRef.getFragment())             // null
```

#### Example 6: Parse URI with Percent-encoded Host

```kotlin
val uriRef = URIReference.parse("http://%65%78%61%6D%70%6C%65.com") // Parse.

println(uriRef.toString())                // "http://%65%78%61%6D%70%6C%65.com"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "%65%78%61%6D%70%6C%65.com"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "REGNAME"
println(uriRef.getHost().getValue())      // "%65%78%61%6D%70%6C%65.com"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // ""
println(uriRef.getQuery())                // null
println(uriRef.getFragment())             // null
```

> [!WARNING]
> If parsing fails, those methods throws `NullPointerException` or `IllegalArgumentException`. See [Java doc](https://hidebike712.github.io/uri-reference/) for more details.

---

<a id="resolving"></a>
### :white_check_mark: Resolving

To resolve a relative reference against a URI reference, use `resolve(uriRef: String)` or `resolve(uriRef: URIReference)`. Below is an example demonstrating how to resolve a relative reference against a base URI.

```kotlin
// A base URI.
val baseUri = URIReference.parse("http://example.com")

// A relative reference.
val relRef = URIReference.parse("/a/b")

// Resolve the relative reference against the base URI.
val resolved = baseUri.resolve(relRef)

println(resolved.toString())                // "http://example.com/a/b"
println(resolved.isRelativeReference())     // false
println(resolved.getScheme())               // "http"
println(resolved.hasAuthority())            // true
println(resolved.getAuthority().toString()) // "example.com"
println(resolved.getUserinfo())             // null
println(resolved.getHost().getType())       // "REGNAME"
println(resolved.getHost().getValue())      // "example.com"
println(resolved.getPort())                 // -1
println(resolved.getPath())                 // "/a/b"
println(resolved.getQuery())                // null
println(resolved.getFragment())             // null
```

---

<a id="normalizing"></a>
### :white_check_mark: Normalizing

For normalization, invoke `normalize()` on a `URIReference` instance to normalize.

#### Example 1: Normalize URI with Mixed-Case Scheme

```kotlin
val normalized = URIReference.parse("hTTp://example.com") // Parse.
                                      .normalize()                // Normalize.

println(normalized.toString())                // "http://example.com/"
println(normalized.isRelativeReference())     // false
println(normalized.getScheme())               // "http"
println(normalized.hasAuthority())            // true
println(normalized.getAuthority().toString()) // "example.com"
println(normalized.getUserinfo())             // null
println(normalized.getHost().getType())       // "REGNAME"
println(normalized.getHost().getValue())      // "example.com"
println(normalized.getPort())                 // -1
println(normalized.getPath())                 // "/"
println(normalized.getQuery())                // null
println(normalized.getFragment())             // null
```

#### Example 2: Normalize URI with Percent-Encoded Host

```kotlin
val normalized = URIReference.parse("http://%65%78%61%6D%70%6C%65.com") // Parse.
                                      .normalize()                              // Normalize.

println(normalized.toString())                // "http://example.com/"
println(normalized.isRelativeReference())     // false
println(normalized.getScheme())               // "http"
println(normalized.hasAuthority())            // true
println(normalized.getAuthority().toString()) // "example.com"
println(normalized.getUserinfo())             // null
println(normalized.getHost().getType())       // "REGNAME"
println(normalized.getHost().getValue())      // "example.com"
println(normalized.getPort())                 // -1
println(normalized.getPath())                 // "/"
println(normalized.getQuery())                // null
println(normalized.getFragment())             // null
```

#### Example 3: Normalize URI with Unresolved Path

```kotlin
val normalized = URIReference.parse("http://example.com/a/b/c/../d/") // Parse.
                                      .normalize()                            // Normalize.

println(normalized.toString())                // "http://example.com/a/b/d/"
println(normalized.isRelativeReference())     // false
println(normalized.getScheme())               // "http"
println(normalized.hasAuthority())            // true
println(normalized.getAuthority().toString()) // "example.com"
println(normalized.getUserinfo())             // null
println(normalized.getHost().getType())       // "REGNAME"
println(normalized.getHost().getValue())      // "example.com"
println(normalized.getPort())                 // -1
println(normalized.getPath())                 // "/a/b/d/"
println(normalized.getQuery())                // null
println(normalized.getFragment())             // null
```

#### Example 4: Normalize Relative Reference

```kotlin
// Parse a relative reference.
val relRef = URIReference.parse("/a/b/c/../d/")

// Resolve the relative reference against "http://example.com".
// NOTE: Relative references must be resolved before normalization.
val resolved = relRef.resolve("http://example.com")

// Normalize the resolved URI.
val normalized = resolved.normalize()

println(normalized.toString())                // "http://example.com/a/b/d/"
println(normalized.isRelativeReference())     // false
println(normalized.getScheme())               // "http"
println(normalized.hasAuthority())            // true
println(normalized.getAuthority().toString()) // "example.com"
println(normalized.getUserinfo())             // null
println(normalized.getHost().getType())       // "REGNAME"
println(normalized.getHost().getValue())      // "example.com"
println(normalized.getPort())                 // -1
println(normalized.getPath())                 // "/a/b/d/"
println(normalized.getQuery())                // null
println(normalized.getFragment())             // null
```

> [!CAUTION]
> Relative reference must be resolved before normalization as [RFC 3986, 5.2.1](https://datatracker.ietf.org/doc/html/rfc3986#section-5.2.1) states as below.
> > RFC 3986, 5.2.1. Pre-parse the Base URI
> >
> > A URI reference must be transformed to its target URI before
> > it can be normalized.---

<a id="constructing"></a>
### :white_check_mark: Constructing

To construct URI references, use `URIReferenceBuilder` class.

#### Example 1: Construct Basic URI

```kotlin
val uriRef = URIReferenceBuilder()
                          .setScheme("http")
                          .setHost("example.com")
                          .setPath("/a/b/c")
                          .appendQueryParam("k1", "v1")
                          .build()

println(uriRef.toString())                // "http://example.com/a/b/c?k1=v1"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "example.com"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "REGNAME"
println(uriRef.getHost().getValue())      // "example.com"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // "/a/b/c"
println(uriRef.getQuery())                // "k1=v1"
println(uriRef.getFragment())             // null
```

#### Example 2: Construct URI from Existing URI

```kotlin
val uriRef = URIReferenceBuilder()
                          .fromURIReference("http://example.com/a/b/c?k1=v1")
                          .appendPath("d", "e", "f")
                          .appendQueryParam("k2", "v2")
                          .build()

println(uriRef.toString())                // "http://example.comd/a/b/c/d/e/f?k1=v1&k2=v2"
println(uriRef.isRelativeReference())     // false
println(uriRef.getScheme())               // "http"
println(uriRef.hasAuthority())            // true
println(uriRef.getAuthority().toString()) // "example.com"
println(uriRef.getUserinfo())             // null
println(uriRef.getHost().getType())       // "REGNAME"
println(uriRef.getHost().getValue())      // "example.com"
println(uriRef.getPort())                 // -1
println(uriRef.getPath())                 // "/a/b/c/d/e/f"
println(uriRef.getQuery())                // "k1=v1&k2=&v2"
println(uriRef.getFragment())             // null
```

> [!WARNING]
> The current implementation of `URIReferenceBuilder` class won't throw an exception until `build()` method is invoked even if invalid input is given since validation for each URI component is performed only when `build()` is called.

## Note

### :pushpin: Immutable class

This library designs most classes such as `URIReference` to be immutable. Here are some examples.

```kotlin
// Example 1: Invoking the "normalize()" method creates a new URIReference instance.
val normalized = URIReference.parse("hTTp://example.com").normalize()

// Example 2: Invoking the "resolve(String uriRef)" method creates a new URIReference instance.
val resolved = URIReference.parse("http://example.com").resolve("/a/b")
```

## See Also

- [RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax](https://datatracker.ietf.org/doc/rfc3986)
- [RFC 2396 - Uniform Resource Identifiers (URI): Generic Syntax](https://datatracker.ietf.org/doc/rfc2396)
