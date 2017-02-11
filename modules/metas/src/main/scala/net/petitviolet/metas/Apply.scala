package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

// https://github.com/scalameta/tutorial/tree/master/macros/src/main/scala/scalaworld/macros
@compileTimeOnly("not expanded")
class Apply extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    def createApply(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Defn.Def = {
      val args = paramss.map { _.map { param => Term.Name(param.name.value) } }
      val defArgs = paramss.map { _.map { param => param.copy(mods = Nil) }}
      q"""def apply(...$defArgs): $name =
            new ${Ctor.Ref.Name(name.value)}(...$args)"""
    }
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, _),
      companion: Defn.Object)) =>
        val applyMethod = createApply(name, ctor.paramss)
        val templateStats: Seq[Stat] =
          applyMethod +: companion.templ.stats.getOrElse(Nil)
        val newCompanion = companion.copy(
          templ = companion.templ.copy(stats = Some(templateStats)))
        Term.Block(Seq(cls, newCompanion))
      // companion object does not exists
      case cls @ Defn.Class(_, name, _, ctor, _) =>
        val applyMethod = createApply(name, ctor.paramss)
        val companion   = q"object ${Term.Name(name.value)} { $applyMethod }"
        Term.Block(Seq(cls, companion))
      case _ =>
        println(defn.structure)
        abort("@Apply must annotate a class.")
    }
  }
}
