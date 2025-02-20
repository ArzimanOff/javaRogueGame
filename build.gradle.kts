plugins {
    id("java")
}

group = "org.arzimanoff"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.googlecode.lanterna:lanterna:3.1.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}
java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.test {
    useJUnitPlatform()
}