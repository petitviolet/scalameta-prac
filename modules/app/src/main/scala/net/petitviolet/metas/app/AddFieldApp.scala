package net.petitviolet.metas.app

import net.petitviolet.metas.AddAbstractField

@AddAbstractField
trait Uses[MyOriginalService]

@AddAbstractField
trait MixIn[MyOriginalService]

object AddFieldApp extends App {

}
