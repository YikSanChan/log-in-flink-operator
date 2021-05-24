import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.slf4j.LoggerFactory

object Job extends BaseJob {

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

  private def run(): JobExecutionResult = {
    val settings = EnvironmentSettings.newInstance.build
    val execEnv: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv = StreamTableEnvironment.create(execEnv, settings)
    val stream = preprocess(tableEnv)
    process(stream)
    execEnv.execute("Streaming")
  }

  override def preprocess(tableEnv: StreamTableEnvironment): DataStream[AnyRef] = {
    tableEnv.executeSql(CreateSource)
    val table = tableEnv.sqlQuery("SELECT a FROM source")
    tableEnv
      .toDataStream(table)
      .map {row =>
        val a = row.getField("a")
        LOG.info("a = " + a)
        a
      }
  }

  def main(args: Array[String]): Unit = {
    run()
  }
}