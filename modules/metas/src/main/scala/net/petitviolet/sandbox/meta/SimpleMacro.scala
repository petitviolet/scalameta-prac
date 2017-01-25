package net.petitviolet.sandbox.meta

import net.petitviolet.sandbox.meta.macros.Class2Map

import scala.collection.immutable.{Seq, Stack}
import scala.meta._
import scala.meta.tokens.Token._
import scala.util.{Failure, Success, Try}

object MetaExapmle extends App {
  val classSource: Parsed[Source] = """case class Bar(value: String)""".parse[Source]
  val nameStat: Parsed[Stat] = """val name = "hoge"""".parse[Stat]
  val valName: Defn.Val = q"""val name = "hoge""""
  val defName: Defn.Def = q"""def name = "hoge""""
  val fooClass: Defn.Class = q"case class Foo(value: Int) extends AnyVal"

  val target =
    """|case class Hoge(value: Int)
       |object a {
       |  x match {
       |    case Hoge(i) => println(i)
       |    case bar: Throwable => println(bar)
       |  }
       |}
       |""".stripMargin

  val result: Seq[String] = target.parse[Source].get.collect {
    case c @ Case((p"${(name: Pat)}: Throwable", cond: Option[Term], body: Term)) =>
//      println(s"case => $c, ($name, $cond, $body)")
      s"NonFatal(${name.syntax}) => $body"
//    case c @ p"case $name: Throwable => $expr" =>
//      println(s"case => $c, class => ${c.getClass}, name: $name, expr => $expr")
//      c
    case other => other.syntax
  }
  println(result.mkString(""))
}



trait Logging {
  def logging(msg: => String, suppress: Boolean = true) = {
    if (!suppress) println(msg)
  }
}

private object TokenExercize extends App with Logging {

  val tokenizedSeq = Seq(
    """val x = 10""",
    """case class Foo(value: Int) { def double() = value * 2 }""",
    """case class Foo(value: Int) { def double( = value * 2 }""",
    """case class Foo(value: Int)  def double() = value * 2 }"""
  ).map {_.tokenize.get}

  // 括弧が文法的に合っているかどうか
  def isBalanced(tokens: Tokens): Boolean = {
    Try {
      tokens.foldLeft(Stack.empty[Token]) { (stack, token) =>
        token match {
          case open if isOpen(open) =>
            logging(s"open: token => `$open`, stack => $stack")
            stack.push(open)
          case close if isClose(close) && stack.isEmpty =>
            sys.error(s"stack is empty, but found close-parenthesis: `$close`")
          case close if isClose(close) && stack.nonEmpty =>
            val (poped, newStack) = stack.pop2
            logging(s"close: stack => $newStack")
            if (isBalancedParenthesis(poped, close)) newStack
            else sys.error(s"invalid parenthesis! left: `$poped`, right: `$close`")
          case default =>
            logging(s"default: token => `$default`, stack => $stack")
            stack
        }
      }
    } match {
      case Success(_) =>
        logging(s"tokens: $tokens is good!", false)
        true
      case Failure(t) =>
        logging(s"tokens: $tokens is bad! cause :$t", false)
        false
    }
  }

  private def isBalancedParenthesis(left: Token, right: Token): Boolean = {
    (left, right) match {
      case (LeftParen(), RightParen()) =>
        // ()
        true
      case (LeftBrace(), RightBrace()) =>
        // {}
        true
      case (LeftBracket(), RightBracket()) =>
        // []
        true
      case _ => false
    }
  }

  private def isOpen(token: Token): Boolean = token match {
    case LeftParen() | LeftBrace() | LeftBracket() => true
    case _ => false
  }

  private def isClose(token: Token): Boolean = token match {
    case RightParen() | RightBrace() | RightBracket() => true
    case _ => false
  }

  tokenizedSeq foreach isBalanced
}

