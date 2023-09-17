ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / organization := "com.peknight"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    "-Xmax-inlines:64"
  ),
)

lazy val ipAddress = (project in file("."))
  .aggregate(
    ipAddressCore.jvm,
    ipAddressCore.js,
    ipAddressHttp4s,
  )
  .settings(commonSettings)
  .settings(
    name := "ip-address"
  )

lazy val ipAddressCore = (crossProject(JSPlatform, JVMPlatform) in file("ip-address-core"))
  .settings(commonSettings)
  .settings(
    name := "ip-address-core",
    libraryDependencies ++= Seq(
    ),
  )

lazy val ipAddressHttp4s = (project in file("ip-address-http4s"))
  .aggregate(
    ipAddressHttp4sService.jvm,
    ipAddressHttp4sService.js,
    ipAddressHttp4sApp.jvm,
    ipAddressHttp4sApp.js,
  )
  .settings(commonSettings)
  .settings(
    name := "ip-address-http4s"
  )

lazy val ipAddressHttp4sService = (crossProject(JSPlatform, JVMPlatform) in file("ip-address-http4s/service"))
  .dependsOn(ipAddressCore)
  .settings(commonSettings)
  .settings(
    name := "ip-address-http4s-service",
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-core" % http4sVersion,
    ),
  )

lazy val ipAddressHttp4sApp = (crossProject(JSPlatform, JVMPlatform) in file("ip-address-http4s/app"))
  .dependsOn(ipAddressHttp4sService)
  .settings(commonSettings)
  .settings(
    name := "ip-address-http4s-app",
    libraryDependencies ++= Seq(
    ),
  )

val http4sVersion = "1.0.0-M34"
