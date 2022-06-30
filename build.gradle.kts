plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.3.7"
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
    paperDevBundle("1.19-R0.1-SNAPSHOT")
}

group = "fr.zelytra"
version = "1.1"
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
