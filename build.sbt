name := "firstScalaProject"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.4.3"


lazy val commonDependencies = Seq(
    "org.apache.spark" %% "spark-streaming" % sparkVersion ,
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,

  )

lazy val Consumer = (project in file("Consumer")).settings(
  libraryDependencies ++= commonDependencies ++ Seq("com.datastax.spark" %% "spark-cassandra-connector" % "3.0-alpha","org.apache.spark" %% "spark-sql" % sparkVersion),
  name := "Consumer"

)

lazy val Producer = (project in file("Producer")).settings(
  libraryDependencies ++= commonDependencies ,
  name := "Producer"

)
lazy val Model = (project in file("Model")).settings(
  libraryDependencies ++=Seq("org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion),
  name := "Model"
)

