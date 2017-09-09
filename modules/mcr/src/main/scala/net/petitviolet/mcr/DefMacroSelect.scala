package net.petitviolet.mcr

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DefMacroSelect {
  def select[A, B](obj: A, field: String): B = macro DefMacroSelectImpl.selectImpl[A, B]
}

object DefMacroSelectImpl {
  def selectImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context)(obj: c.Tree, field: c.Expr[String]): c.Tree = {
    import c.universe._
    val Expr(Literal(Constant(fieldName: String))) = field
    //    c.Expr[B](q"$obj.${TermName(fieldName)}")
    Select(obj, TermName(fieldName))
  }
}
