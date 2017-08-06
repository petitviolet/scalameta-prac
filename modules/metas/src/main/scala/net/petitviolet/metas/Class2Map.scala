package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

// https://github.com/scalameta/tutorial/tree/master/macros/src/main/scala/scalaworld/macros
@compileTimeOnly("hello")
class Class2Map extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ Defn.Class(_, _, _, Ctor.Primary(_, _, paramss), template) =>
        val namesToValues: Seq[Term.Tuple] = paramss.flatten.map { param =>
          // looks below is not collect syntax, but can be compiled.
          q"(${param.name.syntax}, ${Term.Name(param.name.value)})"
        }
        val toMapImpl: Term =
          q"_root_.scala.collection.Map[String, Any](..$namesToValues)"
        val toMap =
          q"def toMap: _root_.scala.collection.Map[String, Any] = $toMapImpl"
        val templateStats: Seq[Stat] = toMap +: template.stats.getOrElse(Nil)
        cls.copy(templ = template.copy(stats = Some(templateStats)))
      case _ =>
        println(defn.structure)
        abort("@Class2Map must annotate a class.")
    }
  }
}
