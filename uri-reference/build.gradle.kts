import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
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

tasks.withType<Test>().configureEach {
    maxHeapSize = "4g"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}