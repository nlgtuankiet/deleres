package com.rainyseason.deleres

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.File
import java.util.concurrent.Callable

@Command(
  name = "deleteDrawable",
  description = ["remove drawable resources, include vector drawable, png, jpg, jpeg, webp"],
  showDefaultValues = true
)
class DrawableRemover : Callable<Int> {

  @Option(
    names = ["extensions"],
    required = false,
    defaultValue = "xml,png,jpg,jpeg,webp"
  )
  private lateinit var extensions: String

  @Parameters(
    description = ["path to scan, default to current dir"],
    defaultValue = ""
  )
  private var path = ""

  override fun call(): Int {
   return removeDrawable(path = path, extensions = extensions)
  }
}

private fun removeDrawable(path: String, extensions: String): Int {
  val extensionList = extensions.split(",").toSet()
  if (extensionList.isEmpty()) {
    return 0
  }

  log("remove drawable with extension: $extensionList")

  val rootFile = if (path.isNotBlank()) {
    File(path)
  } else {
    userDir
  }

  val files = findFileWhere(rootFile.absolutePath) {
    it.isFile
      && extensionList.contains(it.extension)
      && it.pathWithoutName().contains("/drawable")
  }

  if (files.isEmpty()) {
    return 0
  }

  val allJavaAndKotlinContent = allJavaAndKtContentOf(path)

  val allXmlContent = allXmlContentOf(path)

  files.forEach { file ->
    var name = file.name.trim()
    extensionList.plus("9").forEach { name = name.removeSuffix(".$it") }

    val xmlRef = "@drawable/$name"
    val codeRef = "drawable.$name"
    val dynamicRefName = "AppDrawable.$name"
    val shouldRemove = !allXmlContent.contains(xmlRef)
      && !allJavaAndKotlinContent.contains(codeRef)
      && !allJavaAndKotlinContent.contains(dynamicRefName)
    if (shouldRemove) {
      println("found unused drawable: ${file.path}")
      file.delete()
    }
  }

  return 0
}