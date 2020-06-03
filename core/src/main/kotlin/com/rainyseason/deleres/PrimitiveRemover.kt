package com.rainyseason.deleres

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.util.concurrent.Callable

@Command(
  name = "deletePrimitive",
  description = ["remove primitive resources (string, color, etc)"],
  showDefaultValues = true
)
class PrimitiveRemover : Callable<Int> {

  @Option(
    names = ["string"],
    required = false,
    description = ["remove string"],
    defaultValue = "true"
  )
  private var removeString: Boolean = true

  @Option(
    names = ["color"],
    required = false,
    description = ["remove color"],
    defaultValue = "true"
  )
  private var removeColor: Boolean = true

  @Option(
    names = ["stringArray"],
    required = false,
    description = ["remove string-array"],
    defaultValue = "true"
  )
  private var removeStringArray: Boolean = true

  @Option(
    names = ["bool"],
    required = false,
    description = ["remove bool"],
    defaultValue = "true"
  )
  private var removeBool: Boolean = true

  @Option(
    names = ["dimen"],
    required = false,
    description = ["remove dimen"],
    defaultValue = "true"
  )
  private var removeDimen: Boolean = true

  @Option(
    names = ["integer"],
    required = false,
    description = ["remove integer"],
    defaultValue = "true"
  )
  private var removeInteger: Boolean = true

  @Parameters(
    description = ["path to scan, default to current dir"],
    defaultValue = ""
  )
  private var path = ""

  override fun call(): Int {
    if (path.isEmpty()) {
      path = System.getProperty("user.dir")
    }
    return removePrimitive(
      path = path,
      checkString = removeString,
      checkColor = removeColor,
      checkStringArray = removeStringArray,
      checkBool = removeBool,
      checkDimen = removeDimen,
      checkInteger = removeInteger
    )
  }
}

// todo declare-styleable, id, interpolator
/**
 * typealias AppAnim = R.anim
 * typealias AppAnimator = R.animator
 * typealias AppInterpolator = R.interpolator
 * typealias AppMipmap = R.mipmap
 * typealias AppPlurals = R.plurals
 * typealias AppRaw = R.raw
 * typealias AppStyle = R.style
 * typealias AppStyleable = R.styleable
 * typealias AppXml = R.xml
 */
private fun removePrimitive(
  path: String,
  checkString: Boolean,
  checkColor: Boolean,
  checkStringArray: Boolean,
  checkBool: Boolean,
  checkDimen: Boolean,
  checkInteger: Boolean
): Int {
  if (!checkString && !checkColor) {
    return 0
  }
  log("scan primitive resource in $path")
  val containerFiles = findFileWhere(location = path) { file ->
    file.extension == "xml"
  }

  val ignoredNames = setOf(
    "facebook_app_id",
    "fb_login_protocol_scheme",
    "zalo_app_id",
    "zalo_auth_scheme",
    "gcm_sender_id",
    "insider_partner"
  )
  val allJavaAndKtContent = allJavaAndKtContentOf(path)
  val allXmlContent = allXmlContentOf(path)

  containerFiles.forEach { containerFile ->
    val nameToRemove = mutableSetOf<String>()
    val containerTextContent = containerFile.readText()
    val document = Jsoup.parse(containerTextContent, "", Parser.xmlParser())
    if (checkString) {
      checkPrimitive(
        doc = document,
        resourceTag = "string",
        ignoredNames = ignoredNames,
        dynamicPrefix = "AppString.",
        allJavaAndKtContent = allJavaAndKtContent,
        allXmlContent = allXmlContent
      ) { nameToRemove.add(it) }
    }
    if (checkColor) {
      checkPrimitive(
        doc = document,
        resourceTag = "color",
        ignoredNames = ignoredNames,
        dynamicPrefix = "AppColor.",
        allJavaAndKtContent = allJavaAndKtContent,
        allXmlContent = allXmlContent
      ) { nameToRemove.add(it) }
    }
    if (checkStringArray) {
      checkPrimitive(
        doc = document,
        resourceTag = "string-array",
        codeRefPrefix = "array",
        ignoredNames = ignoredNames,
        dynamicPrefix = "AppArray.",
        allJavaAndKtContent = allJavaAndKtContent,
        allXmlContent = allXmlContent
      ) { nameToRemove.add(it) }
    }
    if (checkBool) {
      checkPrimitive(
        doc = document,
        resourceTag = "bool",
        ignoredNames = ignoredNames,
        dynamicPrefix = "AppBool.",
        allJavaAndKtContent = allJavaAndKtContent,
        allXmlContent = allXmlContent
      ) { nameToRemove.add(it) }
    }
    if (checkDimen) {
      checkPrimitive(
        doc = document,
        resourceTag = "dimen",
        ignoredNames = ignoredNames,
        dynamicPrefix = "AppDimen.",
        allJavaAndKtContent = allJavaAndKtContent,
        allXmlContent = allXmlContent
      ) { nameToRemove.add(it) }
    }
    if (checkInteger) {
      checkPrimitive(
        doc = document,
        resourceTag = "integer",
        ignoredNames = ignoredNames,
        dynamicPrefix = "AppInteger.",
        allJavaAndKtContent = allJavaAndKtContent,
        allXmlContent = allXmlContent
      ) { nameToRemove.add(it) }
    }
    if (nameToRemove.isNotEmpty()) {
      val newFileContent = removeNames(content = containerTextContent, names = nameToRemove)
      containerFile.bufferedWriter().use {
        it.write(newFileContent)
      }
    }
  }

  return 0
}

