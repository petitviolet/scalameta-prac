package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta.Defn.Def
import scala.meta._

@compileTimeOnly("not expanded")
class Case {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, template), companion: Defn.Object)) =>
        val (cMethods, iMethods) = Case.createMethods(name, ctor.paramss)

        val newCompanion = {
          val companionStats: Seq[Stat] =
            cMethods ++: companion.templ.stats.getOrElse(Nil)
          companion.copy(
            templ = companion.templ.copy(stats = Some(companionStats)))
        }

        val newCls = {
          val instanceTempl = template.copy(stats = Some(iMethods ++: template.stats.getOrElse(Nil)))
          cls.copy(templ = instanceTempl)
        }

        Term.Block(Seq(newCls, newCompanion))
      // companion object does not exists
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        val (cMethods, iMethods) = Case.createMethods(name, ctor.paramss)
        val companion   = q"object ${Term.Name(name.value)} { ..$cMethods }"

        val newCls = {
          val instanceTempl = template.copy(stats = Some(iMethods ++: template.stats.getOrElse(Nil)))
          cls.copy(templ = instanceTempl)
        }
        Term.Block(Seq(newCls, companion))
      case _ =>
        println(defn.structure)
        abort("@Unapply must annotate a class.")
    }
  }
}

object Case {
  private[petitviolet] def createCompanionMethods(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Seq[Defn.Def] = {
    val applyMethod = Apply.createApply(name, paramss)
    val unApplyMethod = Unapply.createUnApply(name, paramss)
    applyMethod :: unApplyMethod :: Nil
  }
  private[petitviolet] def createInstanceMethods(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Seq[Defn.Def] = {
    val toStringMethod = ToString.createToString(name, paramss)
    toStringMethod :: Nil
  }

  private[petitviolet] def createMethods(name: Type.Name, paramss: Seq[Seq[Term.Param]]): (Seq[Def], Seq[Def]) = {
    (createCompanionMethods(name, paramss), createInstanceMethods(name, paramss))
  }

}
