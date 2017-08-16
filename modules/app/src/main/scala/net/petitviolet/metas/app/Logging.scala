package net.petitviolet.metas.app

import scala.meta.Dialect.current
import scala.meta._

class Logging extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    def loggingTerm(term: Term): Term = {
      q"""
       println(${Lit.String("[start]")})
       val result = $term
       println(${Lit.String("[end]")} + result)
       result
       """
    }

    defn match {
      case Defn.Val(mods, pats, decltpe, term) =>
        Defn.Val(mods, pats, decltpe, loggingTerm(term))
      case Defn.Def(mods, name, tparams, paramss, decltpe, term) =>
        Defn.Def(mods, name, tparams, paramss, decltpe, loggingTerm(term))
      case _ =>
        abort("should annotate only val or def.")
    }
  }
}
