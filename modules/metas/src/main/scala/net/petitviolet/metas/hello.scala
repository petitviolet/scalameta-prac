package net.petitviolet.metas

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.meta._

@compileTimeOnly("hello")
class hello extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case d: Defn.Def =>
        val newBody = q"""{
          println("hello")
          ${d.body}
        }"""
        d.copy(body = newBody)
      case _ =>
        abort("annotate only def")
    }
  }
}
