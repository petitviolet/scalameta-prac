package net.petitviolet.metas.app.macros

import net.petitviolet.metas.mcr.DefMacro

object DefMacroApp extends App {
  println(DefMacro.show(100))
  println(DefMacro.show(100L))
}
