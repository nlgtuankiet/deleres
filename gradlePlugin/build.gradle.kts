import con.rainyseason.deleres.deleres

plugins {
  id("org.jetbrains.kotlin.jvm")
  id("com.github.johnrengelman.shadow")
  id("org.gradle.java-gradle-plugin")
  id("com.gradle.plugin-publish") version "0.10.1"
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(project(":core"))
}

gradlePlugin {
  plugins {
    create("deleres") {
      id = "com.rainyseason.deleres"
      implementationClass = "con.rainyseason.deleres.DeleresPlugin"
    }
  }
}
