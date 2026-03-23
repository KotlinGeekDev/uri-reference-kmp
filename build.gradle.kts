plugins {

    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.android.lint) apply false
}

group = "io.github.kotlingeekdev"
version = "1.0"
description = "Kotlin Multiplatform URI Library Compliant with RFC 3986"