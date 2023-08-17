plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.weave-mc.weave-gradle") version "fac948db7f"
}

group = "com.grappenmaker"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.weave-mc:weave-loader:v0.2.3")
}

kotlin { jvmToolchain(8) }
minecraft.version("1.8.9")