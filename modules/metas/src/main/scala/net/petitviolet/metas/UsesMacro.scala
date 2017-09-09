package net.petitviolet.metas

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

class UsesAnnotation extends scala.annotation.StaticAnnotation {
  //  def macroTransform[T](annottees: Any*): Any = macro UsesMacro.usesImpl[T]
}

object UsesMacro {

  def usesImpl[T](c: whitebox.Context)(annottees: c.Expr[Any]*)(implicit T: c.WeakTypeTag[T]): c.Expr[Any] = {
    import c.universe._

    val typeSymbol: Symbol = weakTypeOf[T].typeSymbol
    val mixinType: TypeSymbol = typeSymbol.asType
    val mixInedOpt: Option[Symbol] = typeSymbol.typeSignature.decls.toList.find {
      case x: TermSymbol => x.isVal && x.isCaseAccessor && x.asType == mixinType
      case _ => false
    }

    //    val toMixIn: Tree = mixInedOpt getOrElse {
    //      val usesFieldName: TermName = typeSymbol.name.toTermName
    //      val x = q"val $usesFieldName: $mixinType"
    //      x
    //    }

    //    c.Expr[Any](???)
    annottees.head
  }

}

