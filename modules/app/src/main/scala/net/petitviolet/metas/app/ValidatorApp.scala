package net.petitviolet.metas.app

import net.petitviolet.metas.validator._

object NonNullApp extends App {
  @NonNull
  val v1 = 100 // OK

  @NonNull
  def d1(): Null = {
    val n = 1 + 100
    null
  }

  @NonNull
  val v2 = {
    //    null // error
  }

  @NonNull
  lazy val v3 = null

  //  println(d1) // error
  //  println(v3) // error
}

object SizeApp extends App {
  @Length(min = 1, max = 10)
  val seq = 1 to 10 // OK

  @Length(min = 10)
  val seq2 = 1 to 10 // OK

  @Length(max = 10)
  val seq3 = 1 to 10 // OK

  @Length(min = 20, max = 100)
  val seq4 = 1 to 10 // Error
}

object NonEmptyApp extends App {
  @NonEmpty
  val nonEmtpy = 1 :: Nil

  @NonEmpty
  val empty = Nil // Error
}

object RangeApp extends App {
  @IntRange(min = 1, max = 10)
  val ok1 = 5

  @IntRange(max = 10)
  val ok2 = 5

  @IntRange(min = 1)
  val ok3 = 5

  @IntRange(min = 1, max = 10)
  val ng = 100
}
