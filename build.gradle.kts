plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
    `java-library`
}

group "at.rueckgr.spotify"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile("se.michaelthelin.spotify:spotify-web-api-java:6.5.2")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
