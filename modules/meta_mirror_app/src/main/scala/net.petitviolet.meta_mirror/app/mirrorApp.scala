package net.petitviolet.meta_mirror.app

object mirrorApp extends App {
  import scala.meta._
  // scalahost plugin required
  implicit val m = Mirror()
  println(m)
  println(m.database)
}

