import con.rainyseason.deleres.deleres

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-android-extensions")
  id("com.rainyseason.deleres")
}

deleres {
  checkEpoxyModelView = true
}

android {
  compileSdkVersion = "28"
  buildToolsVersion = "28.0.3"

  defaultConfig {
    minSdkVersion(21)
    targetSdkVersion(28)
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("com.android.support:appcompat-v7:28.0.0")
  testImplementation("junit:junit:4.12")
  androidTestImplementation("com.android.support.test:runner:1.0.2")
  androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")

}