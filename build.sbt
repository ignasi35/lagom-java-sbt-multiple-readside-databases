organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `multirdbmsjava` = (project in file("."))
  .aggregate(`multirdbmsjava-api`, `multirdbmsjava-impl`)

lazy val `multirdbmsjava-api` = (project in file("multirdbmsjava-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `multirdbmsjava-impl` = (project in file("multirdbmsjava-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceJdbc,
      "mysql" % "mysql-connector-java" % "6.0.6",
      javaJdbc,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`multirdbmsjava-api`)


val lombok = "org.projectlombok" % "lombok" % "1.16.18"

def common = Seq(
  javacOptions in compile += "-parameters"
)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false


