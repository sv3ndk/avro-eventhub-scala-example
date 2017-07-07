package be.svend.eventhub.producer

import java.io.ByteArrayOutputStream
import com.microsoft.azure.eventhubs.{EventData, EventHubClient}
import org.apache.avro.file.{Codec, CodecFactory, DataFileWriter}
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}

import scala.collection.JavaConverters._

/**
  * Created by svend on 25/06/2017.
  *
  * Quick and dirty EventHub message producer, sending randomly generated avro events
  *
  * sbt "runMain be.svend.eventhub.producer.EventHubAvroSender <eventHub connection string>  "
  *
  */
object EventHubAvroSender extends App {

  val connectionString = args(0)

  val ehClient = EventHubClient.createFromConnectionStringSync(connectionString)

  // number of batches to send to the EventHub
  val batch_num = 10

  // number of EventData instance to put inside each batch
  val amqpMessagePerBatch = 15

  // number of avro message to bundle inside each AMQP mesasge
  val userMessagesPerAmqp = 20


  val datumWriter = new GenericDatumWriter[GenericRecord](UserMessage.schema)
  val dataFileWriter = new DataFileWriter[GenericRecord](datumWriter)

  // only deflate seem to be compatible with Azure at the moment
  //dataFileWriter.setCodec( CodecFactory.deflateCodec(9))
  //dataFileWriter.setCodec( CodecFactory.snappyCodec())
  //dataFileWriter.setCodec( CodecFactory.bzip2Codec())

  val futures = (1 to batch_num).map{ batchid =>

    // building one EventData instance with a bunch of Avro messages
    val eventHubMessages = (1 to amqpMessagePerBatch).map { _ =>

        val bos = new ByteArrayOutputStream()
        dataFileWriter.create(UserMessage.schema, bos)
        (1 to userMessagesPerAmqp).foreach { _ =>
          dataFileWriter.append(MessageGen.genMessage.toAvro)
        }
        dataFileWriter.close()
        bos.close()

        new EventData(bos.toByteArray)
      }

    println(s"sending batch $batchid")

    // this sends a batch of EventData asynchronously and returns a Java Future
    ehClient.send(eventHubMessages.asJava)
  }

  println("waiting for all futures before exiting...")
  futures.foreach(_.get())

  println(s"ok, closing")
  ehClient.close()

  println(s"done")

}
