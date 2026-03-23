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
        namespace = "io.kotlingeekdev.urireference"
        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
        minSdk = 24

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

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "uri-referenceKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
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

        jvmMain.dependencies {

        }
        jvmTest.dependencies {
            val junitJupiterVersion = "5.10.0"
            implementation(kotlin("test-junit5"))

            implementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
            implementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
            implementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
            runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
            runtimeOnly("org.junit.vintage:junit-vintage-engine:$junitJupiterVersion")
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.runner)
                implementation(libs.core)
                implementation(libs.ext.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}