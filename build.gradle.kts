plugins {
    `java-library`
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "8.0.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

group = "ru.violence.xholo"
version = "2.0.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly("ru.violence.coreapi:common:0.1.14") {
        isTransitive = false
    }
    compileOnly("ru.violence.coreapi:bukkit:0.1.14") {
        isTransitive = false
    }
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("org.jetbrains:annotations:23.1.0")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
                "version" to project.version,
                "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("libs/xHolo.jar"))
    }
}
