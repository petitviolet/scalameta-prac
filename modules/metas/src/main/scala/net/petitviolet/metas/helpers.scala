package net.petitviolet.metas

import scala.collection.immutable.Seq
import scala.meta._

private[petitviolet] trait InstanceMethodHelper {
  protected val METHOD_NAME: String

  protected def create(cls: Defn.Class): Defn.Def

  def insert(cls: Defn.Class): Defn.Class = {
    val Defn.Class(_, name, _, ctor, template) = cls
    val stats = template.stats getOrElse Nil
    val templateStats: Seq[Stat] =
      if (alreadyDefined(stats)) stats
      else {
        val EqualsMethod: Defn.Def = create(cls)
        EqualsMethod +: stats
      }

    cls.copy(templ = template.copy(stats = Some(templateStats)))
  }

  private def alreadyDefined(stats: Seq[Stat]): Boolean =
    stats.exists { _.syntax.contains(s"def $METHOD_NAME") }
}

private[petitviolet] trait CompanionMethodHelper {
  protected val METHOD_NAME: String

  protected def create(cls: Defn.Class)(companionOpt: Option[Defn.Object]): Defn.Def

  def insert(cls: Defn.Class)(companionOpt: Option[Defn.Object]): Defn.Object = {
    def method = create(cls)(companionOpt)

    companionOpt map { companion =>
      val stats = companion.templ.stats getOrElse Nil
      if (alreadyDefined(stats)) companion
      else companion.copy(templ = companion.templ.copy(stats = Some(method +: stats)))
    } getOrElse {
      q"object ${Term.Name(cls.name.value)} { $method }"
    }
  }

  private def alreadyDefined(stats: Seq[Stat]): Boolean =
    stats.exists { _.syntax.contains(s"def $METHOD_NAME") }

}
