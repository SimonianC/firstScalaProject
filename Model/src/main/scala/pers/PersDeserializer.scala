package pers;

import java.io.UnsupportedEncodingException

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}

class PersDeserializer extends Deserializer[Pers]{
  var encoding = "UTF8"

  def isPerson(x: Any): Boolean = x match {
    case p: String => true
    case _ => false
  }


  def configure(configs: Map[String, Pers], isKey: Boolean): Unit = {
    val propertyName :+ String = if(isKey)  "key.deserializer.encoding" else  "value.deserializer.encoding"
    var encodingValue = configs.get(propertyName)
    if (encodingValue == null) {
      encodingValue = configs.get("deserializer.encoding")
    }
    if (this.isPerson(encodingValue))
      encoding =  encodingValue.toString
  }


  def deserialize(topic: String, data: Byte): Pers = {
    try {
      if (data == null)
         null;
      else {
        val tmp = data.toString.split(",")
        Pers(tmp(0).toInt, tmp(1), tmp(2).toInt)
      }
    } catch {
    case e : UnsupportedEncodingException =>throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding " + encoding)
    } finally {
      "no matching in PersDeserializer.deserialize"
    }

  }


}
