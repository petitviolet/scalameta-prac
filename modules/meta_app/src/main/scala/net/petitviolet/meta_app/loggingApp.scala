package net.petitviolet.meta_app

import net.petitviolet.metas.logging

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
  add(1, 2)
}
