plugins {
  id("org.jetbrains.kotlin.jvm")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(project(":core"))
}
