plugins {
  id("org.jetbrains.kotlin.jvm")
  application
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jsoup:jsoup:1.13.1")
  implementation("info.picocli:picocli:4.3.2")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
  // Define the main class for the application.
  mainClassName = "com.rainyseason.deleres.AppKt"
}
