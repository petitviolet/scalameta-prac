package net.petitviolet.meta_app

import net.petitviolet.metas.{logging, loggingO}

object loggingApp extends App {
  @logging
  private def heavy(n: Int): Int = {
    Thread.sleep(n)
    n
  }

  heavy(100)

  @logging
  def add(i: Int, j: Int): Int = {
    i + j
  }

  println(add(1, 2))
}

object loggingOApp extends App {
  val logger = new {
    def info(s: String): Unit = println(s)
  }
  @loggingO(logger.info)
  def add(i: Int, j: Int): Int = {
    i + j
  }

  println(add(1, 2))
}
