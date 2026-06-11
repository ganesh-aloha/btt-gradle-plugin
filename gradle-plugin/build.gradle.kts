plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.github.gmazzo.buildconfig") version "6.0.9"
}

group = "com.github.blue-triangle-tech"
version = "1.0.6"

gradlePlugin {
    plugins {
        create("bttPlugin") {
            id = "com.github.blue-triangle-tech.btt-gradle-plugin"
            implementationClass = "com.bluetriangle.bttplugin.BttPlugin"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

buildConfig {
    buildConfigField("String", "VERSION", provider { "\"${project.version}\"" })
}

kotlin {
    jvmToolchain(11)
}

repositories {
    mavenCentral()
    google() // optional but good to keep
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.android.tools.build:gradle:8.4.0") // match your AGP
    implementation("org.ow2.asm:asm-util:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["java"])
            }
        }
    }
}


tasks.register("generateBuildInfo") {
    val outputDir = layout.buildDirectory.dir("generated/source/buildInfo")

    outputs.dir(outputDir)

    doLast {
        val file = outputDir.get().file("com/bluetriangle/bttplugin/BttPluginBuildInfo.kt").asFile
        file.parentFile.mkdirs()

        file.writeText(
            """
            package com.btt

            object BttPluginBuildInfo {
                const val VERSION = "$version"
            }
            """.trimIndent()
        )
    }
}