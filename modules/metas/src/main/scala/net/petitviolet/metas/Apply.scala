package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

// https://github.com/scalameta/tutorial/tree/master/macros/src/main/scala/scalaworld/macros
@compileTimeOnly("not expanded")
class Apply extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, _), companion: Defn.Object)) =>
        // companion object exists
        val newCompanion = Apply.insertApply(cls, Some(companion))
        Term.Block(Seq(cls, newCompanion))
      case cls @ Defn.Class(_, name, _, ctor, _) =>
        // companion object does not exist
        val newCompanion = Apply.insertApply(cls)
        Term.Block(Seq(cls, newCompanion))
      case _ =>
        println(defn.structure)
        abort("@Apply must annotate a class.")
    }
  }
}

object Apply {
  private def createApply(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Defn.Def = {
    val args = paramss.map { _.map { param => Term.Name(param.name.value) } }
    val defArgs = paramss.map { _.map { param => param.copy(mods = Nil) }}
    q"""def apply(...$defArgs): $name =
            new ${Ctor.Ref.Name(name.value)}(...$args)"""
  }

  private def containsApply(stats: Seq[Stat]): Boolean = stats.exists { _.syntax.contains("def apply") }

  private[petitviolet] def insertApply(cls: Defn.Class, companionOpt: Option[Defn.Object] = None): Defn.Object = {
    val name = cls.name
    val paramss = cls.ctor.paramss

    companionOpt map { companion =>
      val templateStats: Seq[Stat] = {
        companion.templ.stats collect {
          case stats if containsApply(stats)=>
//            println(s"already apply ${companion.structure}")
            stats
          case stats =>
            val applyMethod = createApply(name, paramss)
//            println(s"no apply ${companion.structure}")
            applyMethod +: stats
        } getOrElse {
          createApply(name, paramss) +: Nil
        }
      }
      val newCompanion = companion.copy(
        templ = companion.templ.copy(stats = Some(templateStats)))
      Term.Block(Seq(cls, newCompanion))

      newCompanion
    } getOrElse {
      val applyMethod = createApply(name, paramss)
      val companion   = q"object ${Term.Name(name.value)} { $applyMethod }"
      Term.Block(Seq(cls, companion))

      companion
    }

  }
}
