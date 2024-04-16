plugins {
    java
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.mpolina.cats"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(mapOf("path" to ":dtomodule")))
    implementation(project(mapOf("path" to ":dao")))
//    implementation(project(mapOf("path" to ":servicelayer")))
    compileOnly("org.projectlombok:lombok")
//    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.5")
    implementation("org.springframework.security:spring-security-taglibs:6.0.3")
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.6")
    implementation("org.springframework.kafka:spring-kafka:3.0.7")
}

tasks.withType<Test> {
    useJUnitPlatform()
}