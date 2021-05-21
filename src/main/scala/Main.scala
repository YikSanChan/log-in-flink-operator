import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object Main {

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
    tableEnv.toDataStream(table).print()
    execEnv.execute("Streaming")
  }
}