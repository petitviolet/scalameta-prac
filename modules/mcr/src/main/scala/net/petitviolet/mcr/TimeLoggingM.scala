package net.petitviolet.mcr

import scala.annotation.compileTimeOnly
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

@compileTimeOnly("logging not expanded")
class TimeLoggingM extends annotation.StaticAnnotation {
  def macroTransform(annottees: Any*) = macro TimeLoggingM.impl
}

object TimeLoggingM {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    annottees.map(_.tree).toList match {
      case DefDef(mods, name, tpes, paramss, rTpes, body) :: Nil =>
        val newBody =
          q"""
          val start = System.nanoTime()
          val result = $body
          val end = System.nanoTime()
          println("[" + ${name.toString} + s"]tracking time: $${((end - start) / 1000000)}ms")
          result
          """
        c.Expr[Any](DefDef.apply(mods, name, tpes, paramss, rTpes, newBody))
      case x =>
        c.abort(c.enclosingPosition, s"annotate only method: $x")
    }
  }
}
