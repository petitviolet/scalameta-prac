package net.petitviolet.metas

import scala.language.dynamics
import scala.meta._

trait MixInDynamic[Service] extends UsesDynamic[Service] {
  private val map = collection.mutable.Map.empty[String, Service]
  final def updateDynamic(name:String)(value: Service) = {
    map(name) = value
  }

  override final def selectDynamic(name: String): Service = map(name)
}
trait UsesDynamic[Service] extends Dynamic {
  def selectDynamic(name: String): Service = sys.error("not implemented")
}


class UsesTrait extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    import scala.collection.immutable.Seq
    defn match {
      case Term.Block(Seq(traitDefn @ Defn.Trait(_, typeName, _, _, _), o: Defn.Object))=>
        // companion objectがある場合
        val usesTrait = UsesTrait.createUsesTrait(typeName)
        Term.Block(traitDefn :: usesTrait :: o :: Nil)
      case traitDefn: Defn.Trait =>
        // companion objectが無い場合
        val usesTrait = UsesTrait.createUsesTrait(traitDefn.name)
        Term.Block(Seq(traitDefn, usesTrait))
      case _ =>
        println(defn.structure)
        abort(s"@${this.getClass.getSimpleName} must be Trait")
    }
  }
}

object UsesTrait {
  import scala.meta._

  def createUsesTrait(typeName: scala.meta.Type.Name): scala.meta.Defn.Trait = {
    val field: Decl.Val = Uses.createFieldToAdd(typeName)
    val traitName: Type.Name = Type.Name(s"Uses${typeName.value}")
    q"trait $traitName { $field }"
  }

}
