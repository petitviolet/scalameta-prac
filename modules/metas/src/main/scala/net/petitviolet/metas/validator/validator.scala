package net.petitviolet.metas.validator

import scala.collection.immutable.Seq
import scala.meta.Dialect.current
import scala.meta._


/**
 * annotate the value is not null
 */
class NonNull extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    def check(name: Name, term: Term): Term = {
      lazy val msg = Lit.String(s"$name is null")
      q"""
       val result = $term
       require(result != ${Lit.Null(null)}, $msg)
       result
       """
    }

    addChecker(defn, check)
  }
}

/**
 * annotate the value is not empty
 */
class NonEmpty extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    def check(name: Name, term: Term): Term = {
      lazy val msg = s"$name is empty."
      q"""
           val result = $term
           require(result.nonEmpty, ${Lit.String(msg)})
           result
         """
    }

    addChecker(defn, check)
  }
}

/**
 * annotate the value length is
 *  - longer than or equals to `min`
 *  - shorter than or equals to `max`
 *
 * @param min if 0, not bounded
 * @param max if 0, not bounded
 */
class Length(min: Int = 0, max: Int = 0) extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    def check(min: Int, max: Int)(name: Name, term: Term): Term = {
      def i = Lit.Int.apply _

      def s = Lit.String.apply _

      q"""
        def msg(min: Int, max: Int, resultSize: Int): String = {
          val minCond = { if (min <= ${i(0)}) ${s("")} else min + ${s(" <=")} }
          val maxCond = { if (max <= ${i(0)}) ${s("")} else ${s("<= ")} + max }
          val res = ${s(name.syntax)} + ${s(" size is invalid. ")} + minCond + ${s(" ")} + resultSize + ${s(" ")} + maxCond
          res
        }
        def minValid(min: Int, resultSize: Int): Boolean = if (min <= ${i(0)}) ${Lit.Boolean(true)} else min <= resultSize
        def maxValid(max: Int, resultSize: Int): Boolean = if (max <= ${i(0)}) ${Lit.Boolean(true)} else max >= resultSize

        val result = $term
        val size: Int = result.size
        require(minValid(${i(min)}, size) && maxValid(${i(max)}, size), msg(${i(min)}, ${i(max)}, size))
        result
        """
    }

    val (minSize, maxSize) = argumentMinMax(this)

//      println(s"min: $minSize, max: $maxSize")

    addChecker(defn, check(minSize, maxSize))
  }
}

/**
 * annotate the value is
 *  - greater than or equals to `min`
 *  - less than or equals to `max`
 *
 * @param min
 * @param max
 */
class IntRange(min: Option[Int] = None, max: Option[Int] = None) extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {

    def check(min: Option[Int], max: Option[Int])(name: Name, term: Term): Term = {
      def i = Lit.Int.apply _

      def s = Lit.String.apply _

      def patOptI(opt: Option[Int]) = s"$opt".parse[Term].get

      q"""
          def msg(value: Option[Int]) = {
            val minCond = if (${patOptI(min)}.isEmpty) ${s("")} else ${patOptI(min)}.get + ${s(" <=")}
            val maxCond = if (${patOptI(max)}.isEmpty) ${s("")} else ${s("<= ")} + ${patOptI(max)}.get
            ${Lit.String(name.syntax)} + ${s(" is invalid. ")} + minCond + ${s(" ")} value + ${s(" ")} + maxCond
          }
          def minValid(min: Option[Int], value: Int): Boolean = min <= value
          def maxValid(max: Option[Int], value: Int): Boolean = max >= value

          val result: Int = $term
          require(minValid(${patOptI(min)}, result) && maxValid(${patOptI(max)}, result), msg(result))
          result
         """
    }

    val (minSize, maxSize) = argumentMinMaxOpt(this)

//      println(s"min: $minSize, max: $maxSize")

    addChecker(defn, check(minSize, maxSize))
  }
}
