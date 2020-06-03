package com.rainyseason.deleres

import picocli.CommandLine
import picocli.CommandLine.Parameters
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "deleteLayout",
  description = ["remove layout resources"],
  showDefaultValues = true
)
class LayoutRemover : Callable<Int> {

  @Parameters(
    description = ["path to scan, default to current dir"],
    defaultValue = ""
  )
  private var path = ""

  override fun call(): Int {
    return removeLayout(path)
  }
}

fun removeLayout(
  path: String,
  onFoundUnused: (() -> Unit)? = null
): Int {
  log("scan layout in $path")
  val layoutFiles = findFileWhere(path) {
    it.extension == "xml" && it.pathWithoutName().contains("/layout")
  }
  if (layoutFiles.isEmpty()) {
    return 0
  }

  val allJavaAndKotlinContent = allJavaAndKtContentOf(path)
  val allXmlContent = allXmlContentOf(path)

  layoutFiles.forEach { file ->
    val name = file.name.removeSuffix(".xml")
    val xmlRef = "@layout/$name"
    val codeRef = "layout.$name"
    val dynamicRef = "AppLayout.$name"
    val dataBindingRef = name.split("_")
      .map { it.toLowerCase() }
      .joinToString("") { it.capitalize() } + "Binding"
    val shouldRemove = !allXmlContent.contains(xmlRef)
      && !allJavaAndKotlinContent.contains(codeRef)
      && !allJavaAndKotlinContent.contains(dataBindingRef)
      && !allJavaAndKotlinContent.contains(dynamicRef)
    if (shouldRemove) {
      println("find unused layout ${file.path}")
      onFoundUnused?.invoke()
      file.delete()
    }
  }

  return 0
}