private fun checkPrimitive(
  doc: Document,
  resourceTag: String,
  codeRefPrefix: String = resourceTag,
  xmlRefPrefix: String = codeRefPrefix,
  ignoredNames: Set<String>,
  dynamicPrefix: String,
  allJavaAndKtContent: String,
  allXmlContent: String,
  onFoundNameToRemove: (String) -> Unit
) {
  doc.select(resourceTag).forEach {
    val name = it.attr("name")?.trim()
    if (!name.isNullOrBlank() && !ignoredNames.contains(name)) {
      val codeRef = "$codeRefPrefix.$name"
      val xmlRef = "@$xmlRefPrefix/$name"
      val dynamicRef = "$dynamicPrefix$name"
      val shouldRemove = !ignoredNames.contains(name)
        && !allJavaAndKtContent.contains(codeRef)
        && !allJavaAndKtContent.contains(dynamicRef)
        && !allXmlContent.contains(xmlRef)
      if (shouldRemove) {
        log("found unused $resourceTag: $name")
        onFoundNameToRemove(name)
      }
    }
  }
}


private fun removeNames(content: String, names: Set<String>): String {
  val tagRegex = """<(\S+) """.toRegex()
  val endTagRegex = """</(\S+)""".toRegex()
  val nameRegex = """name="(.*?)"""".toRegex()

  return buildString {
    var state: Boolean? = null // null -> new, false -> removing, true -> appending
    var lastTagName: String = ""
    content.split("\n").forEach { line ->
      when (state) {
        false -> {
          val tagName = endTagRegex.find(line)?.groups?.get(1)?.value?.trim()
            ?.removeSuffix(">")
          if (line.contains("</") && tagName == lastTagName) {
            state = null
          }
        }
        true -> {
          appendln(line)
          val tagName = endTagRegex.find(line)?.groups?.get(1)?.value?.trim()
            ?.removeSuffix(">")
          if (line.contains("</") && tagName == lastTagName) {
            state = null
          }
        }
        null -> {
          val name = nameRegex.find(line)?.groups?.get(1)?.value?.trim()
          val tagName = tagRegex.find(line)?.groups?.get(1)?.value?.trim()
          if (name == null) {
            appendln(line)
          } else {
            if (!names.contains(name)) {
              // appending
              appendln(line)
              if (!line.contains("</")) {
                lastTagName = tagName ?: error("missing tag name for ${line} file: ${content}")
                state = true
              }
            } else {
              // removing
              if (!line.contains("</")) {
                lastTagName = tagName ?: error("missing tag name for ${line} file: ${content}")
                state = false
              }
            }
          }
        }
      }
    }
  }.removeSuffix("\n")
}