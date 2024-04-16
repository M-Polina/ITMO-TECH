plugins {
    id("java")
}

group = "com.mpolina.cats"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    implementation("org.hibernate.orm:hibernate-core:6.1.7.Final")
    compileOnly("org.hibernate.orm:hibernate-core:6.1.7.Final")
    annotationProcessor("org.hibernate.orm:hibernate-core:6.1.7.Final")

    implementation("org.postgresql:postgresql:42.5.4")
    compileOnly("org.postgresql:postgresql:42.5.4")
    annotationProcessor("org.postgresql:postgresql:42.5.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}