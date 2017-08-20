logLevel := Level.Warn

resolvers += Resolver.bintrayIvyRepo("scalameta", "maven")
resolvers += Resolver.typesafeRepo("releases")

// JMH
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.20")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")

// Formatter plugins
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.0")
