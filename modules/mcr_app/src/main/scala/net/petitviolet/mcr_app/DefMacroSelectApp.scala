package net.petitviolet.mcr_app

import net.petitviolet.mcr.DefMacroSelect

object DefMacroSelectApp extends App {
  case class MyClass(value: String)
  val c = MyClass("hello")
//  val Select = "yes"
  val value = "yes"
  println(DefMacroSelect.select[MyClass, String](c, "value"))
}
