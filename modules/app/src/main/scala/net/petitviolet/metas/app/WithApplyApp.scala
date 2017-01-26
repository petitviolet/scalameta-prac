package net.petitviolet.metas.app

import net.petitviolet.metas.WithApply

@WithApply
class WithApplyClass(value: Int)

object WithApplyApp extends App {
  println(WithApplyClass(100))
}
