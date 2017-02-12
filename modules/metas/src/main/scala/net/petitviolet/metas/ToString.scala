package net.petitviolet.metas

import scala.collection.immutable.Seq
import scala.meta._

/**
 * - before
 * {{{
 * @ToString
 * class ToStringClassA(n: Int, label: String)
 * }}}
 *
 * - after
 * {{{
 * class ToStringClassA(n: Int, label: String) {
 *   override def toString: String = {
 *     "ToStringClassA" + "(" + ("n" + ":" + n.toString + ", " + "label" + ":" + label.toString) + ")"
 *   }
 * }
 * }}}
 */
class ToString extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        ToString.insertToString(cls)
      case _ =>
        println(defn.structure)
        abort("@ToString must annotate a class.")
    }
  }
}

object ToString {

  def insertToString(cls: Defn.Class): Defn.Class = {
    val Defn.Class(_, name, _, ctor, template) = cls
    val stats = template.stats getOrElse Nil
    val templateStats: Seq[Stat] =
      if (containsToString(stats)) stats
      else {
        val toStringMethod: Defn.Def = ToString.createToString(name, ctor.paramss)
        toStringMethod +: stats
      }

    cls.copy(templ = template.copy(stats = Some(templateStats)))
  }

  private def containsToString(stats: Seq[Stat]): Boolean = stats.exists { _.syntax.contains("def toString") }

  private def createToString(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Defn.Def = {

    val args: Seq[String] = paramss.flatMap { params: Seq[Term.Param] =>
      params.map { param: Term.Param =>
        // as just a string like `"n"`
        val paramName = s""""${param.name}""""
        // as a term like `n.toString`
        val value = s"${param.name}.toString".parse[Term].get

        // "\"n\" + \": \" + n.toString"
        s"""$paramName + ": " + $value"""
      }
    }

    // "\"n\" + \":\" + n.toString" +
    // ", " +
    // "\"x\" + \":\" + x.toString"
    val joinedParamStrings = args
      .mkString(""" + ", " + """).parse[Term].get

    q"""
       override def toString: String = {
         ${name.syntax} + "(" + $joinedParamStrings + ")"
       }
      """
  }
}
