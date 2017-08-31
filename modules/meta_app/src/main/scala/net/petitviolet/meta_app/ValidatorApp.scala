package net.petitviolet.meta_app

import net.petitviolet.metas.validator._


object NonNullApp extends App {
  @NonNull
  val v1: Int = 100 // OK

  @NonNull
  val d1 = {
    val n = 1 + 100
    // null // error
  }

  @NonNull
  val v2 = {
    // null // error
  }

  @NonNull
  val v3 = null

  //  println(d1) // error
  //  println(v3) // error
}

object SizeApp extends App {
  @Length(min = 1, max = 10)
  val ok = 1 to 10 // OK

  @Length(min = 100)
  val ng = 1 to 10 // NG

  @Length(max = 10)
  val seq3 = 1 to 10// OK

  @Length(1, 100)
  val seq4 = 1 to 10
}

object NonEmptyApp extends App {
  @NonEmpty
  val nonEmpty = { List(1, 2, 3) }

  @NonEmpty
  val empty = Nil // Error
}

object RangeApp extends App {
  @IntRange(min = Some(1), max = Some(10))
  val ok = 5

  @IntRange(max = Some(10))
  val ng = 50

  @IntRange(min = Some(1))
  val ok2 = 5

  @IntRange(min = Some(1), max = Some(10))
  val ng2 = 100
}
