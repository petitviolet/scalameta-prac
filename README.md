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
- TODO:
   - equals

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

