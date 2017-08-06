import sbt.Keys._

val libVersion = "1.0"

val scala = "2.11.8"

// https://github.com/scalameta/sbt-macro-example/blob/master/build.sbt
lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.url(
    "bintray-sbt-plugin-releases",
    url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
    Resolver.ivyStylePatterns),
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven"),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M7" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
  ),
  scalacOptions ++= Seq(
    "-Xplugin-require:macroparadise",
    "-Ymacro-debug-lite"
  )
)

def commonSettings(moduleName: String) = Seq(
  name := moduleName,
  scalaVersion := scala,
  version := "1.0"
)

lazy val scalaMetaPractice = (project in file("."))
  .settings(commonSettings("scalameta-prac"))
  .aggregate(metas, app)


lazy val metas = (project in file("modules/metas"))
  .settings(commonSettings("metas"))
  .settings(metaMacroSettings)
  .settings(libraryDependencies += "org.scalameta" %% "scalameta" % "1.8.0")

lazy val app = (project in file("modules/app"))
  .settings(commonSettings("app"))
  .settings(metaMacroSettings)
  .dependsOn(metas)
