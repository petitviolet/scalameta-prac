package net.petitviolet.meta_app

import net.petitviolet.metas.TimeLogging

object TimeLoggingApp extends App {
  @TimeLogging
  def heavy(n: Long): Long = {
    Thread.sleep(n)
    n
  }

  println(heavy(100))
}
