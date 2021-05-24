import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.functions.sink.DiscardingSink
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
import org.slf4j.LoggerFactory

trait BaseJob {
  protected final val LOG = LoggerFactory.getLogger(getClass)

  def preprocess(tableEnv: StreamTableEnvironment): DataStream[AnyRef]

  def process(stream: DataStream[AnyRef]): DataStreamSink[AnyRef] = {
    stream
      .map { a =>
        // This LOG line throws Task not serializable
        // Commenting out the LOG line, then the LOG line in Job.scala works just fine
        LOG.info("[BaseJob] a = " + a)
        a
      }
      .addSink(new DiscardingSink[AnyRef])
  }
}
