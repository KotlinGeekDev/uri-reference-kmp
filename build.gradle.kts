import org.gradle.kotlin.dsl.kotlin

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}

dependencies {
    testImplementation(libs.org.junit.jupiter.junit.jupiter.api)
    testImplementation(libs.org.junit.jupiter.junit.jupiter.engine)
    testImplementation(libs.org.junit.jupiter.junit.jupiter.params)
    implementation(kotlin("stdlib"))
}

group = "org.czeal"
version = "1.0.2"
description = "Java URI Library Compliant with RFC 3986"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}