plugins {
    `java-library`
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "8.10"
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

group = "ru.violence.xholo"
version = "2.0.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://maven.pkg.github.com/ruViolence/CoreAPI")
}

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    compileOnly("ru.violence.coreapi:common:0.1.14") {
        isTransitive = false
    }
    compileOnly("ru.violence.coreapi:bukkit:0.1.14") {
        isTransitive = false
    }
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("org.jetbrains:annotations:23.1.0")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifact(tasks.getByName("jar")) {
                artifactId = project.name
                classifier = ""
            }
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations["compileClasspath"].allDependencies.forEach {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    dependencyNode.appendNode("version", it.version)
                }
            }
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "version" to project.version,
            "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        archiveFileName.set("xHolo.jar")
    }
}
