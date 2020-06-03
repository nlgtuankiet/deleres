allprojects {
  repositories {
    jcenter()
    google()
    mavenCentral()
  }
}

buildscript {
  repositories {
    google()
    jcenter()
    mavenCentral()
  }
  val kotlinVersion by extra("1.3.72")
  val kotlin_version by extra("1.3.72")
  dependencies {
    "classpath"(files("gradlePlugin/build/libs/gradlePlugin-all.jar"))
    "classpath"("com.github.jengelman.gradle.plugins:shadow:5.2.0")
    "classpath"("com.android.tools.build:gradle:3.5.3")
    "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  }
}