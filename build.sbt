import sbt.Keys._

val libVersion = "1.0"

val scala211 = "2.11.11"
val scala212 = "2.12.2"

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

// not working
// lazy val scalametaVersion = "2.0.0-RC1"
lazy val scalametaVersion = "1.8.0"
lazy val scalametaDependencies = Seq(
  "org.scalameta" %% "scalameta" % scalametaVersion
)

lazy val scalahostSettings = Seq(
  addCompilerPlugin("org.scalameta" % "scalahost" % "1.8.0" cross CrossVersion.full),
  scalacOptions ++= Seq(
    "-Yrangepos",
    "-Xplugin-require:scalahost"
  )
)

def commonSettings(moduleName: String, _scalaVersion: String) = Seq(
  name := moduleName,
  scalaVersion := _scalaVersion,
  version := "1.0"
)

lazy val scalaMetaPractice = (project in file("."))
  .settings(commonSettings("scalameta-prac", scala212))
  .aggregate(metas, mcr, metaApp, mcrApp)


lazy val mcr = (project in file("modules/mcr"))
  .settings(commonSettings("mcr", scala211))
  .settings(metaMacroSettings)

lazy val mcrApp = (project in file("modules/mcr_app"))
  .settings(commonSettings("mcrApp", scala211))
//  .settings(metaMacroSettings)
  .dependsOn(mcr)

lazy val metas = (project in file("modules/metas"))
  .settings(commonSettings("metas", scala212))
  .settings(metaMacroSettings)
  .settings(libraryDependencies ++= scalametaDependencies)

lazy val metaApp = (project in file("modules/meta_app"))
  .settings(commonSettings("metaApp", scala212))
  .settings(metaMacroSettings)
  .dependsOn(metas)

lazy val metaMirror = (project in file("modules/meta_mirror"))
  .settings(commonSettings("metaMirror", scala212))

lazy val metaMirrorApp = (project in file("modules/meta_mirror_app"))
  .settings(commonSettings("metaMirrorApp", scala212))
  .settings(libraryDependencies ++= scalametaDependencies)
  .settings(scalahostSettings)
//  .dependsOn(metaMirror % Scalameta)  // enable only for run this pr
