# scala metaprogramming practice repository

- [Macros - ユースケース - Scala Documentation](http://docs.scala-lang.org/ja/overviews/macros/usecases)
- [scala.meta](http://scalameta.org/)

# util annotation

## for DI

- [@Uses\[T\]/@MixIn\[T\](tImpl)](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/uses.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/UsesApp.scala)

```scala
trait DoubleService { def double(n: Int) = n * 2 }
object DoubleServiceImpl extends DoubleService

trait TripleService { def triple(n: Int) = n * 3 }
class TripleServiceImpl extends TripleService

@Uses[DoubleService]
@Uses[TripleService]
trait UsesServices {
  def showDouble(n: Int) = println(this.doubleService.double(n))
  def showTriple(n: Int) = println(this.tripleService.triple(n))
}

@MixIn[DoubleService](DoubleServiceImpl)
@MixIn[TripleService](new TripleServiceImpl)
class UsesFieldServicesImpl extends UsesServices

object UsesApp extends App {
  val impl = new UsesFieldServicesImpl
  impl.showDouble(100) // 200
  impl.showTriple(100) // 300
}
```

## validation annotation

- `@NonNull`
- `@NonEmpty`
- `@Length(min, max)`
- `@IntRange(min, max)`

## implementation 

- [validator.scala](https://github.com/petitviolet/scalameta-prac/blob/master/modules/metas/src/main/scala/net/petitviolet/metas/validator.scala)
    - [example](https://github.com/petitviolet/scalameta-prac/blob/master/modules/app/src/main/scala/net/petitviolet/metas/app/ValidatorApp.scala)

# Lisence

[MIT License](https://petitviolet.mit-license.org/)
