package net.petitviolet.metas.app

import net.petitviolet.metas.Unapply

@Unapply
class UnapplyApp(protected val n: Int, val s: String)

object UnapplyAppApp extends App {
  val target = new UnapplyApp(n = 100, s = "hoge")
  target match {
    case UnapplyApp(n, s) => println(s"$n, $s")
    case _ => sys.error("out!")
  }
}
