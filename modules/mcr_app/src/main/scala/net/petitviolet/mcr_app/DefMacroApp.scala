package net.petitviolet.mcr_app

import net.petitviolet.mcr.DefMacro

object DefMacroApp extends App {
  println(DefMacro.show[Int](100))
  // type: scala.Int, value: 100

  println(DefMacro.show[Long](100L))
  // type: scala.Long, value: 100L

  println(DefMacro.show(new java.lang.String("hello")))
  // type: java.lang.String, value: new java.lang.String("hello")
}
