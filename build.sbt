//val dottyVersion = "3.1.3-RC1-bin-SNAPSHOT"
val dottyVersion = "3.2.0"
//val dottyVersion = "3.0.2-RC1-bin-SNAPSHOT"

ThisBuild/version := "0.9.11"
ThisBuild/organization := "com.github.rssh"

Global / concurrentRestrictions += Tags.limit(ScalaJSTags.Link, 1)

lazy val commonSettings = Seq(
   scalaVersion := dottyVersion,
   libraryDependencies += "com.github.rssh" %%% "dotty-cps-async" % "0.9.11",
   libraryDependencies += "org.scalameta" %%% "munit" % "0.7.29" % Test,
   testFrameworks += new TestFramework("munit.Framework")
)


lazy val scalaz  = crossProject(JSPlatform, JVMPlatform)
  .in(file("scalaz"))
  .settings(
    commonSettings,
    name := "cps-async-connect-scalaz",
    libraryDependencies += "org.scalaz" %%% "scalaz-effect" % "7.4.0-M12" ,
    libraryDependencies += "org.scalaz" %%% "scalaz-core" % "7.4.0-M12" 
  ).jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    scalaJSUseMainModuleInitializer := true
  )


lazy val catsEffect  = crossProject(JSPlatform, JVMPlatform)
  .in(file("cats-effect"))
  .settings(
    commonSettings,
    name := "cps-async-connect-cats-effect",
    libraryDependencies += "org.typelevel" %%% "cats-effect" % "3.3.14",
    libraryDependencies += "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7" % Test
  ).jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    scalaJSUseMainModuleInitializer := true,
  ).jvmSettings(
    scalacOptions ++= Seq( "-unchecked", "-explain")
  )


lazy val monix  = crossProject(JSPlatform, JVMPlatform)
  .in(file("monix"))
  .settings(
    commonSettings,
    name := "cps-async-connect-monix",
    libraryDependencies += "io.monix" %%% "monix" % "3.4.1",
  ).jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    scalaJSUseMainModuleInitializer := true
  ).jvmSettings(
  )

lazy val zio  = crossProject(JSPlatform, JVMPlatform)   
  .in(file("zio"))
  .settings(
    commonSettings,
    name := "cps-async-connect-zio",
    libraryDependencies ++= Seq(
      "dev.zio" %%% "zio" % "1.0.16",
      "dev.zio" %%% "zio-streams" % "1.0.16",
    )
  ).jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    //scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.3.0",
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.3.0"
    ),
  ).jvmSettings(
    scalacOptions ++= Seq( "-unchecked", "-Ydebug-trace", "-Ydebug-names", "-Xprint-types",
                            "-Ydebug", "-uniqid", "-Ycheck:macros",  "-Yprint-syms" )
  )

lazy val zio2  = crossProject(JSPlatform, JVMPlatform)   
  .in(file("zio2"))
  .settings(
    commonSettings,
    name := "cps-async-connect-zio2",
    libraryDependencies ++= Seq(
      "dev.zio" %%% "zio" % "2.0.2",
      "dev.zio" %%% "zio-managed" % "2.0.2",
      "dev.zio" %%% "zio-streams" % "2.0.2",
    )
  ).jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    //scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.4.0-M1",
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.4.0-M1"
    ),
  ).jvmSettings(
    scalacOptions ++= Seq( "-unchecked", "-Ydebug-trace", "-Ydebug-names", "-Xprint-types",
                            "-Ydebug", "-uniqid", "-Ycheck:macros",  "-Yprint-syms", "-explain" )
  )


lazy val streamFs2 = crossProject(JSPlatform, JVMPlatform)
                     .in(file("stream-fs2"))
                     .dependsOn(catsEffect)
                     .settings(
                         commonSettings,
                         name := "cps-async-connect-fs2",
                         libraryDependencies ++= Seq(
                             "co.fs2" %%% "fs2-core" % "3.3.0",
                             "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7" % Test
                         )
                      )

lazy val streamAkka = (project in file("stream-akka")).
                      settings(
                         commonSettings,
                         name := "cps-async-connect-akka-stream",
                         scalacOptions += "-explain",
                         libraryDependencies ++= Seq(
                            ("com.typesafe.akka" %% "akka-stream" % "2.6.20")
                         )
                      )


lazy val root = (project in file("."))
                .aggregate(catsEffect.jvm, catsEffect.js,
                           monix.jvm, monix.js,
                           scalaz.jvm, scalaz.js , 
                           zio.jvm,  zio.js,
                           zio2.jvm,  zio2.js,
                           streamFs2.jvm, streamFs2.js,
                           streamAkka
                )
                .settings(
                   publish := {},
                   publishLocal := {},
                   publishArtifact := false
                )


