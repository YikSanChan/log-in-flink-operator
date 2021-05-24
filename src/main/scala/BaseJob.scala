import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.functions.sink.DiscardingSink
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

trait BaseJob {
  def preprocess(tableEnv: StreamTableEnvironment): DataStream[AnyRef]

  def process(stream: DataStream[AnyRef]): DataStreamSink[AnyRef] = {
    stream.addSink(new DiscardingSink[AnyRef])
  }
}
