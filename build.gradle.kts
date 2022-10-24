@file:Suppress("HardCodedStringLiteral", "UnstableApiUsage")

plugins {
    java
    id("org.springframework.boot").version("2.6.6")
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
}

group = "pl.szymsoft.salon"
version = "0.0.1-SNAPSHOT"

// Java 18 is not unstable, some of its features are, but to use them, one  needs to consciously enable them with '--enable-preview' flag
// A reasonable voice for using the latest(even non LTS) releases on production - https://www.youtube.com/watch?v=2QscBQDs2fY&t=135s
java {
    sourceCompatibility = JavaVersion.VERSION_18
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["testcontainers.version"] = "1.16.3"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainers.version")}")
    }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-validator:commons-validator:1.7")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.javamoney:moneta:1.4.2")
    implementation("com.opencsv:opencsv:5.6")
    implementation("io.vavr:vavr:0.10.4")

    compileOnly("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-core:4.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("net.andreinc:mockneat:0.4.8")

    testCompileOnly("org.projectlombok:lombok")

    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
    reports {
        // reports cannot be generated because of too long test names
        junitXml.required.set(false)
        html.required.set(false)
    }
}
