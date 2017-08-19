package net.petitviolet.mcr_app

import net.petitviolet.mcr.TimeLoggingM

object TimeLoggingMApp {
  @TimeLoggingM def heavy(n: Long): Long = {
    Thread.sleep(n)
    n
  }

  def main(args: Array[String]): Unit = {
    println(heavy(100))
  }
}
