package net.petitviolet.metas.app

import net.petitviolet.metas.TimeLogging

object TimeLoggingApp {
  @TimeLogging def heavy(n: Long): Long = {
    Thread.sleep(n)
    n
  }

  def main(args: Array[String]): Unit = {
    println(heavy(100))
  }

}
