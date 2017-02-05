package net.petitviolet.metas.app

import net.petitviolet.metas.Uses

//@AddField
trait AddFieldApp[Service] {
}

trait MyService {
  def double(n: Int) = n * 2
}

@Uses[MyService]
trait UsesFieldTarget {
  def show(n: Int) = println(this.myService.double(n))
}

class UsesFieldTargetImpl extends UsesFieldTarget {
  override val myService: MyService = new MyService {}
}

object UsesFieldApp extends App {
  val impl = new UsesFieldTargetImpl
  impl.show(100)
}
