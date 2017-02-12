# scala metaprogramming practice repository

- [Macros - ユースケース - Scala Documentation](http://docs.scala-lang.org/ja/overviews/macros/usecases)
- [scala.meta](http://scalameta.org/)

# util annotation

## class to case class

- [@Apply](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/Apply.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/ApplyApp.scala)
- [@Unapply](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/Unapply.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/UnapplyApp.scala)
- [@ToString](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/ToString.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/ToStringApp.scala)
- [@Equals](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/Equals.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/EqualsApp.scala)

### @Case annotation

`@Case` is an annotation to insert whole methods as a case class to instance itself and companion object.

[@Case](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/Case.scala)

Before expansion.

```scala
@Case
class CaseApp(val n: Int, val s: String)
```

After compilation and expansion.

```scala
class CaseApp(val n: Int, val s: String) {
  override def toString: String = {
    "CaseApp" + "(" + ("n" + ": " + n.toString + ", " + "s" + ": " + s.toString) + ")"
  }

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[CaseApp]) false else {
      val other = obj.asInstanceOf[CaseApp]
      this.n == other.n && this.s == other.s
    }
  }
}
object CaseApp {
  def unapply(arg: CaseApp): Option[(Int, String)] = {
    Some((arg.n, arg.s))
  }

  def apply(n: Int, s: String): CaseApp = new CaseApp(n, s)
}
```

# for DI

- [@Uses\[T\]/@MixIn\[T\](tImpl)](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/uses.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/UsesApp.scala)

# validation annotation

- `@NonNull`
- `@NonEmpty`
- `@Length(min, max)`
- `@IntRange(min, max)`

## implementation 

- [validator.scala](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/validator.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/ValidatorApp.scala)

