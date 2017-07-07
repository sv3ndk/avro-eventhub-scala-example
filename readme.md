Azure event hub client. 

## How to send messages: 

This generates a bunch of random avro messages and post them to eventHub:


```
 sbt "runMain be.svend.eventhub.producer.EventHubAvroSender <some eventHub connection string> "
```



## How to receive messages:

This starts a receiver connected to an EventHub instance and save the messages to files in the specified path:

```
sbt "runMain be.svend.eventhub.consumer.EventHubReceiver /tmp/raw_ais 0 <some eventHub connection string> "
```
