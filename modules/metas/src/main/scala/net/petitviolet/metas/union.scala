package net.petitviolet.metas
import scala.meta._
import scala.annotation.StaticAnnotation

class union extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val tpe = t"String | Int"
    defn match {
      case v: Defn.Val =>
        v.copy(decltpe = Some(tpe))
      case _ =>
        abort("only val")
    }
  }
}
