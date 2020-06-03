package com.rainyseason.deleres

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.util.concurrent.Callable

@Command(
  name = "deleteEpoxyView",
  description = ["remove epoxy @ModelView"],
  showDefaultValues = true
)
class EpoxyViewRemover : Callable<Int> {

  @Parameters(
    description = ["path to scan, default to current dir"],
    defaultValue = ""
  )
  private var path = ""

  override fun call(): Int {
    return removeEpoxy(path)
  }
}

fun removeEpoxy(
  path: String,
  onFoundUnused: (() -> Unit)? = null
): Int {
  log("scan epoxy model view in $path")
  val epoxyClassNameRegex = """class (\w+)""".toRegex()
  val epoxyFiles = findFileWhere(path) {
    it.extension == "kt" && it.readText().run {
      contains("@ModelView(") && !contains("open class")
    }
  }
  if (epoxyFiles.isEmpty()) {
    return 0
  }
  val allJavaAndKotlinContent = allJavaAndKtContentOf(path)

  epoxyFiles.forEach { file ->
    val fileContent = file.readText()
    val className = epoxyClassNameRegex.find(fileContent)?.groups?.get(1)?.value?.trim()!!
      .toLowerCase()
      .removeSuffix("epoxy")
    val ktxStatement = "$className {"
    val modelStatement = "${className}Model_()"
    val shouldRemove = !allJavaAndKotlinContent.contains(ktxStatement, true)
      && !allJavaAndKotlinContent.contains(modelStatement, true)
    if (shouldRemove) {
      log("found unused model view ${file.path}")
      onFoundUnused?.invoke()
      file.delete()
    }
  }

  return 0
}