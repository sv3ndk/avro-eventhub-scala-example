package org.svend.playground

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericData.Record

/**
  * Created by svend on 25/06/2017.
  */
case class UserMessage(userId: Int, mood: String, message: String) {
  def toAvro: Record = {
    val rec = new GenericData.Record(UserMessage.schema)
    rec.put("user_id", userId)
    rec.put("mood", mood)
    rec.put("message", message)
    rec
  }

}

object UserMessage {

  val schema = new Schema.Parser().parse("""{"namespace": "svend.playground.user",
    "type": "record",
    "name": "User",
    "fields": [
    {"name": "user_id",  "type": ["int", "null"]},
    {"name": "mood",  "type": ["string", "null"]},
    {"name": "message", "type": ["string", "null"]}
    ]
  }""")



}