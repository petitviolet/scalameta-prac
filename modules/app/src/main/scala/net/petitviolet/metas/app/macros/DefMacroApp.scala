package net.petitviolet.metas.app.macros

import net.petitviolet.metas.mcr.DefMacro

object DefMacroApp extends App {
  println(DefMacro.show[Int](100))
  println(DefMacro.show[Long](100L))
}
