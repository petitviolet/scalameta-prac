package net.petitviolet.metas
import scala.meta.Pat.Var.{Term => PTerm}
import scala.meta._
import scala.collection.immutable.Seq

object validator {

  private def addChecker(defn: Any, checkFunc: (Name, Term) => Term): Defn = {
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

  private def argumentMinMax(target: AnyRef) = {
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

  class NonNull extends scala.annotation.StaticAnnotation {
    inline def apply(defn: Any): Any = meta {
      def check(name: Name, term: Term): Term = {
        lazy val msg = s"$name is null"
        q"""{
           val result = $term
           require(result != null, $msg)
           result
         }"""
      }

      addChecker(defn, check)
    }
  }

  class NonEmpty extends scala.annotation.StaticAnnotation {
    inline def apply(defn: Any): Any = meta {
      def check(name: Name, term: Term): Term = {
        lazy val msg = s"$name is empty."
        q"""{
           val result = $term
           require(result.nonEmpty, $msg)
           result
         }"""
      }
      addChecker(defn, check)
    }
  }

  class Length(min: Int = 0, max: Int = 0) extends scala.annotation.StaticAnnotation {
    inline def apply(defn: Any): Any = meta {
      def check(min: Int, max: Int)(name: Name, term: Term): Term = {

        q"""{
          def msg(resultSize: Int) = {
            val minCond = if ($min <= 0) "" else $min + " <="
            val maxCond = if ($max <= 0) "" else "<= " + $max
            ${name.syntax} + " size is invalid. " + minCond + " " + resultSize + " " + maxCond
          }
          def minValid(min: Int, resultSize: Int): Boolean =
            if (min <= 0) true else min <= resultSize
          def maxValid(max: Int, resultSize: Int): Boolean =
            if (max <= 0) true else max >= resultSize

          val result = $term
          val size: Int = result.size
          require(minValid($min, size) && maxValid($max, size), msg(size))
          result
         }"""
      }

      val (minSize, maxSize) = argumentMinMax(this)

//      println(s"min: $minSize, max: $maxSize")

      addChecker(defn, check(minSize, maxSize))
    }
  }


  class IntRange(min: Int = 0, max: Int = 0) extends scala.annotation.StaticAnnotation {
    inline def apply(defn: Any): Any = meta {

      def check(min: Int, max: Int)(name: Name, term: Term): Term = {
        q"""{
          def msg(value: Int) = {
            val minCond = if ($min <= 0) "" else $min + " <="
            val maxCond = if ($max <= 0) "" else "<= " + $max
            ${name.syntax} + " is invalid. " + minCond + " " + value + " " + maxCond
          }
          def minValid(min: Int, value: Int): Boolean =
            if (min <= 0) true else min <= value
          def maxValid(max: Int, value: Int): Boolean =
            if (max <= 0) true else max >= value

          val result = $term
          require(minValid($min, result) && maxValid($max, result), msg(result))
          result
         }"""
      }

      val (minSize, maxSize) = argumentMinMax(this)

//      println(s"min: $minSize, max: $maxSize")

      addChecker(defn, check(minSize, maxSize))
    }
  }
}