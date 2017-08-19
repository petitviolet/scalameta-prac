package net.petitviolet.mcr_app

import scala.language.experimental.macros
import scala.language.reflectiveCalls

//sealed trait Point {
//  def ++[P<: Self](other: P): Self
//}
//
//case class AP(value: Int) extends Point {
//  type Self >: this.type <: AP
//  override def ++[P <: Self](other: P): AP = AP(this.value + other.value)
//}
//sealed trait Point {
//  type Self = Int // >: this.type /**/>: Point
//  def :+:(other: Self): Self// = ??? // = PointOps.add[P#Self](this.asInstanceOf[P#Self], other)
//
////  def :+:[P <: Point](other:P): P = add(this.asInstanceOf[P], other)
////  def add[P <: Point](self: P, other:P): P = PointOps.add[P](self, other)
//
////  def :+:[P <: Point](other:P): P = PointOps.add[P](this.asInstanceOf[P], other)
//}
//
//case class ReceivedPoint(value: Int) extends Point { //self: Point.Self =>
////  override type Self = ReceivedPoint
//  def :+:(other: ReceivedPoint): ReceivedPoint = ReceivedPoint(value + other.value)
//
//  override def :+:(other: Self): Self = ???
//}
//
//case class AvailablePoint(value: Int) extends Point {
////  override type Self = AvailablePoint
//  def :+:(other: AvailablePoint): AvailablePoint = AvailablePoint(value + other.value)
//  override def :+:(other: Self): Self = ???
//}

//case class OtherPoint(value: Int)  extends Point {
//  def :+:(other: P): P = OtherPoint(value + other.value)
//}
