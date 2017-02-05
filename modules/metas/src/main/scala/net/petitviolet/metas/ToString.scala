package net.petitviolet.metas

import scala.collection.immutable.Seq
import scala.meta.Ctor.Ref.Name
import scala.meta.Term.Param
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
    def createToString(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Defn.Def = {

      val args: Seq[String] = paramss.flatMap { params: Seq[Param] =>
        params.map { param: Param =>
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
        .mkString(""" + ", " + """)

      q"""
       override def toString: String = {
         ${name.syntax} + "(" + $joinedParamStrings + ")"
       }
      """
    }

    defn match {
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        val templateStats: Seq[Stat] =
          if (template.syntax.contains("toString")) {
            template.stats getOrElse Nil
          } else {
            val toStringMethod: Defn.Def = createToString(name, ctor.paramss)
            toStringMethod +: template.stats.getOrElse(Nil)
          }

        cls.copy(templ = template.copy(stats = Some(templateStats)))
      case _ =>
        println(defn.structure)
        abort("@ToString must annotate a class.")
    }
  }
}
