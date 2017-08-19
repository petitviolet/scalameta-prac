package net.petitviolet.meta_app

import net.petitviolet.metas.hello

object helloApp extends App {
  @hello
  def say() = {
    println("world")
  }

  say()
}
