import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
//import org.apache.flink.api.scala._

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

  private val CreateSink =
    """
      |CREATE TABLE sink (
      |  a int
      |) WITH (
      |  'connector' = 'print'
      |)
      |""".stripMargin

  private val Transform = "INSERT INTO sink SELECT a FROM source"

  def main(args: Array[String]): Unit = {
    val settings = EnvironmentSettings.newInstance.build
    val execEnv: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv = StreamTableEnvironment.create(execEnv, settings)
    tableEnv.executeSql(CreateSource)
    tableEnv.executeSql(CreateSink)
    tableEnv.executeSql(Transform).wait()
//    val table = tableEnv.sqlQuery("SELECT a FROM source")
//    tableEnv.toAppendStream[Int](table).print()
  }
}