package net.petitviolet.meta_mirror.app

import scala.meta._

object mirrorApp extends App {
  implicit val m: Mirror = Mirror()

  m.sources.foreach {
    _.collect {
      case ref @ q"println(str)" =>
        println(s"children: ${ref.children}")
        println(s"parent: ${ref.parent}")
      case ref @ Term.Name("println") =>
        println(s"symbol: ${ref.symbol}")
    }
  }
}

