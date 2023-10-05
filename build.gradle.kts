plugins {
    kotlin("jvm") version "1.9.10"
    id("com.github.weave-mc.weave-gradle") version "fac948db7f"
}

group = "com.grappenmaker"
version = "0.2"

minecraft.version("1.8.9")

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.weave-mc:weave-loader:v0.2.4")
}

kotlin {
    jvmToolchain(8)
}