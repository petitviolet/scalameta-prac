package net.petitviolet.metas.app

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.meta._

@compileTimeOnly("logging not expanded")
class TimeLogging extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val getNano: Term = q"System.nanoTime()"
    defn match {
      case d @ Defn.Def(_, name, _, _, _, body) =>
        def s = Lit.String.apply _
        val newBody =
          q"""
          val methodName = ${s(name.value)}
          val start = $getNano
          val result = $body
          val end = $getNano
          val millis = (end - start) / ${Lit.Long(1000000)}
          println(${s("[")} + methodName + ${s("]tracking time: ")} + millis + ${s(" ms")})
          result
          """
        d.copy(body = newBody)
      case _ =>
        abort("annotate only function!")
    }
  }
}
