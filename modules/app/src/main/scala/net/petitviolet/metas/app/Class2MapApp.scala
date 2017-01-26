package net.petitviolet.metas.app

import net.petitviolet.metas.Class2Map

case class MapClass(n: Int, label: String)

@Class2Map
case class MapClassAnnotated(n: Int, label: String)

private object Class2MapApp extends App {
  val mapClass = MapClass(1, "non-annotated")
  // compile error!
  // println(mapClass.toMap)
  val mapClassAnnotated = MapClassAnnotated(2, "annotated")
  println(mapClassAnnotated.toMap)
}
