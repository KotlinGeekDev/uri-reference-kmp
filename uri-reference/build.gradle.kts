import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.vanniktech.mavenPublish) version libs.versions.mavenPublish
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.android.lint)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvmToolchain(21)

    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }

    android {
        namespace = "io.kotlingeekdev.urireference.android"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        enableCoreLibraryDesugaring = false

        aarMetadata {
            minCompileSdk = libs.versions.android.minSdk.get().toInt()
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    iosArm64()
    iosSimulatorArm64()
    iosX64()
    linuxX64()
    mingwX64()

    macosX64()
    macosArm64()

    applyDefaultHierarchyTemplate()

    val xcfName = "uri-referenceLib"

    iosX64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", "io.kotlingeekdev.urireference")
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", "io.kotlingeekdev.urireference")
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", "io.kotlingeekdev.urireference")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation("com.fleeksoft.io:io:0.0.8")
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(kotlin("test-common"))
            }
        }

        jvmMain {}
        jvmTest.dependencies {
            implementation(kotlin("test-junit5"))
            implementation(libs.org.junit.jupiter.api)
        }

        androidMain {}

        getByName("androidHostTest") {}

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.runner)
                implementation(libs.core)
                implementation(libs.ext.junit)
            }
        }

        appleMain {

        }
    }

}

val isJitpack = System.getenv("JITPACK") == "true"

mavenPublishing {
    configure(
        KotlinMultiplatform(
            sourcesJar = SourcesJar.Sources(),
            androidVariantsToPublish = listOf("release"),
        )
    )
    publishToMavenCentral(automaticRelease = true)
    if (!isJitpack) {
        signAllPublications()
    }

    coordinates(rootProject.group.toString(), rootProject.name, rootProject.version.toString())

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

tasks.withType<Test>().configureEach {
    maxHeapSize = "4g"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}