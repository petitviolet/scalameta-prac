package net.petitviolet.metas.app.macros

import net.petitviolet.metas.mcr.TimeLoggingM

object TimeLoggingMApp {
  @TimeLoggingM def heavy(n: Long): Long = {
    Thread.sleep(n)
    n
  }

  def main(args: Array[String]): Unit = {
    println(heavy(100))
  }
}

