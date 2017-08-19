package net.petitviolet.metas

import scala.annotation.compileTimeOnly
import scala.meta.Dialect.current
import scala.meta._

@compileTimeOnly("logging annotation")
class logging extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"..$mods def $name(..$paramss): $tpe = $body" =>
        val paramNames = paramss.map { p => Term.Name(p.name.value) }.reduceLeft {
          (acc: Term, n: Term) => q"$acc + ${Lit.String(",")} + $n"
        }
        q"""
         ..$mods def $name(..$paramss): $tpe = {
            println(${Lit.String(s"[start]${name.value}(")} + $paramNames + ${Lit.String(")")})
            val result = $body
            println(${Lit.String(s"[end]${name.value}(")} + $paramNames + ${Lit.String(") => ")} + result)
            result
         }
         """
    }
  }
}
//    def loggingTerm(term: Term): Term = {
//      q"""
//       println(${Lit.String("[start]")})
//       val result = $term
//       println(${Lit.String("[end]")} + result)
//       result
//       """
//    }
//
//
//    defn match {
//      case Defn.Val(mods, pats, decltpe, term) =>
//        Defn.Val(mods, pats, decltpe, loggingTerm(term))
//      case Defn.Def(mods, name, tparams, paramss, decltpe, term) =>
//        Defn.Def(mods, name, tparams, paramss, decltpe, loggingTerm(term))
//      case _ =>
//        abort("should annotate only val or def.")
//    }
//  }
//}

@compileTimeOnly("logging annotation")
class loggingO(outF: (String) => Unit) extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val out: Term = this match {
      case Term.New(Template(_, Seq(Term.Apply(_, Seq(_out))), _, _)) =>
        Term.Name(_out.syntax)
    }
    println("----------------------------------")
    println(this.structure)
    println(out)
    println("----------------------------------")
    defn match {
      case q"..$mods def $name(..$paramss): $tpe = $body" =>
        val paramNames = paramss.map { p => Term.Name(p.name.value) }.reduceLeft {
          (acc: Term, n: Term) => q"$acc + ${Lit.String(",")} + $n"
        }
        q"""
         ..$mods def $name(..$paramss): $tpe = {
            $out(${Lit.String(s"[start]${name.value}(")} + $paramNames + ${Lit.String(")")})
            val result = $body
            $out(${Lit.String(s"[end]${name.value}(")} + $paramNames + ${Lit.String(") => ")} + result)
            result
         }
         """
    }
  }
}
