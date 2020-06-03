allprojects {
  repositories {
    jcenter()
    mavenCentral()
  }
}

buildscript {
  repositories {
    jcenter()
    mavenCentral()
  }
  val kotlinVersion by extra("1.3.72")
  dependencies {
    "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  }
}