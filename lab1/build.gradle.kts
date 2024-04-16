plugins {
    id("java")
}

group = "com.mpolina.banks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("name.remal.gradle-plugins.lombok:lombok:2.0.2")
    implementation("name.remal.lombok:name.remal.lombok.gradle.plugin:2.0.2")
    implementation("org.projectlombok:lombok:1.18.22")
    implementation("org.projectlombok:lombok:1.18.22")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}