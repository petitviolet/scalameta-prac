package net.petitviolet.metas.app

import net.petitviolet.metas.ToString

class ToStringClass(n: Int, label: String)

@ToString
class ToStringClassA(n: Int, label: String)

private object ToStringApp extends App {
  val no = new ToStringClass(1, "non-annotated")
  println(no.toString)

  val annotated = new ToStringClassA(2, "annotated")
  println(annotated.toString)
}
