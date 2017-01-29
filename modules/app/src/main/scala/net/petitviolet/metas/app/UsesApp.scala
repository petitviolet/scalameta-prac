package net.petitviolet.metas.app

import net.petitviolet.metas.Uses

// Error:(4, 2) top-level class without companion can only expand either into an eponymous class or into a block consisting in eponymous companions
//@Uses
trait MyOriginalService {
  def double(n: Int) = n * 2
}

object MyOriginalService {
  @Uses
  trait ChildService extends MyOriginalService
}

//object MyOriginalService {}
//
//private trait UsesDynamicAppTrait extends UsesDynamic[MyOriginalService] {
//  def print() = {
//    println(this.myOriginalService.double(10))
//  }
//}
//
//private object UsesDynamicApp extends UsesDynamicAppTrait with App with MixInDynamic[MyOriginalService] {
//  this.myOriginalService = new MyOriginalService {}
//  println(this)
//  print()
//}
//

