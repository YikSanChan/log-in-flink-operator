import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.sink.DiscardingSink
import org.slf4j.LoggerFactory

object Main {

  private final val LOG = LoggerFactory.getLogger(getClass)

  private val CreateSource =
    """
      |CREATE TABLE source (
      |  a int
      |) WITH (
      |  'connector' = 'datagen',
      |  'rows-per-second' = '5'
      |)
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    val settings = EnvironmentSettings.newInstance.build
    val execEnv: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv = StreamTableEnvironment.create(execEnv, settings)
    tableEnv.executeSql(CreateSource)
    val table = tableEnv.sqlQuery("SELECT a FROM source")
    tableEnv
      .toDataStream(table)
      .map {row =>
        val a = row.getField("a")
        LOG.info("a = " + a)
        a
      }
      .addSink(new DiscardingSink[AnyRef])
    execEnv.execute("Streaming")
  }
}