plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.3.3"
}
allprojects {
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://papermc.io/repo/repository/maven-public/")
            url = uri("https://repo.maven.apache.org/maven2/")
        }

    }
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

group = "fr.zelytra"
version = "1.0"
description = "NovaStructura"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
