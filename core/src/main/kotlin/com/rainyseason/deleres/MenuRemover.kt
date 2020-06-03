package com.rainyseason.deleres

import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.util.concurrent.Callable

@Command(
  name = "deleteMenu",
  description = ["remove menu resources"],
  showDefaultValues = true
)
class MenuRemover : Callable<Int> {

  @Parameters(
    description = ["path to scan, default to current dir"],
    defaultValue = ""
  )
  private var path = ""

  override fun call(): Int {
    return removeMenu(path)
  }
}

fun removeMenu(path: String): Int {
  val menuFiles = findFileWhere(path) {
    it.extension == "xml" && it.pathWithoutName().contains("/menu")
  }
  if (menuFiles.isEmpty()) {
    return 0
  }

  val allJavaAndKotlinContent = allJavaAndKtContentOf(path)
  val allXmlContent = allXmlContentOf(path)

  menuFiles.forEach { menuFile ->
    val name = menuFile.name.removeSuffix(".xml")
    val codeRef = "menu.$name"
    val dynamicRef = "AppMenu.$name"
    val xmlRef = "@menu/$name"
    val shouldRemove = !allJavaAndKotlinContent.contains(codeRef)
      && !allJavaAndKotlinContent.contains(dynamicRef)
      && !allXmlContent.contains(xmlRef)
    if (shouldRemove) {
      log("found unused menu ${menuFile.path}")
      menuFile.delete()
    }
  }
  return 0
}