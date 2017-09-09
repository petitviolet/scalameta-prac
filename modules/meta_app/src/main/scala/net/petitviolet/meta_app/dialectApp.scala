package net.petitviolet.meta_app

import net.petitviolet.metas.union

object dialectApp extends App {
  val str: String = "hoge"
  // this project is not Dotty...
  //  @union val str2 = "hoge"

  import scala.meta._
  {
    import scala.meta.dialects.Scala211
    "<hello />".parse[Term].get
  }

  {
    import scala.meta.dialects.Dotty
    "<hello />".parse[Term].get
  }

}
