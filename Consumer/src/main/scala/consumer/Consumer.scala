package consumer

import org.apache.spark.sql.Encoder
import com.datastax.spark.connector._
import com.datastax.spark.connector.SomeColumns
//import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import pers.{Pers ,PersDeserializer}

object Consumer extends App {
  val conf = new SparkConf(true)
    .setAppName("consumer")
    .setMaster("local[*]")
    .set("spark.cassandra.connection.host", "0.0.0.0")
    .set("spark.cassandra.connection.port", "9042");

  val sc = new SparkContext(conf)

  val ssc = new StreamingContext( sc , Seconds(5))

  val preferredHosts = LocationStrategies.PreferConsistent
  val topics = Array("mind7")

  //CassandraConnector(conf)


  //SparkStreaming configuration for KAFKA
  val kafkaParams = Map(
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" -> classOf[PersDeserializer],
    "group.id" -> "use_a_separate_group_id_for_each_stream",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" ->  (false : java.lang.Boolean)
  )

  val columns = SomeColumns("id", "nom", "age")
  println("creation dstream")
  val dstream = KafkaUtils.createDirectStream[String, Pers](
    ssc,
    preferredHosts,
    ConsumerStrategies.Subscribe[String, Pers](topics, kafkaParams))

  println("execution du foreach")
  dstream.foreachRDD( rdd => {
    rdd.map(el =>{
      (el.value.id,el.value.nom,el.value.age)
    }).saveToCassandra("test","pers",columns)
  })



  ssc.start()
  ssc.awaitTermination()


}
