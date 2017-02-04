package net.petitviolet.metas.app.macros

import scala.reflect.macros.blackbox

sealed trait Point extends Any

case class ReceivedPoint(value: Int) extends AnyVal with Point {
  def +(other: ReceivedPoint): ReceivedPoint = ReceivedPoint(value + other.value)
}

case class AvailablePoint(value: Int) extends AnyVal with Point {
  def +(other: AvailablePoint): AvailablePoint = AvailablePoint(value + other.value)
}

case class OtherPoint(value: Int)  extends AnyVal with Point {

}

object Point {
  import scala.language.experimental.macros

  def add[P <: Point](p1: P, p2: P): P = macro addImpl[P]

  def addImpl[P <: Point](c: blackbox.Context)(p1: c.Expr[P], p2: c.Expr[P]): c.Expr[P] = {
    import c.universe._
    val tree = q"$p1 + $p2"
    c.Expr[P](tree)
  }
}
