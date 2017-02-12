package net.petitviolet.metas

import scala.collection.immutable.Seq
import scala.meta._

class Equals extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        Equals.insertEquals(cls)
      case _ =>
        println(defn.structure)
        abort("@Equals must annotate a class.")
    }
  }
}

object Equals {

  def insertEquals(cls: Defn.Class): Defn.Class = {
    val Defn.Class(_, name, _, ctor, template) = cls
    val stats = template.stats getOrElse Nil
    val templateStats: Seq[Stat] =
      if (containsEquals(stats)) stats
      else {
        val EqualsMethod: Defn.Def = Equals.createEquals(name, ctor.paramss)
        EqualsMethod +: stats
      }

    cls.copy(templ = template.copy(stats = Some(templateStats)))
  }

  private def containsEquals(stats: Seq[Stat]): Boolean = stats.exists { _.syntax.contains("def equals") }

  private def createEquals(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Defn.Def = {
    val argName = Term.Name("obj")
    val arg = {
      val inputType = Type.Name("Any")
      Term.Param(Nil, argName, Some(inputType), None)
    }

    val castArgName = Term.Name("other")
    val castArg = q"val ${Pat.Var.Term(castArgName)} = $argName.asInstanceOf[$name]"

    val equalStats = {
      val selfName = Term.Name("this")
      paramss.flatMap { _.map { param: Term.Param =>
        val inputParam = Term.Name(param.name.syntax)
        val selfSelect = Term.Select(selfName, inputParam)
        val otherSelect = Term.Select(castArgName, inputParam)
        q"($selfSelect == $otherSelect)".syntax
      }}.mkString (" && ").parse[Term].get
    }

    val methodName = Term.Name("equals")
    val resultType= Type.Name("Boolean")
    q"""override def $methodName($arg): $resultType = {
        if (!$argName.isInstanceOf[$name]) false
        else {
          $castArg
          $equalStats
        }
       }
     """
  }
}
