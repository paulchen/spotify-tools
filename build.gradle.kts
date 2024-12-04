import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    `java-library`
    application
    distribution
    id("com.github.ben-manes.versions") version "0.51.0"
}

repositories {
    mavenCentral()
}

dependencies {
    api("se.michaelthelin.spotify:spotify-web-api-java:8.4.1")
    api("org.apache.logging.log4j:log4j-api:2.24.2")
    api("org.apache.logging.log4j:log4j-api-kotlin:1.5.0")
    implementation("org.apache.logging.log4j:log4j-core:2.24.2")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.2")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("at.rueckgr.spotify.main.MainKt")
}

distributions {
    main
}
