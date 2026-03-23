plugins {
    alias(libs.plugins.vanniktech.mavenPublish) version libs.versions.mavenPublish
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.android.lint) apply false
}

allprojects {
    apply(plugin = "com.vanniktech.maven.publish")

    group = "io.github.kotlingeekdev"
    version = "1.0"
    description = "Kotlin Multiplatform URI Library Compliant with RFC 3986"

    val isJitpack = System.getenv("JITPACK") == "true"

    mavenPublishing {
//    configure(
//        KotlinMultiplatform(
//            sourcesJar = SourcesJar.Sources(),
//            androidVariantsToPublish = listOf("release"),
//        )
//    )
        publishToMavenCentral(automaticRelease = true)
        if (!isJitpack) {
            signAllPublications()
        }

        coordinates(group.toString(), rootProject.name, version.toString())

        pom {
            name = "Uri-reference-kmp"
            description = "Kotlin Multiplatform URI Library Compliant with RFC 3986, based on the original Java library."
            url = "https://github.com/KotlinGeekDev/uri-reference-kmp"

            licenses {
                license {
                    name = "Apache License, Version 2.0"
                    url = "https://opensource.org/license/apache-2.0"
                    distribution = "https://opensource.org/license/apache-2.0"
                }
            }

            developers {
                developer {
                    name = "KotlinGeekDev"
                    email = "kotlingeek@protonmail.com"
                    url = "https://github.com/KotlinGeekDev"
                }
                developer {
                    name = "Hideki Ikeda"
                    email = "hidebike712@gmail.com"
                    url = "https://github.com/hidebike712"
                }
            }

            scm {
                connection = "scm:git:git://github.com/KotlinGeekDev/uri-reference-kmp.git"
                url = "https://github.com/KotlinGeekDev/uri-reference-kmp"

            }
        }
    }
}