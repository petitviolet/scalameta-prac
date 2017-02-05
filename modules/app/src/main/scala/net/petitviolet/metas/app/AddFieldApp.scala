package net.petitviolet.metas.app

import net.petitviolet.metas.{ MixIn, Uses }

trait MyService { def double(n: Int) = n * 2 }
object MyServiceImpl extends MyService

trait OtherService { def triple(n: Int) = n * 3 }
class OtherServiceImpl extends OtherService

@Uses[MyService]()
@Uses[OtherService]()
trait UsesFieldTarget {
  def showDouble(n: Int) = println(this.myService.double(n))
  def showTriple(n: Int) = println(this.otherService.triple(n))
}

@MixIn[MyService](MyServiceImpl)
@MixIn[OtherService](new OtherServiceImpl)
class UsesFieldTargetImpl extends UsesFieldTarget

object UsesFieldApp extends App {
  val impl = new UsesFieldTargetImpl
  impl.showDouble(100) // 200
  impl.showTriple(100) // 300
}
