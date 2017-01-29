package net.petitviolet.metas.app

import net.petitviolet.metas._

trait MyOriginalService {
  def double(n: Int) = n * 2
}

private object MyOriginalServiceImpl extends MyOriginalService

private trait AddFieldAppTrait extends Uses[MyOriginalService] {
  def print() = {
    println(this.myOriginalService.double(10))
  }
}

private object AddFieldApp extends AddFieldAppTrait with App with MixIn[MyOriginalService] {
  this.myOriginalService = MyOriginalServiceImpl
  println(this)
  print()
}

//@AddField
trait AddFieldSample[Service] {
}

object HogeHoge {
  implicit val clazz = classOf[MyOriginalService]
  @UsesField
  trait SomeApp {
  }
}
