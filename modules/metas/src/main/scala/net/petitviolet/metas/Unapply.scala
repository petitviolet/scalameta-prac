package net.petitviolet.metas

import scala.collection.immutable.Seq
import scala.meta.Term.Select
import scala.meta._

class Hoge(val n: Int, val s: String)

object Hoge {
  def unapply(arg: Hoge): Option[(Int, String)] = Some((arg.n, arg.s))
}

class Unapply extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    def createUnApply(name: Type.Name, paramss: Seq[Seq[Term.Param]]): Defn.Def = {
      val argName = Term.Name("arg")
      val inputParam: Seq[Seq[Term.Param]] = {
        val param: Term.Param = Term.Param(Nil, argName, Some(name), None)
        (param :: Nil) :: Nil
      }
      val resultParam: Type = {
        val types: Seq[Type] = paramss.flatMap { _.collect {
          case Term.Param(_ :: Mod.ValParam() :: Nil, _, Some(typeArg), _) =>
            // private val n: Int
            Type.Name(typeArg.syntax)
          case Term.Param(_ :: Mod.VarParam() :: Nil, _, Some(typeArg), _) =>
            // private var n: Int
            Type.Name(typeArg.syntax)
          case Term.Param(Mod.ValParam() :: Nil, _, Some(typeArg), _) =>
            // val n: Int
            Type.Name(typeArg.syntax)
          case Term.Param(Mod.VarParam() :: Nil, _, Some(typeArg), _) =>
            // var n: Int
            Type.Name(typeArg.syntax)
          case x =>
            println(s"invalid paramss: $paramss")
            abort(s"non-accessible constructor exists. `unapply` always returns None. cause => $x")
        }}

        val tupled = Type.Tuple(types)
        Type.Apply(Type.Name("Option"), tupled :: Nil)
      }

      val body: Term = {
        val select: Seq[Select] = paramss.flatMap { _.map { param =>
          Term.Select(argName, Term.Name(param.name.value))
        } }
        Term.Block(q"Some((..$select))" :: Nil)
      }
      Defn.Def(Nil, Term.Name("unapply"), Nil, inputParam, Some(resultParam), body)
    }
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, _),
      companion: Defn.Object)) =>
        val unApplyMethod = createUnApply(name, ctor.paramss)
        val templateStats: Seq[Stat] =
          unApplyMethod +: companion.templ.stats.getOrElse(Nil)
        val newCompanion = companion.copy(
          templ = companion.templ.copy(stats = Some(templateStats)))
        Term.Block(Seq(cls, newCompanion))
      // companion object does not exists
      case cls @ Defn.Class(_, name, _, ctor, _) =>
        val unApplyMethod = createUnApply(name, ctor.paramss)
        val companion   = q"object ${Term.Name(name.value)} { $unApplyMethod }"
        Term.Block(Seq(cls, companion))
      case _ =>
        println(defn.structure)
        abort("@Unapply must annotate a class.")
    }
  }
}
