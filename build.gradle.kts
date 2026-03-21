plugins {
//    `java-library`
    `maven-publish`
    kotlin("multiplatform") version "2.3.10"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
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

//    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    linuxX64()
    mingwX64()

    macosX64()
    macosArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(kotlin("stdlib"))
            implementation("com.fleeksoft.io:io:0.0.8")
        }
        commonTest.dependencies {
            implementation(kotlin("test-common"))
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
    }


}

group = "io.github.kotlingeekdev"
version = "1.0"
description = "Kotlin Multiplatform URI Library Compliant with RFC 3986"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["kotlin"])
    }
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}