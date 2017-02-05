package net.petitviolet.metas

import scala.annotation.compileTimeOnly
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
object Uses {
  private[metas] def createFieldToAdd(typeName: String): Decl.Val = {
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

object MixIn {

  private[metas] def implementationToAdd(typeName: String, impl: Term): Defn.Val = {
    val fieldName = {
      val valName = typeName.head.toLower + typeName.tail
      Pat.Var.Term(Term.Name(valName))
    }
    val typeAnnotation = Type.Name(typeName)

    q"val $fieldName: $typeAnnotation = $impl"
  }

}


@compileTimeOnly("@Uses not expanded")
class Uses[T] extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val injectionTypeOpt = this match {
      case Term.New(Template(_, Seq(Term.Apply(
            Term.ApplyType(_, Seq(Type.Name(typeParam: String))), _)), _, _)) =>
        Some(typeParam)
      case _ => None
    }
    defn match {
      case cls @ Defn.Trait(_, _, _, _, template) =>
        val addField: Seq[Decl.Val] = injectionTypeOpt map { injectionType =>
          Uses.createFieldToAdd(injectionType) :: Nil
        }  getOrElse Nil
        val templateStats: Seq[Stat] =
            addField ++ template.stats.getOrElse(Nil)

        cls.copy(templ = template.copy(stats = Some(templateStats)))
      case _ =>
        println(this.structure)
        abort("@Uses[T] can only annotate a trait")
    }
  }
}
@compileTimeOnly("@MixIn not expanded")
class MixIn[T](impl: T) extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val (injectionType: String, impl: Term) = this match {
      case Term.New(Template(_, Seq(Term.Apply(
      Term.ApplyType(_, Seq(Type.Name(typeParam: String))), Seq(implName: Term.Name))), _, _)) =>
        // passed object as an implementation
        (typeParam, implName)

      case Term.New(Template(_, Seq(Term.Apply(
      Term.ApplyType(_, Seq(Type.Name(typeParam: String))),
      Seq(implNew: Term.New))), _, _)) =>
        // passed instantiating class as an implementation
        (typeParam, implNew)
      case _ =>
        println(this.structure)
        abort("invalid parameters")
    }
    // to inject a field has implementation
    val addField = MixIn.implementationToAdd(injectionType, impl)

    defn match {
      case cls @ Defn.Class(_, _, _, _, template) =>
        val templateStats: Seq[Stat] = addField +: template.stats.getOrElse(Nil)
        cls.copy(templ = template.copy(stats = Some(templateStats)))
      case obj @ Defn.Object(_, _, template) =>
        val templateStats: Seq[Stat] = addField +: template.stats.getOrElse(Nil)
        obj.copy(templ = template.copy(stats = Some(templateStats)))
      case trt @ Defn.Trait(_, _, _, _, template) =>
        val templateStats: Seq[Stat] = addField +: template.stats.getOrElse(Nil)
        trt.copy(templ = template.copy(stats = Some(templateStats)))
      case _ =>
        println(this.structure)
        abort("invalid annotating")
    }
  }
}

