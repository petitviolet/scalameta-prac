package net.petitviolet.metas

import scala.collection.immutable.Seq
import scala.meta.Dialect.current
import scala.meta._

package object validator {
    // add check statement to `val` or `def`
    def addChecker(defn: Any, checkFunc: (Name, Term) => Term): Defn = {
      defn match {
        case Defn.Val(mods, pats, decltpe, value) =>
          val checkedValue = checkFunc(Term.Name(pats.head.syntax), value)
          Defn.Val(mods, pats, decltpe, checkedValue)
        case Defn.Def(mods, name, tparams, paramss, decltpe, body) =>
          val checkedBody = checkFunc(name, body)
          Defn.Def(mods, name, tparams, paramss, decltpe, checkedBody)
        case _ =>
          abort("error")
      }
    }

    // extract annotation class constructor, `min` and `max`
    def argumentMinMaxOpt(target: AnyRef): (Option[Int], Option[Int]) = {
      target match {
        case c @ Term.New(Template(_, Seq(Term.Apply(_,
        Seq(
        Term.Arg.Named(Term.Name("min"),
        Term.Apply(Term.Name("Some"), Seq(Lit(min: Int)))),
        Term.Arg.Named(Term.Name("max"),
        Term.Apply(Term.Name("Some"), Seq(Lit(max: Int))))))), _, _)) =>
          (Some(min), Some(max))
        case c @ Term.New(Template(_, Seq(Term.Apply(_,
        Seq(Term.Arg.Named(Term.Name("max"),
        Term.Apply(Term.Name("Some"), Seq(Lit(max: Int))))))), _, _)) =>
          (None, Some(max))
        case c @ Term.New(Template(_, Seq(Term.Apply(_,
        Seq(Term.Arg.Named(Term.Name("max"),
        Term.Apply(Term.Name("Some"), Seq(Lit(min: Int))))))), _, _)) =>
          (Some(min), None)
        case _ => (None, None)
      }
    }

    def argumentMinMax(target: AnyRef) = {
      target match {
        case c @ Term.New(Template(_, Seq(Term.Apply(_,
        Seq(Term.Arg.Named(Term.Name("min"), Lit(min: Int)),
        Term.Arg.Named(Term.Name("max"), Lit(max: Int))))), _, _)) =>
          (min, max)
        case c @ Term.New(Template(_, Seq(Term.Apply(_,
        Seq(Term.Arg.Named(Term.Name("max"), Lit(max: Int))))), _, _)) =>
          (0, max)
        case c @ Term.New(Template(_, Seq(Term.Apply(_,
        Seq(Term.Arg.Named(Term.Name("min"), Lit(min: Int))))), _, _)) =>
          (min, 0)
        case _ => (0, 0)
      }
    }

}
