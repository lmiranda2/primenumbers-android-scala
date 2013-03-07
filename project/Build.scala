import sbt._

import Keys._
import AndroidKeys._
import AndroidNdkKeys._

object General {
  // Some basic configuration
  val settings = Defaults.defaultSettings ++ Seq (
    name := "primenumbers-android-scala",
    version := "0.1",
    versionCode := 0,
    scalaVersion := "2.10.1-RC2",
    scalacOptions := Seq("-language:implicitConversions", "-feature", "-unchecked", "-deprecation", "-encoding", "utf8"),
    platformName in Android := "android-17",
    parallelExecution in Test := false
  )

  // Default Proguard settings
  lazy val proguardSettings = inConfig(Android) (Seq (
    useProguard := true,
    proguardOptimizations += "-keep class edu.luc.etl.cs313.android.scala.** { *; }"
  ))

  // Example NDK settings
  lazy val ndkSettings = AndroidNdk.settings ++ inConfig(Android) (Seq(
    jniClasses := Seq(),
    javahOutputFile := Some(new File("native.h"))
  ))

  // Full Android settings
  lazy val fullAndroidSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    proguardSettings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "change-me",
      libraryDependencies ++= Seq(
        "junit" % "junit" % "4.11",
        "com.google.dexmaker" % "dexmaker" % "1.0",
        "org.mockito" % "mockito-core" % "1.9.5",
        "org.robolectric" % "robolectric" % "2.0-alpha-1",
        "com.novocode" % "junit-interface" % "0.10-M2" % "test",
        "org.scalatest" % "scalatest_2.10.0-RC3" % "2.0.M5-B1" % "test"
      )
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    "main",
    file("."),
    settings = General.fullAndroidSettings ++ AndroidEclipseDefaults.settings
  )
}