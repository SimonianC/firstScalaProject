package pers

import java.io.UnsupportedEncodingException
import java.util

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer

class PersSerialize extends Serializer[Pers]{
  var encoding = "UTF8"
  def isPerson(x: Any): Boolean = x match {
    case p: String => true
    case _ => false
  }

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
    val propertyName : String = if(isKey)  "key.serializer.encoding" else  "value.serializer.encoding"
    var encodingValue = configs.get(propertyName)
    if (encodingValue == null) {
      encodingValue = configs.get("deserializer.encoding")
    }
    if (this.isPerson(encodingValue))
      encoding =  encodingValue.toString
  }

  override def serialize(topic: String, data: Pers): Array[Byte] = {
    try {
      if (data == null) {
         null
      }
      else {
        data.toString.getBytes(encoding)
      }
    }catch {
      case e: UnsupportedEncodingException =>
        throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + encoding)
    }
  }

  override def close(): Unit = {}
}
