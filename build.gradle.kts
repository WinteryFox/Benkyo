import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.springframework.boot") version "2.6.6" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("plugin.spring") version "1.6.10" apply false

    id("com.google.cloud.tools.jib") version "3.2.1" apply false
    id("nu.studer.jooq") version "7.1.1"
    kotlin("jvm") version "1.6.10"

    java
}

allprojects {
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
            jvmTarget = "16"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
