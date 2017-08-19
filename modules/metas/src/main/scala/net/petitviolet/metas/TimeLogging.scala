package net.petitviolet.metas

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.meta._

@compileTimeOnly("logging not expanded")
class TimeLogging extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case d @ Defn.Def(_, name, _, _, _, body) =>
        def s = Lit.String.apply _
        val newBody =
          q"""
          val start = System.nanoTime()
          val result = $body
          val end = System.nanoTime()
          println(${s(s"[${name.value}]tracking time:")} + ((end - start) / ${Lit.Long(1000000)}) + ${s(" ms")})
          result
          """
        d.copy(body = newBody)
      case _ =>
        abort("annotate only function!")
    }
  }
}
