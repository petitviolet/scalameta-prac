package net.petitviolet.metas.app.macros

//object PointOps {
//  import scala.language.experimental.macros
//  import scala.reflect.macros.blackbox
//
//  def add[P <: Point](p1: P, p2: P): P = macro addImpl[P]
//
//  def addImpl[P <: Point](c: blackbox.Context)(p1: c.Expr[P], p2: c.Expr[P]): c.Expr[P] = {
//    import c.universe._
//    val tree = q"$p1 :+: $p2"
//    c.Expr[P](tree)
//  }
//
//  def tm(c: blackbox.Context)(x: c.Expr[Int])(y: c.Expr[Int]) = c.universe.Ident(c.universe.TypeName("C"))
//
//  def selfType[P <: Point: c.WeakTypeTag](c: blackbox.Context): c.universe.Ident = {
//    import c.universe._
//    val typeName = weakTypeOf[P].typeSymbol.fullName
//    Ident(TermName(typeName))
//  }
//}


