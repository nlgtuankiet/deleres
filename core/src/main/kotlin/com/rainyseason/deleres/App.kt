package com.rainyseason.deleres

import picocli.CommandLine
import picocli.CommandLine.Command
import kotlin.system.exitProcess

@Command(
  subcommands = [
    MenuRemover::class,
    EpoxyViewRemover::class,
    LayoutRemover::class,
    PrimitiveRemover::class,
    DrawableRemover::class
  ]
)
object App

fun main(args: Array<String>) {
  exitProcess(CommandLine(App).execute(*args))
}
