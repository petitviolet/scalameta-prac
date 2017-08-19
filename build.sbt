import sbt.Keys._

val libVersion = "1.0"

val scala = "2.12.2"

// https://github.com/scalameta/sbt-macro-example/blob/master/build.sbt
lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
//  resolvers += Resolver.url(
//    "bintray-sbt-plugin-releases",
//    url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
//    Resolver.ivyStylePatterns),
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven"),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M9" cross CrossVersion.full),
  // for old-style macro
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
  ),
  scalacOptions ++= Seq(
    "-Xplugin-require:macroparadise",
    "-Ymacro-debug-lite"
  )
)

def commonSettings(moduleName: String, _scalaVersion: String = scala) = Seq(
  name := moduleName,
  scalaVersion := _scalaVersion,
  version := "1.0"
)

lazy val scalaMetaPractice = (project in file("."))
  .settings(commonSettings("scalameta-prac"))
  .aggregate(metas, mcr, metaApp, mcrApp)


lazy val mcr = (project in file("modules/mcr"))
  .settings(commonSettings("mcr", "2.11.11"))
  .settings(metaMacroSettings)

lazy val mcrApp = (project in file("modules/mcr_app"))
  .settings(commonSettings("mcrApp", "2.11.11"))
  .settings(metaMacroSettings)
  .dependsOn(mcr)

lazy val metas = (project in file("modules/metas"))
  .settings(commonSettings("metas"))
  .settings(metaMacroSettings)
  .settings(libraryDependencies += "org.scalameta" %% "scalameta" % "1.8.0")

lazy val metaApp = (project in file("modules/meta_app"))
  .settings(commonSettings("metaApp"))
  .settings(metaMacroSettings)
  .dependsOn(metas)

