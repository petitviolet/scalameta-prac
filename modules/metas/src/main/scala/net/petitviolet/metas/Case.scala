package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta.Defn.Def
import scala.meta._

@compileTimeOnly("not expanded")
class Case {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, template), companion: Defn.Object)) =>
        // companion object exists
        val (newCls, newCompanion) = Case.insertMethods(cls, Some(companion))
        Term.Block(Seq(newCls, newCompanion))
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        // companion object does not exists
        val (newCls, newCompanion) = Case.insertMethods(cls, None)
        Term.Block(Seq(newCls, newCompanion))
      case _ =>
        println(defn.structure)
        abort("@Unapply must annotate a class.")
    }
  }
}

object Case {
  private def insertInstanceMethods(cls: Defn.Class): Defn.Class = {
    (ToString.insert _ compose Equals.insert)(cls)
  }
  private def insertCompanionMethods(cls: Defn.Class, companionOpt: Option[Defn.Object]): Defn.Object = {
    (Apply.insert(cls) _ andThen
      Option.apply[Defn.Object] andThen
      Unapply.insert(cls))(companionOpt)
  }

  def insertMethods(cls: Defn.Class, companionOpt: Option[Defn.Object]): (Defn.Class, Defn.Object) = {
    (insertInstanceMethods(cls), insertCompanionMethods(cls, companionOpt))
  }

}
