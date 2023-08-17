rootProject.name = "weave-optifine-url"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}