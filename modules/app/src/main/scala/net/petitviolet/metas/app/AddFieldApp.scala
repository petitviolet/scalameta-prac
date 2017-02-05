package net.petitviolet.metas.app

import net.petitviolet.metas.{MixIn, Uses}

//@AddField
trait AddFieldApp[Service] {
}

trait MyService {
  def double(n: Int) = n * 2
}

trait OtherService {
  def triple(n: Int) = n * 3
}

class OtherServiceImpl extends OtherService

object MyServiceImpl extends MyService

@Uses[OtherService]
@Uses[MyService]
trait UsesFieldTarget {
  def show(n: Int) = println(this.myService.double(n))
  def showTriple(n: Int) = println(this.otherService.triple(n))
}

@MixIn[MyService](MyServiceImpl)
@MixIn[OtherService](new OtherServiceImpl)
class UsesFieldTargetImpl extends UsesFieldTarget {
}

object UsesFieldApp extends App {
  val impl = new UsesFieldTargetImpl
  impl.show(100)

  impl.showTriple(100)
}
