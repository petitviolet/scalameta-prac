package net.petitviolet.metas.app

import net.petitviolet.metas.{ MixIn, Uses }

trait DoubleService { def double(n: Int) = n * 2 }
object DoubleServiceImpl extends DoubleService

trait TripleService { def triple(n: Int) = n * 3 }
class TripleServiceImpl extends TripleService

@Uses[DoubleService]
@Uses[TripleService]
trait UsesServices {
  def showDouble(n: Int) = println(this.doubleService.double(n))
  def showTriple(n: Int) = println(this.tripleService.triple(n))
}

@MixIn[DoubleService](DoubleServiceImpl)
@MixIn[TripleService](new TripleServiceImpl)
class UsesFieldServicesImpl extends UsesServices

object UsesApp extends App {
  val impl = new UsesFieldServicesImpl
  impl.showDouble(100) // 200
  impl.showTriple(100) // 300
}
