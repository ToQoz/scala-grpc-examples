import sbt.Compile

scalaVersion := "2.13.3"

val protocVersion = "3.12.2"
val akkaGrpcVersion = "1.0.2"

lazy val exampleCore = Project(
  id = "example-core",
  base = file("example-core")
)

lazy val defaultSettings = Seq[sbt.Def.SettingsDefinition](
  libraryDependencies ++= Seq(
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
  ),
  PB.protocVersion := protocVersion,
  Compile / PB.protocOptions += "--experimental_allow_proto3_optional",
  Compile / PB.protoSources := Seq((baseDirectory in LocalRootProject).value / "example-core/src/main/protobuf"),
  Compile / PB.targets ++= Seq(
    scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
  )
)

lazy val exampleAkkaGrpc = Project(
  id = "example-akka-grpc",
  base = file("example-akka-grpc")
).settings(defaultSettings: _*).enablePlugins(AkkaGrpcPlugin)

lazy val exampleGrpcJava = Project(
  id = "example-grpc-java",
  base = file("example-grpc-java")
).dependsOn(exampleCore)
  .settings(defaultSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
      "io.grpc" % "grpc-services" % scalapb.compiler.Version.grpcJavaVersion
    )
  )
