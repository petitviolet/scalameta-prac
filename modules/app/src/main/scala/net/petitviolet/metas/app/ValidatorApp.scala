package net.petitviolet.metas.app

import net.petitviolet.metas.validator._


object NonNullApp extends App {
  @NonNull
  def v1: Int = {
    val x = 100
    x
  }// OK

  @NonNull
  def d1(): Null = {
    val n = 1 + 100
    null
  }

  @NonNull
  def v2 = {
    //    null // error
  }

  @NonNull
  def v3 = null

  //  println(d1) // error
  //  println(v3) // error
}

object SizeApp extends App {
  @Length(1, 10)
  def seq = 1 to 10 // OK

  @Length(min = 10)
  def seq2 = 1 to 10 // OK

  @Length(max = 10)
  def seq3 = 1 to 10// OK

  @Length(1, 100)
  def seq4 = 1 to 10
}

object NonEmptyApp extends App {
  @NonEmpty
  def nonEmtpy = { List(1, 2, 3) }

  @NonEmpty
  def empty = Nil // Error
}

object RangeApp extends App {
//  @IntRange(min = Some(1), max = Some(10))
//  def ok1 = 5
//
//  @IntRange(max = Some(10))
//  def ok2 = 5
//
//  @IntRange(min = Some(1))
//  def ok3 = 5
//
//  @IntRange(min = Some(1), max = Some(10))
//  def ng = 100
}
