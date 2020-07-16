name := "firstScalaProject"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.4.3"
lazy val commonDependencies =
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion ,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
    "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,
    "com.datastax.spark" %% "spark-cassandra-connector" % "3.0-alpha"
  )

lazy val Consumer = (project in file("Consumer")).settings(
  commonDependencies

)

lazy val Producer = (project in file("Producer")).settings(
  commonDependencies

)
lazy val Model = (project in file("Model")).settings(
  commonDependencies

)

