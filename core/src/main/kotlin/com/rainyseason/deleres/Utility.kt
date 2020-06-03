package com.rainyseason.deleres

import java.io.File

fun log(message: String) {
  println(message)
}

private val ignoredDir = setOf(".gradle", ".idea", "build", ".git", "node_modules")

val userDir by lazy {
  File(System.getProperty("user.dir"))
}

fun allJavaAndKtContentOf(path: String): String {
  return findFileWhere(path) {
    it.extension == "kt" || it.extension == "java"
  }.joinToString(";") { it.readText() }
}

fun File.pathWithoutName(): String {
  return path.removeSuffix(name)
}

fun allXmlContentOf(path: String): String {
  return findFileWhere(path) {
    it.extension == "xml"
  }.joinToString(";") { it.readText() }
}

fun findFileWhere(location: String, predicate: (File) -> Boolean): List<File> {
  val result = mutableListOf<File>()
  val locationFile = File(location.ifEmpty { System.getProperty("user.dir") })
  if (locationFile.isDirectory && ignoredDir.contains(locationFile.name)) {
    return emptyList()
  }
  locationFile.listFiles()?.forEach { file ->
    if (file.isFile) {
      if (predicate.invoke(file)) {
        result.add(file)
      }
    } else if (file.isDirectory) {
      result.addAll(findFileWhere(file.absolutePath, predicate))
    }
  } ?: return emptyList()
  return result
}

