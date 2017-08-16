package net.petitviolet.metas.mcr
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DefMacro {
  def show[A](value: A): String = macro DefMacroImpl.showImpl[A]
}

object DefMacroImpl {
  def showImpl[A: c.WeakTypeTag](c: blackbox.Context)
                                (value: c.Expr[A]): c.Expr[String] = {
    import c.universe._
    val typeName = c.weakTypeTag[A].tpe.typeSymbol.fullName
    val tree = value.tree
    c.Expr[String](Literal(Constant(s"type: $typeName, value: $tree")))
  }
}
