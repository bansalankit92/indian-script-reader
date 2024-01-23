import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "in.co.ankitbansal"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.projectlombok:lombok")
//    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

// https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox
    implementation("org.apache.pdfbox:pdfbox:2.0.29")
// https://mvnrepository.com/artifact/com.ibm.icu/icu4j
    implementation("com.ibm.icu:icu4j:73.1")
// https://mvnrepository.com/artifact/net.sourceforge.tess4j/tess4j
    implementation("net.sourceforge.tess4j:tess4j:5.8.0")
// https://mvnrepository.com/artifact/com.github.jai-imageio/jai-imageio-core
    implementation("com.github.jai-imageio:jai-imageio-core:1.4.0")
// https://mvnrepository.com/artifact/com.github.jai-imageio/jai-imageio-jpeg2000
    implementation("com.github.jai-imageio:jai-imageio-jpeg2000:1.4.0")
// https://mvnrepository.com/artifact/org.apache.pdfbox/jbig2-imageio
    implementation("org.apache.pdfbox:jbig2-imageio:3.0.4")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
