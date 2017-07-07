package be.svend.eventhub.consumer

import com.microsoft.azure.eventhubs.{EventData, EventHubClient}
import java.io.{BufferedOutputStream, FileOutputStream}
import scala.collection.JavaConverters._

/**
  *
  * Quick and dirty reader for Azure EventHub client
  *
  * Use as follows:
  *
  * sbt "runMain be.svend.eventhub.consumer.EventHubReceiver /tmp/raw_ais 0 <connection string to EvenHub> "
  * */
object EventHubReceiver extends App {

    val fileBaseName = args(0)
    val partitionId = args(1)
    val connectionString = args(2)

    println(s"Starting eventHub receiver, listing to partition $partitionId and outputing to $fileBaseName")

    val client = EventHubClient.createFromConnectionStringSync(connectionString)
    val receiver = client.createReceiverSync("$Default",  partitionId, "-1", false)

    def saveBytes(bytes: Array[Byte], fileOut: String): Unit = {
      println(s"saving payload to $fileOut")
      val bos = new BufferedOutputStream(new FileOutputStream(fileOut))
      bos.write(bytes)
      bos.close()
    }

    def filename = s"$fileBaseName-${System.currentTimeMillis}.eventhub"

    while (true) {

      receiver.receiveSync(10).asScala.foreach{e => saveBytes(e.getBytes, filename)}
    }
}