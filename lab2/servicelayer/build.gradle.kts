plugins {
    id("java")
}

group = "com.mpolina.cats"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":dao")))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("name.remal.gradle-plugins.lombok:lombok:2.0.2")
    implementation("name.remal.lombok:name.remal.lombok.gradle.plugin:2.0.2")

    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("com.h2database:h2:2.1.214")
    testImplementation("org.hibernate.orm:hibernate-core:6.1.7.Final")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}