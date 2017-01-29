package net.petitviolet.metas

import scala.language.dynamics

trait MixIn[Service] extends Uses[Service] {
  private val map = collection.mutable.Map.empty[String, Service]
  final def updateDynamic(name:String)(value: Service) = {
    map(name) = value
  }

  override final def selectDynamic(name: String): Service = map(name)
}
trait Uses[Service] extends Dynamic {
  def selectDynamic(name: String): Service = sys.error("not implemented")
}


