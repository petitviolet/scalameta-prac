package net.petitviolet.metas.app

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.meta._

@compileTimeOnly("logging not expanded")
class TimeLogging extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val getNano = q"System.nanoTime()"
    defn match {
      case d @ Defn.Def(_, name, _, _, _, body) =>
        val newBody =
          q"""
          val methodName = ${name.value}
          val start = $getNano
          val result = $body
          val end = $getNano
          println(s"[$$methodName] tracking time: $${(end - start) / 1000000} ms")
          result
          """
        d.copy(body = newBody)
      case _ =>
        abort("annotate only function!")
    }
  }
}
