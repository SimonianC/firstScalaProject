package producer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import pers.PersSerialize


object Producer extends App{


    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", classOf[PersSerialize])//)"org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", classOf[PersSerialize])//"org.apache.kafka.common.serialization.StringSerializer")
    props.put("group.id", "something")

    val conf = new SparkConf(true)
      .setAppName("producer")
      .setMaster("local[*]")

    //Create the KAFKA producer
    val producer = new KafkaProducer[String, String](props)



    // Create the SparkContext and the SparkContext
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext( sc , Seconds(5))



    // Read the CSV file with the SparkContext
    val persRdd = sc.textFile("/home/mind7/Bureau/batch_folder/*.csv")
    // TODO optimize with spark
    //implicit val ec = ExecutionContext.global
    persRdd.collect.foreach( el => {

          val record = new ProducerRecord("mind7", el.split(",")(0), el  )
          producer.send(record)

    })

    producer.close()

}
