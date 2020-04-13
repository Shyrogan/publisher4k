plugins {
    java
    kotlin("jvm") version "1.3.71"
    `maven-publish`
}

group = "fr.shyrogan"
version = "1.0.1"
description = "A fast and reliable publisher to subscriber library wrote for Kotlin"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.ow2.asm", "asm-debug-all", "5.2")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}