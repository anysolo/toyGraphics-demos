import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.*

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.1")
    }
}

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

val localProperties = Properties()
val localPropertiesFile: File = rootProject.file("artifactory.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

repositories {
    val artifactory_contextUrl: String by localProperties
    val artifactory_username: String by localProperties
    val artifactory_password: String by localProperties

    jcenter()
    maven(url="https://dl.bintray.com/anysolo/edu")
    maven(url="https://jitpack.io")

    maven {
        url = uri("${artifactory_contextUrl}/gradle-dev")
        credentials {
            username = artifactory_username
            password = artifactory_password
        }
    }
}

dependencies {
    compile(gradleApi())
    compile(kotlin("stdlib"))
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    compile("com.anysolo:toyGraphics:2.0-SNAPSHOT")
    //compile("jfugue:jfugue:4.0.3")
}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = "HelloKt"
    }
}
