# Log in Flink operator

The repo helps reproduce the problem stated here: http://apache-flink-user-mailing-list-archive.2336050.n4.nabble.com/Task-not-serializable-when-logging-in-a-trait-method-td43911.html

To reproduce, simply run the `main` method in `Job` class.

---

I try to log in both BaseJob and Job.
The log line in Job works just fine.
However, the log line in BaseJob throws "Task not serializable" exception.

Here's the full error trace:
```
Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: Task not serializable
	at org.apache.flink.api.scala.ClosureCleaner$.ensureSerializable(ClosureCleaner.scala:408)
	at org.apache.flink.api.scala.ClosureCleaner$.org$apache$flink$api$scala$ClosureCleaner$$clean(ClosureCleaner.scala:400)
	at org.apache.flink.api.scala.ClosureCleaner$.clean(ClosureCleaner.scala:168)
	at org.apache.flink.streaming.api.scala.StreamExecutionEnvironment.scalaClean(StreamExecutionEnvironment.scala:914)
	at org.apache.flink.streaming.api.scala.DataStream.clean(DataStream.scala:1189)
	at org.apache.flink.streaming.api.scala.DataStream.map(DataStream.scala:623)
	at BaseJob$class.process(BaseJob.scala:15)
	at Job$.process(Job.scala:7)
	at Job$.run(Job.scala:25)
	at Job$.main(Job.scala:42)
	at Job.main(Job.scala)
Caused by: java.io.NotSerializableException: Job$
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1193)
	at java.base/java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1579)
	at java.base/java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1536)
	at java.base/java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1444)
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1187)
	at java.base/java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:353)
	at org.apache.flink.util.InstantiationUtil.serializeObject(InstantiationUtil.java:624)
	at org.apache.flink.api.scala.ClosureCleaner$.ensureSerializable(ClosureCleaner.scala:406)
	... 10 more
```

I wonder why, and how to resolve the issue?