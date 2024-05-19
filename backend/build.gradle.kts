plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    jacoco
}

group = "cz.woidig"
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
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.liquibase:liquibase-core")
    implementation("org.hibernate.orm:hibernate-community-dialects")
    implementation("org.xerial:sqlite-jdbc")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("commons-validator:commons-validator:1.8.0") {
        exclude(module = "commons-logging")
        exclude(module = "commons-collections")
    }
}

// paths that will be excluded from the coverage report
var jacocoExcludes = listOf(
    "**/config/*",
    "**/model/*",
    "**/exceptions/*",
    "**/dto/*"
)

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
    dependsOn(tasks.test) // tests are required to run before generating the report

    // exclude paths from coverage report
    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it).matching {
            exclude(jacocoExcludes)
        }
    })
}
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = BigDecimal("0.8")
            }
        }
    }
}