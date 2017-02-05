package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta.Decl.Val
import scala.meta._

/**
 * - before
 * {{{
 *  @AddAbstractField
    trait AddField[MyOriginalService]
 * }}}
 *
 * - after
 * {{{
 *  trait AddField[MyOriginalService] {val myOriginalService: MyOriginalService}
 * }}}
 */
class AddField extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ Defn.Trait(_, _, tparams, _, template) =>
        val addField: Seq[Val] = tparams map { tparam => AddField.createFieldToAdd(tparam.name.syntax) }
        val templateStats: Seq[Stat] =
          if (template.syntax.contains("toString")) {
            template.stats getOrElse Nil
          } else {
            addField ++ template.stats.getOrElse(Nil)
          }

        cls.copy(templ = template.copy(stats = Some(templateStats)))
      case _ =>
        println(defn.structure)
        abort("@ToString must annotate a trait.")
    }
  }

}

object AddField {
  private[metas] def createFieldToAdd(typeName: String): Val = {
    val fields = {
      val fieldName = {
        val valName = typeName.head.toLower + typeName.tail
        Pat.Var.Term(Term.Name(valName))
      }
      val typeAnnotation = Type.Name(typeName)

      q"val $fieldName: $typeAnnotation"
    }
    fields
  }
}


@compileTimeOnly("@Uses not expanded")
class Uses[T] extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    println(this.structure)
    val injectionTypeOpt = this match {
      case Term.New(Template(_, Seq(Term.Apply(
      Term.ApplyType(_, Seq(Type.Name(target: String))), _)), _, _)) =>
        Some(target)
      case _ => None
    }
    defn match {
      case cls @ Defn.Trait(_, _, _, _, template) =>
        val addField: Seq[Val] = injectionTypeOpt map { injectionType =>
          AddField.createFieldToAdd(injectionType) :: Nil
        }  getOrElse Nil
        val templateStats: Seq[Stat] =
          if (template.syntax.contains("toString")) {
            template.stats getOrElse Nil
          } else {
            addField ++ template.stats.getOrElse(Nil)
          }

        cls.copy(templ = template.copy(stats = Some(templateStats)))
    }
  }
}

