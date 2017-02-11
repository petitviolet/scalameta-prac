package net.petitviolet.metas.app

import net.petitviolet.metas.Apply

@Apply
class ApplyApp(value: Int)

object ApplyAppApp extends App {
  println(ApplyApp(100))
}
