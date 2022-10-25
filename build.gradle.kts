plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    `java-library`
    application
    distribution
    id("com.github.ben-manes.versions") version "0.43.0"
}

repositories {
    mavenCentral()
}

dependencies {
    api("se.michaelthelin.spotify:spotify-web-api-java:7.2.2")
    api("org.apache.logging.log4j:log4j-api:2.19.0")
    api("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("at.rueckgr.spotify.main.DiscoverWeeklyClonerKt")
}

distributions {
    main
}
