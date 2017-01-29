package net.petitviolet.metas

import scala.collection.immutable.Seq
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
    def createFieldToAdd(tparams: Seq[Type.Param]) = {
      val fields = tparams.map { tparam =>
        val typeName = tparam.name.syntax
        val fieldName = {
          val valName = typeName.head.toLower + typeName.tail
          Pat.Var.Term(Term.Name(valName))
        }
        val typeAnnotation = Type.Name(typeName)

        q"val $fieldName: $typeAnnotation"
      }
      fields
    }

    defn match {
      case cls @ Defn.Trait(_, _, tparams, _, template) =>
        val addField = createFieldToAdd(tparams)
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


class UsesField extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls@Defn.Trait(_, _, tparams, _, template) =>
        val addField = UsesField.createFieldToAdd(classOf[String])
        val templateStats: Seq[Stat] =
          addField +: template.stats.getOrElse(Nil)

        cls.copy(templ = template.copy(stats = Some(templateStats)))
    }
  }
}

object UsesField {
  def createFieldToAdd[_](clazz: Class[_]) = {
    val fieldName = {
      val name = clazz.getSimpleName
      val valName = name.head.toLower + name.tail
      Pat.Var.Term(Term.Name(valName))
    }
    val typeName = Type.Name(clazz.getSimpleName)
    q"val $fieldName: $typeName"
  }
}
