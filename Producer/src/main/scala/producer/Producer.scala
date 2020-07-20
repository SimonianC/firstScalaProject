package producer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import pers.{Pers, PersDeserializer, PersSerialize, Unmarshaller}

import scala.concurrent.{Await, Future}







object Producer extends App{


    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", classOf[PersSerialize])
    props.put("group.id", "something")

    val conf = new SparkConf(true)
      .setAppName("producer")
      .setMaster("local[*]")

    //Create the KAFKA producer
    val producer = new KafkaProducer[String, Pers](props)



    // Create the SparkContext and the SparkContext
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext( sc , Seconds(5))



    // Read the CSV file with the SparkContext
    val persRdd = sc.textFile("/home/mind7/Bureau/batch_folder/*.csv")


    persRdd.mapPartitions( iterEl =>{
        iterEl.map( stringEl=> {
            Unmarshaller.unmarshallValeursFoncieres(stringEl.split(",").toList)
        })
    }).foreachPartition(partition => {
         partition.foreach( persEl => {
             val record = new ProducerRecord("mind7", "key", persEl)
             (producer.send(record).get)
         })
        //Await.result(Future.sequence(futureResults), 10 seconds)

    })
    producer.close()

}
