package net.petitviolet.metas.mcr
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DefMacro {
  def show[A](value: A): String = macro DefMacroImpl.showImpl[A]
}

object DefMacroImpl {
  def showImpl[A](c: blackbox.Context)(value: c.Expr[A]): c.Expr[String] = {
    import c.universe._
    val tpeA = c.weakTypeOf[A]
    c.Expr(Literal(Constant(s"type: ${tpeA.typeSymbol.fullName}, value: ${value.toString()}")))
  }
}
